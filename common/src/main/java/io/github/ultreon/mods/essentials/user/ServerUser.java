package io.github.ultreon.mods.essentials.user;

import com.google.common.annotations.Beta;
import com.mojang.authlib.GameProfile;
import com.ultreon.mods.lib.network.api.packet.BasePacket;
import com.ultreon.mods.lib.network.api.packet.ClientEndpoint;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.SetUserFlagPacket;
import io.github.ultreon.mods.essentials.network.economy.SetBalancePacket;
import io.github.ultreon.mods.essentials.network.screen.ErrorScreenPacket;
import io.github.ultreon.mods.essentials.shop.ServerShop;
import io.github.ultreon.mods.essentials.shop.ShopItem;
import io.github.ultreon.mods.essentials.teleport.ServerTeleportManager;
import io.github.ultreon.mods.essentials.util.BaseLocation;
import io.github.ultreon.mods.essentials.util.LocationImpl;
import io.github.ultreon.mods.essentials.util.homes.ServerHome;
import io.github.ultreon.mods.essentials.util.homes.ServerHomes;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static io.github.ultreon.mods.essentials.util.MoneyUtils.SIGN;

@SuppressWarnings("FieldCanBeLocal")
public final class ServerUser extends User {
    private static final Map<UUID, ServerUser> USER_CACHE = new ConcurrentHashMap<>();
    private final File dataDir;
    @Getter
    private final File mainFile;
    private final File emailsDir;
    private boolean permsInitialized = false;

    private double prevBalance = -1;
    @Getter
    private double balance;

    @Getter
    private ServerHomes homes;
    private final Set<Permission> permissions = new HashSet<>();
    private final Set<ServerRole> roles = new HashSet<>();
    private final Object balanceLock = new Object();
    @Getter
    private boolean moderator;
    @Getter
    private boolean admin;

    private ServerUser(UUID id) {
        super(id);

        // Initialize
        this.homes = new ServerHomes(this);

        // Create user data directory.
        File userdata = UEssentials.getDataFile("userdata");
        if (!userdata.exists()) {
            if (!userdata.mkdirs()) {
                throw new IOError(new IOException("Couldn't create directory: " + userdata));
            }
        }

        // Read user data.
        this.dataDir = new File(userdata, String.valueOf(id));
        this.mainFile = new File(dataDir, "main.dat");
        this.emailsDir = new File(dataDir, "emails");

        makeDirsIfAbsent(dataDir);
        makeDirsIfAbsent(emailsDir);

        if (this.mainFile.exists()) {
            try {
                load(NbtIo.readCompressed(mainFile));
            } catch (Throwable t) {
                UEssentials.LOGGER.error("Failed to load server user:", t);
            }
        }

        Set<Permission> perms = Permissions.getPerms();
        if (!this.permsInitialized) {
            for (Permission permission : perms) {
                if (permission.def()) {
                    this.permissions.add(permission);
                }
            }
        }
    }

    public void tick(Player ticker) {
        if (ticker instanceof ServerPlayer player) {
            if (!player.connection.connection.isEncrypted() && !UEssentials.isDevMode()) {
                disconnect("Access Denied\nConnection isn't encrypted");
                return;
            }
            if (player.connection.connection.isConnected()) {
                if (balance != prevBalance) {
                    Networking.get().sendToClient(new SetBalancePacket(balance), player);
                    prevBalance = balance;
                }
            }
        }
    }

    private <T extends BasePacket<T> & ClientEndpoint> void send(BasePacket<T> packet) {
        Networking.get().sendToClient(packet, player());
    }

    public static Collection<ServerUser> online() {
        return USER_CACHE.values();
    }

    public static void close() {
        USER_CACHE.clear();
    }

    private void makeDirsIfAbsent(File file) {
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("Creating directories failed for path: " + file);
        }
    }

    @Override
    public GameProfile getProfile() {
        return player().getGameProfile();
    }

    public static ServerUser get(String username) {
        return get(UEssentials.player(username));
    }

    public static ServerUser get(UUID uuid) {
        return fromCacheOr(uuid, () -> new ServerUser(uuid));
    }

    public static ServerUser get(ServerPlayer player) {
        return fromCacheOr(player.getUUID(), () -> new ServerUser(player.getUUID()));
    }

    public static void saveAll() {
        for (ServerUser user : USER_CACHE.values()) {
            user.save();
        }
    }

    private static ServerUser fromCacheOr(UUID uuid, Supplier<ServerUser> user) {
        return USER_CACHE.computeIfAbsent(uuid, uuid1 -> user.get());
    }

    public ServerPlayer player() {
        MinecraftServer server = getServer();
        return server.getPlayerList().getPlayer(id);
    }

    public Player basePlayer() {
        return player();
    }

    @Override
    public boolean hasPermission(Permission perm) {
        if (permissions.stream().anyMatch(permission -> permission.isChildOf(Permissions.MASTER) || permission.equals(Permissions.MASTER)) ||
            permissions.stream().anyMatch(permission -> permission.isChildOf(perm) || permission.equals(perm))) {
            return true;
        }

        for (ServerRole role : roles) {
            if (role.hasPermission(perm)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasExactPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public boolean hasOwnPermission(Permission perm) {
        return permissions.stream().anyMatch(permission -> permission.isChildOf(Permissions.MASTER) || permission.equals(Permissions.MASTER)) ||
               permissions.stream().anyMatch(permission -> permission.isChildOf(perm) || permission.equals(perm));

    }

    public boolean hasRolePermission(Permission perm) {
        for (ServerRole role : roles) {
            if (role.hasPermission(perm)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addPermission(Permission perm) {
        permissions.add(perm);
    }

    @Override
    public void removePermission(Permission perm) {
        permissions.removeIf(permission -> permission.equals(perm));
    }

    @Override
    protected boolean isRoot() {
//        Account loggedInUser = AccountManager.get().getLoggedInUser(id);
//        return loggedInUser != null && loggedInUser.isRoot();
        return player().getName().getString().equals("Qboi_") || isDeveloperAccount();
    }

    private boolean isDeveloperAccount() {
        return player().getName().getString().equals("Dev") && UEssentials.isDevMode();
    }

//    @Override
//    protected boolean isAdmin() {
//        Account loggedInUser = AccountManager.get().getLoggedInUser(id);
//        return loggedInUser != null && loggedInUser.isAdmin();
//    }

    public void addRole(ServerRole role) {
        this.roles.add(role);
    }

    public void removeRole(ServerRole role) {
        this.roles.remove(role);
    }

    private void load(CompoundTag tag) {
        homes = ServerHomes.from(this, tag.getList("Homes", Tag.TAG_COMPOUND));

        CompoundTag flags = tag.getCompound("Flags");
        moderator = flags.getBoolean("moderator");

        ListTag permissions = tag.getList("Permissions", Tag.TAG_STRING);
        this.permissions.clear();
        for (Tag permTag : permissions) {
            if (permTag instanceof StringTag) {
                String string = permTag.getAsString();
                this.permissions.add(new Permission(string));
            }
        }

        this.balance = tag.getFloat("balance");
        this.permsInitialized = tag.getBoolean("permsInitialized");
    }

    private CompoundTag save(CompoundTag tag) {
        tag.put("Homes", homes.to(new ListTag()));

        CompoundTag flags = new CompoundTag();
        flags.putBoolean("moderator", moderator);
        tag.put("Flags", flags);

        ListTag perms = new ListTag();
        for (Permission perm : this.permissions) {
            perms.add(StringTag.valueOf(perm.id()));
        }

        tag.put("Permissions", perms);
        tag.putDouble("balance", balance);
        tag.putBoolean("permsInitialized", permsInitialized);

        return tag;
    }

    @SneakyThrows
    public void save() {
        CompoundTag tag = save(new CompoundTag());

        NbtIo.writeCompressed(tag, mainFile);
    }

    public void setBalance(double balance) {
        this.balance = balance;
        Networking.get().sendToClient(new SetBalancePacket(balance), player());
    }

    public void reduceMoney(double amount) {
        this.balance -= amount;
        Networking.get().sendToClient(new SetBalancePacket(balance), player());
    }

    public void addMoney(double amount) {
        this.balance += amount;
        Networking.get().sendToClient(new SetBalancePacket(balance), player());
    }

    public Set<Permission> getEnabledPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    private MinecraftServer getServer() {
        return UEssentials.server();
    }

    public boolean isOnline() {
        return UEssentials.server().getPlayerList().getPlayer(id) != null;
    }

    public boolean isOffline() {
        return UEssentials.server().getPlayerList().getPlayer(id) == null;
    }

    public @Nullable ServerHome getHome(String name) {
        return homes.get(name);
    }

    public ServerTeleportManager getTeleportManager() {
        return ServerTeleportManager.get(id);
    }

    public void requestTeleportTo(ServerPlayer player) {
        getTeleportManager().requestTeleportTo(player.getUUID());
    }

    public void requestTeleportTo(UUID playerUUID) {
        getTeleportManager().requestTeleportTo(playerUUID);
    }

    public void requestTeleportFrom(ServerPlayer player) {
        getTeleportManager().requestTeleportFrom(player.getUUID());
    }

    public void requestTeleportFrom(UUID playerUUID) {
        getTeleportManager().requestTeleportFrom(playerUUID);
    }

    public void ban(@Nullable String reason) {
        ban(reason == null ? null : Component.literal(reason));
    }

    @SneakyThrows
    public void ban(@Nullable Component reason) {
        UserBanList banList = UEssentials.server().getPlayerList().getBans();
        GameProfile profile = getProfile();
        ServerPlayer sender = player();
        if (!banList.isBanned(profile)) {
            UserBanListEntry entry = new UserBanListEntry(profile, null, profile.getName(), null, reason == null ? null : reason.getString());
            banList.add(entry);

            if (isOnline()) {
                showError("Banned", reason == null ? "The ban hammer has spoken" : reason.getString());

                String message = "" + ChatFormatting.RED + ChatFormatting.BOLD + "You are banned from the server!";
                if (reason != null) {
                    message += "\n" + ChatFormatting.RESET + reason.getString();
                }
                banList.save();
            }
        }
    }

    public void kick(@Nullable String reason) {
        kick(reason == null ? null : Component.literal(reason));
    }

    public void kick(@Nullable Component reason) {
        // Todo: add here a feedback message in gui form.

        ServerPlayer player = player();
        if (player != null) {
            String message = "" + ChatFormatting.RED + ChatFormatting.BOLD + "You have been kicked out of the server!";
            showError("Kicked", reason == null ? "You have been kicked out of the server!" : reason.getString());

            if (reason != null) {
                message += "\n" + ChatFormatting.RESET + reason.getString();
            }
        }
    }

    @Override
    public void showError(String title, String description) {
        Networking.get().sendToClient(new ErrorScreenPacket(title, description), player());
    }

    @Override
    public void showError(Component title, Component description) {
        Networking.get().sendToClient(new ErrorScreenPacket(title, description), player());
    }

    public void fullFeed() {
        player().getFoodData().eat(20, 40f);
    }

    public void fullHeal() {
        player().setHealth(20f);
    }

    public void op() {
        if (player().hasPermissions(3)) {
            PlayerList playerList = UEssentials.server().getPlayerList();
            GameProfile profile = getProfile();
            CommandSourceStack cmdSource = player().createCommandSourceStack();

            if (!playerList.isOp(profile)) {
                playerList.op(profile);
                cmdSource.sendSuccess(() -> Component.translatable("commands.op.success", profile.getName()), true);
            }
        }
    }

    public void deop() {
        if (player().hasPermissions(4)) {
            PlayerList playerList = UEssentials.server().getPlayerList();
            GameProfile profile = getProfile();
            CommandSourceStack cmdSource = player().createCommandSourceStack();

            if (playerList.isOp(profile)) {
                playerList.deop(profile);
                cmdSource.sendSuccess(() -> Component.translatable("commands.deop.success", profile.getName()), true);
            }
        }
    }

    public void teleport(ServerLevel newLevel, double x, double y, double z, float xRot, float yRot) {
        ServerPlayer player = player();
        player.teleportTo(newLevel, x, y, z, xRot, yRot);
    }

    public void teleport(ServerUser destination) {
        player().teleportTo(destination.getLevel(), destination.getX(), destination.getY(), destination.getZ(), destination.player().getYRot(), destination.player().getXRot());
    }

    private double getX() {
        return player().getX();
    }

    private double getY() {
        return player().getY();
    }

    private double getZ() {
        return player().getZ();
    }

    public ServerLevel getLevel() {
        return player().serverLevel();
    }

    public int buy(UUID shopItemId) {
        ShopItem shopItem = ServerShop.get().getItem(shopItemId);

        synchronized (balanceLock) {
            if (shopItem.canBuyWith(getBalance())) {
                ItemStack item = shopItem.getItem();
                reduceMoney(shopItem.getPrice());
                ServerPlayer player = player();
                ItemStack singleItem = item.copy();
                singleItem.setCount(1);
                int count = item.getCount();
                while (count > 0) {
                    if (!player.addItem(singleItem.copy())) {
                        break;
                    }
                    count--;
                }
                if (count > 0) {
                    ItemStack toDrop = item.copy();
                    toDrop.setCount(count);
                    player.drop(toDrop, false);
                }
                return 0x00;
            } else {
                return 0x02;
            }
        }
    }

    public boolean isFlying() {
        return player().getAbilities().flying;
    }

    public void setFlying(boolean value) {
        player().getAbilities().flying = value;
        player().onUpdateAbilities();
    }

    public boolean hasFlyMode() {
        return player().getAbilities().mayfly;
    }

    public void setFlyMode(boolean value) {
        player().getAbilities().mayfly = value;
        player().onUpdateAbilities();
    }

    public boolean hasGodMode() {
        return player().getAbilities().invulnerable;
    }

    public void setGodMode(boolean value) {
        player().getAbilities().invulnerable = value;
        player().onUpdateAbilities();
    }

    public boolean isBuildingAllowed() {
        return player().getAbilities().mayBuild;
    }

    public void setBuildingAllowed(boolean value) {
        player().getAbilities().mayBuild = value;
        player().onUpdateAbilities();
    }

    public boolean hasInstaBuild() {
        return player().getAbilities().instabuild;
    }

    public void setInstaBuild(boolean value) {
        player().getAbilities().instabuild = value;
        player().onUpdateAbilities();
    }

    @Beta
    private boolean hasPlaceForItem(ItemStack item) {
        ServerPlayer player = player();

        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stackInSlot = inv.getItem(i);
            CompoundTag tag = stackInSlot.getTag();
            CompoundTag tag1 = item.getTag();

//            stackInSlot.isItemEqual();
        }

        return false;
    }

    private void giveItem(ItemStack item) {
        player().addItem(item);
    }

    public boolean couldGetRemoteUserPermissions() {
        return hasPermission(Permissions.CHANGE_USER_PERMISSIONS);
    }

    public boolean couldGetRolePermissions() {
        return hasPermission(Permissions.CHANGE_ROLE_PERMISSIONS);
    }

    public Vec3 getPosition() {
        return player().getPosition(0f);
    }

    public Vec2 getRotation() {
        return player().getRotationVector();
    }

    public void burn(int seconds) {
        player().setSecondsOnFire(seconds);
    }

    public void setModerator(boolean moderator) {
        this.moderator = moderator;
        Networking.get().sendToClient(new SetUserFlagPacket(UserFlags.MODERATOR, moderator), player());
    }

    public void makeModerator() {
        setModerator(true);
    }

    public void revokeModerator() {
        setModerator(false);
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        Networking.get().sendToClient(new SetUserFlagPacket(UserFlags.ADMIN, admin), player());
    }

    public void makeAdmin() {
        setAdmin(true);
    }

    public void revokeAdmin() {
        setAdmin(false);
    }

    public void disconnect(@Nullable String reason) {
        disconnect(reason == null ? null : Component.literal(reason));
    }

    public void disconnect(@Nullable Component reason) {
        ServerPlayer player = player();
        if (player != null) {
            Component message = Component.literal("You got randomly disconnected.\nYou probably did something wrong.\nAlthough, we don't know why.");
            if (reason != null) {
                message = reason;
            }
        }
    }

    public void handleIllegalPacket() {
        disconnect("You sent an illegal packet.");
    }

    public void instaKill() {
        player().kill();
    }

    public BaseLocation getLocation() {
        return new LocationImpl(getLevel(), getPosition(), getRotation());
    }

    public void exchangeExp(int money) {
        int amount = money * 10;
        if (getTotalExpPoints(basePlayer()) >= amount) {
            this.player().giveExperiencePoints(-amount);
            addMoney(money);
            this.sendMessage(Component.translatable("message.ultimate_essentials.exchangedExp", SIGN, money));
        }
    }

    public boolean canExchangeExp(int exp) {
        return player().experienceLevel >= exp;
    }

    public void sendMessage(Component message) {
        player().sendSystemMessage(message);
    }

    public void sendChat(Component message) {
        player().sendSystemMessage(message);
    }
}

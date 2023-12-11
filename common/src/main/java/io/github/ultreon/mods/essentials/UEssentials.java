package io.github.ultreon.mods.essentials;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.ultreon.mods.lib.util.ServerLifecycle;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.init.ModSounds;
import io.github.ultreon.mods.essentials.security.AccessManager;
import io.github.ultreon.mods.essentials.server.ServerEvents;
import io.github.ultreon.mods.essentials.server.commands.MasterCommand;
import io.github.ultreon.mods.essentials.server.commands.UEssentialsCommand;
import io.github.ultreon.mods.essentials.shop.ServerShop;
import io.github.ultreon.mods.essentials.shop.ShopLoader;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.user.User;
import io.github.ultreon.mods.essentials.util.UniqueObject;
import io.github.ultreon.mods.essentials.util.warps.ServerWarps;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author XyperCode
 */
public class UEssentials {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "ultimate_essentials";

    // Mod Data
    public static final String NBT_NAME = UEssentialsMain.NBT_NAME;
    public static final String MOD_NAME;
    public static final String MOD_VERSION;
    public static final String MOD_DESCRIPTION;

    @Getter
    private static final boolean clientSide;
    @Getter
    private static final boolean serverSide;
    private static final TriState DEV_OVERRIDE = TriState.DEFAULT;

    @Getter
    private static MinecraftServer server;
    @Getter
    private static ServerWarps warps = new ServerWarps();
    private static File dataFile;
    @Getter
    private static UUID owner;

    @Getter
    private static String inviteCode;


    @SuppressWarnings("ConstantConditions")
    private static final Supplier<Boolean> getClientSide = () -> {
        try {
            return Minecraft.getInstance() != null; // This is null when running runData.
        } catch (Throwable t) {
            return false;
        }
    };
    private static UEssentials instance;

    static {
        Mod info = Platform.getMod(MOD_ID);
        MOD_VERSION = info.getVersion();
        MOD_DESCRIPTION = info.getDescription();
        MOD_NAME = info.getName();

        clientSide = getClientSide.get();

        boolean s;
        try {
            Class.forName("net.minecraft.server.MinecraftServer");
            s = true;
        } catch (ClassNotFoundException e) {
            s = false;
        }
        serverSide = s;
    }

    /**
     * The UEssentials class is responsible for booting the mod and initializing various components such as permissions and
     * commands.
     */
    public UEssentials() {
        // Pre-boot.
        LOGGER.info("Booting " + MOD_NAME + " (" + NBT_NAME + ")");

        UEssentials.instance = this;

        // Initialize permissions class.
        Permissions.initClass();

        // Registration
        ModSounds.register();

        CommandRegistrationEvent.EVENT.register(UEssentials::onCommandRegister);
        LifecycleEvent.SERVER_STARTING.register(this::serverStarting);
        LifecycleEvent.SERVER_STARTED.register(this::serverStarted);
        LifecycleEvent.SERVER_STOPPED.register(this::serverStopped);

        TickEvent.PLAYER_PRE.register(ServerEvents::onPlayerTick);
        BlockEvent.BREAK.register(ServerEvents::onBlockBreak);

        EnvExecutor.runInEnv(Env.CLIENT, () -> UEssentialsClient::new);
    }

    private static void load(CompoundTag tag) {
        warps = ServerWarps.from(tag.getList("Warps", Tag.TAG_COMPOUND));
        AccessManager.get().load(tag.getList("Access", Tag.TAG_INT_ARRAY));
    }

    public static boolean isDevMode() {
        if (DEV_OVERRIDE != TriState.DEFAULT)
            return DEV_OVERRIDE.value();

        return Platform.isDevelopmentEnvironment();
    }

    public static UEssentials get() {
        return instance;
    }

    private CompoundTag save(CompoundTag tag) {
        tag.put("Warps", warps.to(new ListTag()));

        ListTag access = AccessManager.get().save();
        tag.put("Access", access);

        return tag;
    }

    /**
     * Saves the server data.
     */
    @SneakyThrows
    public void save() {
        CompoundTag tag = save(new CompoundTag());

        NbtIo.writeCompressed(tag, dataFile);
    }

    public void serverStarting(MinecraftServer server) {
        UEssentials.server = server;

        if (getDataFolder().exists() && !getDataFolder().isDirectory())
            throw new RuntimeException("Data folder exists, but isn't a directory.");
        if (!getDataFolder().exists() && !getDataFolder().mkdirs())
            throw new RuntimeException("Data folder couldn't be created.");

        if (getDataFolder().exists()) {
            ServerShop.load();
        }

        ShopLoader.load(ServerShop.get());

        dataFile = getDataFile("server.dat");

        // Read user data.
        if (dataFile.exists()) {
            try {
                load(NbtIo.readCompressed(dataFile));
            } catch (Throwable t) {
                LOGGER.error("Error occurred when loading server data:", t);
            }
        }
    }

    public void serverStarted(MinecraftServer event) {
        byte[] bytes = new byte[8];
        new SecureRandom().nextBytes(bytes);
        String code = Base64.getEncoder().encodeToString(bytes);
        UEssentials.inviteCode = code.substring(0, code.length() - 1);

        LOGGER.info("Invite code: " + inviteCode);
    }

    public void serverStopped(MinecraftServer server) {
        if (getDataFolder().exists()) {
            ServerShop.unload();
        }

        UEssentials.server = null;
        UEssentials.dataFile = null;
    }
    /**
     * Registers various commands with the given command dispatcher.
     *
     * @param dispatcher the command dispatcher to register commands with
     * @param context the command build context
     * @param selection the command selection
     */
    private static void onCommandRegister(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        MasterCommand.register(dispatcher);
        UEssentialsCommand.register(dispatcher);
        UEssentialsCommand.registerExp(dispatcher);
    }

    public static boolean isOwner(Entity entity) {
        if (entity instanceof Player) {
            return isOwner((Player) entity);
        }
        return false;
    }

    public static boolean isOwner(Player player) {
        return isOwner(player.getUUID());
    }

    private static boolean isOwner(UUID user) {
        return owner == user || isServerGod(user);
    }

    public static boolean isServerGod(@NotNull UUID uuid) {
        return EnvExecutor.getEnvSpecific(() -> () -> {
            if (Minecraft.getInstance().player != null) {
                return Minecraft.getInstance().player.getUUID() == uuid;
            } else return true;
        }, () -> () -> {
            GameProfile profile = ServerLifecycle.getCurrentServer().getSingleplayerProfile();
            if (profile != null) {
                return Objects.equals(profile.getId(), uuid);
            }
            return false;
        });
    }

    public static void setOwner(ServerPlayer player) {
        setOwner(player.getUUID());
    }

    public static void setOwner(UniqueObject uniqueId) {
        setOwner(uniqueId.uuid());
    }

    public static void setOwner(UUID uniqueId) {
        UEssentials.owner = uniqueId;
    }

    public static boolean hasOwner() {
        return owner != null;
    }

    public static ServerUser getOwnerAsUser() {
        return ServerUser.get(owner);
    }

    public static ServerPlayer player(String username) {
        return server().getPlayerList().getPlayerByName(username);
    }

    public static ServerPlayer player(UUID uuid) {
        return server().getPlayerList().getPlayer(uuid);
    }

    public static ServerPlayer player(User user) {
        return server().getPlayerList().getPlayer(user.uuid());
    }

    public static ServerPlayer player(UniqueObject uniqueObject) {
        return server().getPlayerList().getPlayer(uniqueObject.uuid());
    }

    public static MinecraftServer server() {
        return server;
    }

    public static Iterable<? extends ServerPlayer> onlinePlayers() {
        return server().getPlayerList().getPlayers();
    }

    public static long getServerUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    public static Path getDataPath(String path) {
        return Paths.get(getDataPath().toString(), path);
    }

    public static File getDataFile(String path) {
        return Paths.get(getDataPath().toString(), path).toFile();
    }

    public static Path getDataPath() {
        Env dist = Platform.getEnvironment();
        if (dist == null)
            throw new NullPointerException("FMLEnvironment.dist is null, so this mc instance is neither the client or the server.");

        return switch (dist) {
            case SERVER -> Paths.get(System.getProperty("user.dir"), "uessentials-data");
            case CLIENT -> getLevelDir();
        };
    }

    public static File getDataFolder() {
        return getDataPath().toFile();
    }

    @NotNull
    private static Path getLevelDir() {
        return server().getWorldPath(new LevelResource("uessentials-data"));
    }

    /**
     * Get the mod's resource location by only a path.
     *
     * @param path the path for the resource location of the mod.
     * @return the expected resource location.
     */
    @SuppressWarnings("unused")
    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}

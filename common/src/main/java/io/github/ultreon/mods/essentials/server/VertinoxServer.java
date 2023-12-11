package io.github.ultreon.mods.essentials.server;

//import com.mojang.brigadier.CommandDispatcher;
//import io.github.ultreon.mods.essentials.UEssentialsMain;
//import io.github.ultreon.mods.essentials.easy.Server;
//import io.github.ultreon.mods.essentials.security.AccessManager;
//import io.github.ultreon.mods.essentials.shop.ServerShop;
//import io.github.ultreon.mods.essentials.shop.ShopLoader;
//import io.github.ultreon.mods.essentials.user.ServerUser;
//import io.github.ultreon.mods.essentials.user.User;
//import io.github.ultreon.mods.essentials.util.UniqueObject;
//import io.github.ultreon.mods.essentials.util.commands.*;
//import io.github.ultreon.mods.essentials.util.warps.ServerWarps;
//import lombok.Getter;
//import lombok.SneakyThrows;
//import net.minecraft.client.Minecraft;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.NbtIo;
//import net.minecraft.nbt.Tag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.storage.LevelResource;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.RegisterCommandsEvent;
//import net.minecraftforge.event.server.ServerStartedEvent;
//import net.minecraftforge.event.server.ServerStartingEvent;
//import net.minecraftforge.event.server.ServerStoppedEvent;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.DistExecutor;
//import net.minecraftforge.fml.IExtensionPoint;
//import net.minecraftforge.fml.ModList;
//import net.minecraftforge.fml.ModLoadingContext;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import net.minecraftforge.fml.loading.FMLEnvironment;
//import net.minecraftforge.forgespi.language.IModInfo;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.io.File;
//import java.lang.management.ManagementFactory;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.SecureRandom;
//import java.util.Base64;
//import java.util.Objects;
//import java.util.UUID;
//
//public class VertinoxServer {
//    // Directly reference a log4j logger.
//    public static final Logger LOGGER = LogManager.getLogger();
//
//    // Mod Data
//    public static final String NBT_NAME = UEssentialsMain.NBT_NAME;
//    public static final String MOD_ID = "uessentials_server";
//    public static final String MOD_NAME;
//    public static final String MOD_VERSION;
//    public static final String MOD_DESCRIPTION;
//    public static final String MOD_NAMESPACE;
//    @Nullable
//    private static final Boolean MOD_DEV_OVERRIDE = null;
//    private static UEssentials instance;
//
//    static {
//        IModInfo info = ModList.get().getMods().stream().filter(modInfo -> Objects.equals(modInfo.getModId(), MOD_ID)).findFirst().orElseThrow(IllegalStateException::new);
//        MOD_VERSION = info.getVersion().toString();
//        MOD_DESCRIPTION = info.getDescription();
//        MOD_NAMESPACE = info.getNamespace();
//        MOD_NAME = info.getDisplayName();
//    }
//
//    public static final String MOD_DATA_DIR = "uessentials-data";
//
//    private static MinecraftServer server;
//    private static ServerWarps warps = new ServerWarps();
//    private static File dataFile;
//
//    public static String INVITE_CODE;
//
//    @Getter
//    private static final boolean clientSide = false;
//    @Getter
//    private static final boolean serverSide = true;
//    private static UUID owner;
//
//    @SuppressWarnings("unused")
//    public VertinoxServer() {
//        instance = this;
//
//        LOGGER.info("Booting " + MOD_NAME + " (" + NBT_NAME + ")");
//
//        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//
//        // Register ourselves for server and other game events we are interested in
//        MinecraftForge.EVENT_BUS.register(this);
//
//        // Register ourselves for server and other game events we are interested in
//        this.setExtensionPoint();
//    }
//
//    public static UEssentials get() {
//        return instance;
//    }
//
//    @SuppressWarnings("ConstantConditions")
//    public static boolean isModDev() {
//        if (MOD_DEV_OVERRIDE != null) {
//            return MOD_DEV_OVERRIDE;
//        }
//        return !FMLEnvironment.production;
//    }
//
//    private void setExtensionPoint() {
//        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "serverutils", (remote, isServer) -> isServer));
//    }
//
//    @SubscribeEvent
//    public static void onCommandRegister(RegisterCommandsEvent event) {
//        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
//        HomesCommand.register(dispatcher);
//        WarpsCommand.register(dispatcher);
//        TeleportCommands.registerTpAsk(dispatcher);
//        TeleportCommands.registerTpAccept(dispatcher);
//        TeleportCommands.registerTpDeny(dispatcher);
//        WeatherCommand.register(dispatcher);
//        TimeCommand.register(dispatcher);
//        IRLTimeCommand.register(dispatcher);
//        FlyCommand.register(dispatcher);
//        GodCommand.register(dispatcher);
////        BuildingAllowedCommand.register(dispatcher);
////        InstaBuildCommand.register(dispatcher);
//        SpeedCommand.register(dispatcher);
//        BurnCommand.register(dispatcher);
//        HealCommand.register(dispatcher);
//        FeedCommand.register(dispatcher);
//        ItemCommands.register(dispatcher);
//        InfoCommands.register(dispatcher);
//    }
//
//    public static Server getEasyServer() {
//        return new Server(server());
//    }
//
//    public static ResourceLocation res(String path) {
//        return new ResourceLocation(MOD_ID, path);
//    }
//
//    public static MinecraftServer server() {
//        return server;
//    }
//
//    public static Path getDataPath(String path) {
//        return Paths.get(getDataPath().toString(), path);
//    }
//
//    public static File getDataFile(String path) {
//        return Paths.get(getDataPath().toString(), path).toFile();
//    }
//
//    public static Path getDataPath() {
//        Dist dist = FMLEnvironment.dist;
//        if (dist == null)
//            throw new NullPointerException("FMLEnvironment.dist is null, so this mc instance is neither the client or the server.");
//
//        return switch (dist) {
//            case DEDICATED_SERVER -> Paths.get(System.getProperty("user.dir"), "uessentials-data");
//            case CLIENT -> getLevelDir();
//        };
//    }
//
//    public static File getDataFolder() {
//        return getDataPath().toFile();
//    }
//
//    @NotNull
//    private static Path getLevelDir() {
//        return server().getWorldPath(new LevelResource("uessentials-data"));
//    }
//
//    @Deprecated
//    public static Path getDataFolderPath() {
//        return Paths.get(".", "uessentials-data");
//    }
//
//    @Deprecated
//    public static Path getDataFilePath(String subPath) {
//        return Paths.get(getDataFolder().getAbsolutePath(), subPath);
//    }
//
//    public static boolean isOwner(Entity entity) {
//        if (entity instanceof Player) {
//            return isOwner((Player) entity);
//        }
//        return false;
//    }
//
//    public static boolean isOwner(Player player) {
//        return isOwner(player.getUUID());
//    }
//
//    private static boolean isOwner(UUID user) {
//        return owner == user || isServerGod(user);
//    }
//
//    public static boolean isServerGod(UUID uuid) {
//        return DistExecutor.unsafeRunForDist(() -> () -> {
//            if (Minecraft.getInstance().player != null) {
////                LOGGER.warn(Minecraft.getInstance().player.getUUID());
////                LOGGER.warn(uuid);
//                return Minecraft.getInstance().player.getUUID() == uuid;
//            } else return true;
//        }, () -> () -> false);
//    }
//
//    public static void setOwner(ServerPlayer player) {
//        setOwner(player.getUUID());
//    }
//
//    public static void setOwner(UniqueObject uniqueId) {
//        setOwner(uniqueId.uuid());
//    }
//
//    public static void setOwner(UUID uniqueId) {
//        UEssentials.owner = uniqueId;
//    }
//
//    public static boolean hasOwner() {
//        return owner != null;
//    }
//
//    public static ServerUser getOwnerAsUser() {
//        return ServerUser.get(owner);
//    }
//
//    public static UUID getOwner() {
//        return owner;
//    }
//
//    public static ServerPlayer player(String username) {
//        return server().getPlayerList().getPlayerByName(username);
//    }
//
//    public static ServerPlayer player(UUID uuid) {
//        return server().getPlayerList().getPlayer(uuid);
//    }
//
//    public static ServerPlayer player(User user) {
//        return server().getPlayerList().getPlayer(user.uuid());
//    }
//
//    public static ServerPlayer player(UniqueObject uniqueObject) {
//        return server().getPlayerList().getPlayer(uniqueObject.uuid());
//    }
//
//    public static Iterable<? extends ServerPlayer> onlinePlayers() {
//        return server().getPlayerList().getPlayers();
//    }
//
//    public static long getServerUptime() {
//        return ManagementFactory.getRuntimeMXBean().getUptime();
//    }
//
//    /**
//     * Server starting handler.
//     * The server field will contain the server after the server starting event.
//     *
//     * @param event the fml server starting event.
//     */
//    @SneakyThrows
//    @SubscribeEvent
//    public void serverStarting(ServerStartingEvent event) {
//        LOGGER.info("Server is starting, the mod feels it's previous variant...");
//
//        server = event.getServer();
//
//        if (getDataFolder().exists() && !getDataFolder().isDirectory())
//            throw new RuntimeException("Data folder exists, but isn't a directory.");
//        if (!getDataFolder().exists() && !getDataFolder().mkdirs())
//            throw new RuntimeException("Data folder couldn't be created.");
//
//        if (getDataFolder().exists()) {
//            ServerShop.load();
//        }
//
//        ShopLoader.load(ServerShop.get());
//
//        dataFile = getDataFile("server.dat");
//
//        // Read user data.
//        if (dataFile.exists()) {
//            try {
//                load(NbtIo.readCompressed(dataFile));
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//        }
//    }
//
//    private static void load(CompoundTag tag) {
//        warps = ServerWarps.from(tag.getList("Warps", Tag.TAG_COMPOUND));
//        AccessManager.get().load(tag.getList("Access", Tag.TAG_INT_ARRAY));
//    }
//
//    private CompoundTag save(CompoundTag tag) {
//        tag.put("Warps", warps.to(new ListTag()));
//
//        ListTag access = AccessManager.get().save();
//        tag.put("Access", access);
//
//        return tag;
//    }
//
//    /**
//     * Saves the server data.
//     */
//    @SneakyThrows
//    public void save() {
//        CompoundTag tag = save(new CompoundTag());
//
//        NbtIo.writeCompressed(tag, dataFile);
//    }
//
//    @SubscribeEvent
//    public void serverStarted(ServerStartedEvent event) {
//        byte[] bytes = new byte[8];
//        new SecureRandom().nextBytes(bytes);
//        String code = Base64.getEncoder().encodeToString(bytes);
//        INVITE_CODE = code.substring(0, code.length() - 1);
//
//        LOGGER.info("Invite code: " + INVITE_CODE);
//    }
//
//    public static ServerWarps getWarps() {
//        return warps;
//    }
//
//    /**
//     * Server starting handler.
//     * The server field will be nullified after the server stopped event.
//     *
//     * @param event the fml server stopped event.
//     */
//    @SubscribeEvent
//    public void serverStopped(ServerStoppedEvent event) {
//        LOGGER.info("Server has been stopped, the feeling are fading away...");
//
//        server = null;
//        dataFile = null;
//    }
//
//    public void register(ModLoadingContext context) {
//
//    }
//}

package io.github.ultreon.mods.essentials.server.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.permission.SetUserPermissionPacket;
import io.github.ultreon.mods.essentials.security.AccessManager;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import io.github.ultreon.mods.essentials.user.User;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

import java.util.Collection;
import java.util.UUID;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public final class UEssentialsCommand {
    @SuppressWarnings("CodeBlock2Expr")
    public static void registerExp(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> experienceCommand = dispatcher.register(Commands
            .literal("experience")
            .then(Commands
                .literal("query")
                .then(Commands
                    .argument("targets", EntityArgument.player())
                    .then(Commands
                        .literal("total").executes((source) -> {
                            return queryExperience(source.getSource(), EntityArgument.getPlayer(source, "targets"), Type.TOTAL_POINTS);
                        })
                    )
                )
            )
        );
        LiteralCommandNode<CommandSourceStack> xpCommand = dispatcher.register(Commands
            .literal("xp")
            .then(Commands
                .literal("query")
                .then(Commands
                    .argument("targets", EntityArgument.player())
                    .then(Commands
                        .literal("total").executes((source) -> {
                            return queryExperience(source.getSource(), EntityArgument.getPlayer(source, "targets"), Type.TOTAL_POINTS);
                        })
                    )
                )
            )
        );
//        dispatcher.register(Commands.literal("xp").requires((source) -> {
//            return source.hasPermission(2);
//        }).redirect(experienceCommand));
    }

    private static int queryExperience(CommandSourceStack p_137313_, ServerPlayer p_137314_, Type p_137315_) {
        int i = p_137315_.query.applyAsInt(p_137314_);
        p_137313_.sendSuccess(() -> Component.literal("Total experience for ").append(p_137314_.getDisplayName()).append(" is ").append(Component.literal(Integer.toString(i))), false);
        return i;
    }

    enum Type {
        TOTAL_POINTS("total", (player) -> {
            return Mth.floor(User.getTotalExpPoints(player));
        });

        public final String name;
        final ToIntFunction<ServerPlayer> query;

        Type(String p_137353_, ToIntFunction<ServerPlayer> p_137356_) {
            this.name = p_137353_;
            this.query = p_137356_;
        }
    }
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("uessentials")
                .then(Commands.literal("moderators")
                        .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                        .then(Commands.literal("add")
                                .then(Commands.argument("target", EntityArgument.player())
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                                        .executes((context) -> {
                                            CommandSourceStack source = context.getSource();
                                            ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                            ServerUser user = ServerUser.get(player);
                                            user.makeModerator();

                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("target", EntityArgument.player())
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                                        .executes((context) -> {
                                            CommandSourceStack source = context.getSource();
                                            ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                            ServerUser user = ServerUser.get(player);
                                            user.revokeModerator();

                                            return 0;
                                        })
                                )
                        )
                )
                .then(Commands.literal("admins")
                        .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                        .then(Commands.literal("add")
                                .then(Commands.argument("target", EntityArgument.player())
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                                        .executes((context) -> {
                                            CommandSourceStack source = context.getSource();
                                            ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                            ServerUser user = ServerUser.get(player);
                                            user.makeAdmin();

                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("target", EntityArgument.player())
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                                        .executes((context) -> {
                                            CommandSourceStack source = context.getSource();
                                            ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                            ServerUser user = ServerUser.get(player);
                                            user.revokeAdmin();

                                            return 0;
                                        })
                                )
                        )
                )
                .then(Commands.literal("server")
                        .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                        .then(Commands.literal("master")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.literal("add")
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                                                .executes((context) -> {
                                                    CommandSourceStack source = context.getSource();
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                                    UUID id = player.getUUID();
                                                    ServerUser user = ServerUser.get(id);
                                                    user.addPermission(Permissions.MASTER);
                                                    Networking.get().sendToAllClients(new SetUserPermissionPacket(user, Permissions.MASTER, true));

                                                    source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.master.added", player.getName()), true);
                                                    return 0;
                                                })
                                        )
                                        .then(Commands.literal("remove")
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                                                .executes((context) -> {
                                                    CommandSourceStack source = context.getSource();
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "target");
                                                    UUID id = player.getUUID();
                                                    ServerUser user = ServerUser.get(id);
                                                    user.removePermission(Permissions.MASTER);
                                                    Networking.get().sendToAllClients(new SetUserPermissionPacket(user, Permissions.MASTER, false));

                                                    source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.master.removed", player.getName()), true);
                                                    return 0;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("access")
                                .then(Commands.literal("add")
                                        .then(Commands.argument("target", GameProfileArgument.gameProfile())
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)))
                                                .executes((context) -> {
                                                    CommandSourceStack source = context.getSource();
                                                    Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(context, "target");
                                                    for (GameProfile profile : profiles) {
                                                        AccessManager.get().addAccess(profile.getId());
                                                    }

                                                    source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.access.added", profiles.stream()
                                                            .map(gameProfile -> gameProfile.getName() != null ? gameProfile.getName() : gameProfile.getId().toString())
                                                            .collect(Collectors.joining(", "))), false);
                                                    return 0;
                                                })
                                        )
                                )
                                .then(Commands.literal("add-uuid")
                                        .then(Commands.argument("uuid", UuidArgument.uuid())
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)))
                                                .executes((context) -> {
                                                    CommandSourceStack source = context.getSource();
                                                    UUID uuid = UuidArgument.getUuid(context, "uuid");
                                                    AccessManager.get().addAccess(uuid);

                                                    source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.access.added", uuid), false);
                                                    return 0;
                                                })
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("target", GameProfileArgument.gameProfile())
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                                                .executes((context) -> {
                                                    CommandSourceStack source = context.getSource();
                                                    Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(context, "target");
                                                    for (GameProfile profile : profiles) {
                                                        AccessManager.get().removeAccess(profile.getId());
                                                    }

                                                    source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.access.removed", profiles.stream()
                                                            .map(gameProfile -> gameProfile.getName() != null ? gameProfile.getName() : gameProfile.getId().toString())
                                                            .collect(Collectors.joining(", "))), false);
                                                    return 0;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("owner")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("target", EntityArgument.player())
                                                .requires((source) -> (source.hasPermission(4) && isServer(source)) || isOwner(source))
                                                .executes((context) -> {
                                                    CommandSourceStack source = context.getSource();
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "target");

                                                    if (!UEssentials.hasOwner()) {
                                                        UEssentials.setOwner(player);
                                                    }

                                                    source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.owner.set", player.getName()), true);
                                                    return 0;
                                                })
                                        )
                                )
                        )
                )
        );
    }

    private static boolean isOwner(CommandSourceStack source) {
        return UEssentials.isOwner(source.getEntity());
    }

    private static boolean isServer(CommandSourceStack source) {
        return source.getEntity() == null;
    }
}
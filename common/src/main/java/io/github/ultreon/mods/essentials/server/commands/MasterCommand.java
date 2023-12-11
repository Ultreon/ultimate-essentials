package io.github.ultreon.mods.essentials.server.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.permission.SetUserPermissionPacket;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public final class MasterCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("ue_master")
                .then(Commands.argument("targets", GameProfileArgument.gameProfile())
                        .then(Commands.literal("add")
                                .requires((source) -> source.hasPermission(4) && isServer(source))
                                .executes((context) -> {
                                    CommandSourceStack source = context.getSource();
                                    Collection<GameProfile> targets = GameProfileArgument.getGameProfiles(context, "targets");
                                    if (targets.size() == 1) {
                                        GameProfile profile = new ArrayList<>(targets).get(0);
                                        UUID id = profile.getId();
                                        ServerUser user = ServerUser.get(id);
                                        user.addPermission(Permissions.MASTER);
                                        Networking.get().sendToAllClients(new SetUserPermissionPacket(user, Permissions.MASTER, true));

                                        source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.master.added", profile.getName()), true);
                                    } else if (targets.isEmpty()) {
                                        source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.master.not_found"), true);
                                    } else {
                                        source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.master.too_many"), true);
                                    }

                                    return 0;
                                }))
                        .then(Commands.literal("remove"))
                        .requires((source) -> source.hasPermission(4) && isServer(source))
                        .executes((context) -> {
                            CommandSourceStack source = context.getSource();
                            Collection<GameProfile> targets = GameProfileArgument.getGameProfiles(context, "targets");
                            if (targets.size() == 1) {
                                GameProfile gameProfile = new ArrayList<>(targets).get(0);
                                UUID id = gameProfile.getId();
                                ServerUser user = ServerUser.get(id);
                                user.removePermission(Permissions.MASTER);
                                Networking.get().sendToAllClients(new SetUserPermissionPacket(user, Permissions.MASTER, false));

                                source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.master.removed", gameProfile.getName()), true);
                            } else if (targets.isEmpty()) {
                                source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.master.not_found"), true);
                            } else {
                                source.sendSuccess(() -> Component.translatable("command.ultimate_essentials.master.too_many"), true);
                            }

                            return 0;
                        }))
        );
    }

    private static boolean isServer(CommandSourceStack source) {
        return source.getEntity() == null;
    }
}
package io.github.ultreon.mods.essentials.server;

import dev.architectury.event.EventResult;
import dev.architectury.utils.value.IntValue;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.UUID;

public class ServerEvents {
    private static final HashMap<UUID, Integer> playerTicks = new HashMap<>();

    public static void onPlayerTick(Player player) {
        ServerUser.get(player.getUUID()).tick(player);

//        UUID uuid = player.getUUID();
//        playerTicks.putIfAbsent(uuid, -1);
//        int integer = playerTicks.get(uuid);
//        playerTicks.put(uuid, integer++);
//
//        ServerUser.get(uuid).tick();
//
//        if (integer >= 0) {
//            if (player instanceof ServerPlayer serverPlayer) {
//                if (!ServerAuthType.isLoggedIn(player.getUUID()) && !UEssentials.isOwner(serverPlayer)) {
//                    event.setCanceled(true);
//                    ServerAuthType.delayedKick(serverPlayer);
//                }
//                if (!AccessManager.get().hasAccess(serverPlayer) && !UEssentials.isOwner(serverPlayer)) {
//                    event.setCanceled(true);
//                    Networking.get().sendToClient(new ErrorScreenPacket("§c§lAccess Denied", "§7§oDisallowed connection.", true), serverPlayer);
//                    ServerAuthType.delayedKick(serverPlayer, new Component("§c§lAccess Denied\n§7§oDisallowed connection."));
//                }
//            }
//        }
    }

    public static EventResult onBlockBreak(Level level, BlockPos pos, BlockState state, ServerPlayer player, IntValue xp) {
        ServerUser user = ServerUser.get(player);
        return user.hasPermission(Permissions.BREAK_BLOCKS) ? EventResult.pass() : EventResult.interruptFalse();
    }
}

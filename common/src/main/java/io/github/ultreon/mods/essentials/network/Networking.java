package io.github.ultreon.mods.essentials.network;

import com.ultreon.mods.lib.network.api.Network;
import com.ultreon.mods.lib.network.api.PacketRegisterContext;
import com.ultreon.mods.lib.network.api.packet.BasePacket;
import com.ultreon.mods.lib.network.api.packet.ClientEndpoint;
import com.ultreon.mods.lib.util.ServerLifecycle;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.network.cheats.*;
import io.github.ultreon.mods.essentials.network.economy.*;
import io.github.ultreon.mods.essentials.network.homes.*;
import io.github.ultreon.mods.essentials.network.moderation.*;
import io.github.ultreon.mods.essentials.network.permission.*;
import io.github.ultreon.mods.essentials.network.ui.ErrorScreenPacket;
import io.github.ultreon.mods.essentials.network.teleport.*;
import io.github.ultreon.mods.essentials.network.ui.SetOverlayMessagePacket;
import io.github.ultreon.mods.essentials.network.ui.ShowOverlayMessagePacket;
import io.github.ultreon.mods.essentials.network.warps.*;
import io.github.ultreon.mods.essentials.user.ServerUser;
import net.minecraft.network.chat.Component;

/**
 * The Networking class provides methods for handling network communication between client and server.
 */
public final class Networking extends Network {
    private static Networking instance;

    private Networking() {
        super(UEssentials.MOD_ID, "network");
    }

    public static synchronized void initialize() {
        if (instance == null) {
            instance = new Networking();
        }
    }

    public static ClientConnection connection() {
        return new ClientConnection();
    }

    public static Networking get() {
        return instance;
    }

    public <T extends BasePacket<T> & ClientEndpoint> void sendToAllClients(BasePacket<T> packet) { // has to be ServerPlayer if world is not null
        channel.sendToPlayers(ServerLifecycle.getCurrentServer().getPlayerList().getPlayers(), packet);
    }

    public static void handleIllegalPacket(ServerUser user) {
        user.disconnect(Component.translatable("ultimate_essentials.error_code.disconnected"));
    }

    @Override
    protected void registerPackets(PacketRegisterContext ctx) {
        // UI packets
        ctx.register(ErrorScreenPacket::new);
        ctx.register(ShowOverlayMessagePacket::new);
        ctx.register(SetOverlayMessagePacket::new);

        // Home packets
        ctx.register(SetHomePacket::new);
        ctx.register(ListHomesPacket::new);
        ctx.register(ListHomesPacket.Begin::new);
        ctx.register(ListHomesPacket.Entry::new);
        ctx.register(ListHomesPacket.End::new);
        ctx.register(TeleportHomePacket::new);
        ctx.register(DeleteHomePacket::new);
        ctx.register(RenameHomePacket::new);
        ctx.register(RepositionHomePacket::new);
        ctx.register(SetRotationOfHomePacket::new);

        // Warp packets
        ctx.register(SetWarpPacket::new);
        ctx.register(ListWarpsPacket::new);
        ctx.register(ListWarpsPacket.Begin::new);
        ctx.register(ListWarpsPacket.Entry::new);
        ctx.register(ListWarpsPacket.End::new);
        ctx.register(TeleportWarpPacket::new);
        ctx.register(DeleteWarpPacket::new);
        ctx.register(RenameWarpPacket::new);
        ctx.register(RepositionWarpPacket::new);
        ctx.register(SetRotationOfWarpPacket::new);

        // Teleport packets
        ctx.register(RequestTpToPacket::new);
        ctx.register(RequestTpFromPacket::new);
        ctx.register(TpRequestedFromPacket::new);
        ctx.register(TpRequestedToPacket::new);
        ctx.register(ListTpRequestsPacket::new);
        ctx.register(ListTpRequestsPacket.Begin::new);
        ctx.register(ListTpRequestsPacket.Entry::new);
        ctx.register(ListTpRequestsPacket.End::new);
        ctx.register(AcceptTpRequestPacket::new);
        ctx.register(DenyTpRequestPacket::new);
        ctx.register(TeleportBackPacket::new);

        // User moderation packets
        ctx.register(KickPlayerPacket::new);
        ctx.register(BanPlayerPacket::new);
        ctx.register(TeleportMeToPacket::new);
        ctx.register(TeleportToMePacket::new);
        ctx.register(OpPlayerPacket::new);
        ctx.register(DeOpPlayerPacket::new);
        ctx.register(InstaKillPacket::new);

        // User cheat packets.
        ctx.register(FullFeedPlayerPacket::new);
        ctx.register(FullHealPlayerPacket::new);
        ctx.register(GodModePacket::new);
        ctx.register(FlyModePacket::new);
        ctx.register(BurnPlayerPacket::new);

        // Game moderation
        ctx.register(SetWeatherPacket::new);

        // Permissions
        ctx.register(SetRolePermissionPacket::new);
        ctx.register(SetUserPermissionPacket::new);
        ctx.register(CreatePermissionPacket::new);
        ctx.register(CreateRolePacket::new);
        ctx.register(DeleteRolePacket::new);

        // Economy
        ctx.register(ListShopPagePacket::new);
        ctx.register(ListShopPagePacket.Begin::new);
        ctx.register(ListShopPagePacket.Page::new);
        ctx.register(ListShopPagePacket.End::new);
        ctx.register(BuyPacket::new);
        ctx.register(BuyPacket.Response::new);
        ctx.register(SetBalancePacket::new);
        ctx.register(AddMoneyPacket::new);
        ctx.register(ReduceMoneyPacket::new);
        ctx.register(ExchangeExpPacket::new);
    }
}

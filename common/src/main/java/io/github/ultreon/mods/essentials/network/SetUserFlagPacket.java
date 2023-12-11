package io.github.ultreon.mods.essentials.network;

import com.ultreon.mods.lib.network.api.packet.BiDirectionalPacket;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.user.UserFlags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetUserFlagPacket extends BiDirectionalPacket<SetUserFlagPacket> {
    private final UserFlags flag;
    private final boolean set;

    public SetUserFlagPacket(FriendlyByteBuf buf) {
        this.flag = buf.readEnum(UserFlags.class);
        this.set = buf.readBoolean();
    }

    public SetUserFlagPacket(UserFlags flag, boolean set) {
        this.flag = flag;
        this.set = set;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(flag);
        buf.writeBoolean(set);
    }

    @Override
    protected void handleClient() {
        if (flag == UserFlags.MODERATOR) {
            LocalUser.get().setModerator(set);
        }
        if (flag == UserFlags.ADMIN) {
            LocalUser.get().setAdmin(set);
        }
    }

    @Override
    protected void handleServer(ServerPlayer sender) {
        sender.sendSystemMessage(Component.literal("Sorry but this feature is unavailable as of now.").withStyle(ChatFormatting.AQUA));
    }
}

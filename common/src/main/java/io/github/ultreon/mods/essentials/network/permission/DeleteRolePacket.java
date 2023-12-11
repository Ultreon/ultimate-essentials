package io.github.ultreon.mods.essentials.network.permission;

import com.ultreon.mods.lib.network.api.packet.BiDirectionalPacket;
import io.github.ultreon.mods.essentials.Constants;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.user.*;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

@Getter
public class DeleteRolePacket extends BiDirectionalPacket<DeleteRolePacket> {
    private final String role;

    public DeleteRolePacket(FriendlyByteBuf buffer) {
        super();
        this.role = buffer.readUtf(Constants.MAX_ROLE_NAME_LEN);
    }

    public DeleteRolePacket(Role role) {
        super();
        this.role = role.getName();
    }

    @Override
    protected void handleClient() {
        ClientRoles.handlePacket(this);
    }

    @Override
    protected void handleServer(ServerPlayer sender) {
        ServerUser user = ServerUser.get(Objects.requireNonNull(sender));
        if (user.hasPermission(Permissions.DELETE_ROLES)) {
            ServerRoles.deleteRole(role);
        } else {
            Networking.handleIllegalPacket(user);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.role, Constants.MAX_ROLE_NAME_LEN);
    }
}

package io.github.ultreon.mods.essentials.network.permission;

import com.ultreon.mods.lib.network.api.packet.BiDirectionalPacket;
import io.github.ultreon.mods.essentials.Constants;
import io.github.ultreon.mods.essentials.client.gui.screens.moderation.PermissionsScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.user.*;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

@Getter
public class SetRolePermissionPacket extends BiDirectionalPacket<SetRolePermissionPacket> {
    private final String roleName;
    private final String permission;
    private final boolean enable;

    public SetRolePermissionPacket(FriendlyByteBuf buf) {
        super();

        this.roleName = buf.readUtf(Constants.MAX_ROLE_NAME_LEN);
        this.permission = buf.readUtf(Constants.MAX_PERMISSION_ID_LEN);
        this.enable = buf.readBoolean();
    }

    public SetRolePermissionPacket(Role role, Permission permission, boolean enable) {
        super();

        this.roleName = role.getName();
        this.permission = permission.id();
        this.enable = enable;
    }

    @Override
    protected void handleClient() {
        ClientRoles.handlePacket(this);

        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof PermissionsScreen) {
            ((PermissionsScreen) screen).handlePacket(this);
        }
    }

    @Override
    protected void handleServer(ServerPlayer sender) {
        ServerUser user = ServerUser.get(Objects.requireNonNull(sender));
        Permission permission = ServerPermissions.getPermission(getPermission());
        if (permission == null) {
            Networking.handleIllegalPacket(user);
        }
        if (user.hasPermission(Permissions.CHANGE_ROLE_PERMISSIONS)) {
            if (permission != Permissions.CHANGE_PERMISSIONS) {
                ServerRoles.handlePacket(this);
                return;
            }
        }

        Networking.handleIllegalPacket(user);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.roleName, Constants.MAX_ROLE_NAME_LEN);
        buf.writeUtf(this.permission, Constants.MAX_PERMISSION_ID_LEN);
        buf.writeBoolean(this.enable);
    }
}

package io.github.ultreon.mods.essentials.network.permission;

import com.ultreon.mods.lib.network.api.packet.BiDirectionalPacket;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.client.gui.screens.moderation.PermissionsScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.user.*;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public class SetUserPermissionPacket extends BiDirectionalPacket<SetUserPermissionPacket> {
    private final UUID user;
    private final String permission;
    private final boolean enable;

    public SetUserPermissionPacket(FriendlyByteBuf buf) {
        super();
        this.user = buf.readUUID();
        this.permission = buf.readUtf();
        this.enable = buf.readBoolean();
    }

    public SetUserPermissionPacket(User user, Permission permission, boolean enable) {
        super();
        this.user = user.uuid();
        this.permission = permission.id();
        this.enable = enable;
    }

    @Override
    protected void handleClient() {
        RemoteUser user = RemoteUser.get(this.user);
        user.handlePacket(this);

        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof PermissionsScreen) {
            LocalUser localUser = LocalUser.get();
            if (this.user == localUser.uuid()) {
                localUser.handlePacket(this);
            }
            ((PermissionsScreen) screen).handlePacket(this);
        }
    }

    @Override
    protected void handleServer(ServerPlayer sender) {
        ServerUser user = ServerUser.get(Objects.requireNonNull(sender));
        Permission permission = ServerPermissions.getPermission(this.getPermission());
        if (permission == null) {
            user.kick("Illegal use of permissions");
            return;
        }
        Set<Permission> enablePermissions = user.getEnabledPermissions();
        UEssentials.LOGGER.warn(user.getName());
        UEssentials.LOGGER.warn(StringUtils.join(enablePermissions, ", "));

        ServerUser target = ServerUser.get(getUser());

        if (user.canChangePermission(target, permission)) {
            target.setPermission(permission, isEnable());
            Networking.get().sendToAllClients(this);
            return;
        }

        user.kick("Changing permission not permitted.");
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.user);
        buf.writeUtf(this.permission);
        buf.writeBoolean(this.enable);
    }
}

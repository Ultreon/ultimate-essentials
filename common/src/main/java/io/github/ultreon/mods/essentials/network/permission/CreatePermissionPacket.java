package io.github.ultreon.mods.essentials.network.permission;

import com.ultreon.mods.lib.network.api.packet.PacketToClient;
import io.github.ultreon.mods.essentials.Constants;
import io.github.ultreon.mods.essentials.user.ClientPermissions;
import io.github.ultreon.mods.essentials.user.Permission;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;

@Getter
public class CreatePermissionPacket extends PacketToClient<CreatePermissionPacket> {
    private final String permission;

    public CreatePermissionPacket(FriendlyByteBuf buffer) {
        super();

        this.permission = buffer.readUtf(Constants.MAX_PERMISSION_ID_LEN);
    }

    public CreatePermissionPacket(Permission permission) {
        super();
        this.permission = permission.id();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.permission, Constants.MAX_PERMISSION_ID_LEN);
    }

    @Override
    protected void handle() {
        ClientPermissions.registerPermission(new Permission(permission));
    }
}

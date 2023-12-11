package io.github.ultreon.mods.essentials.network.permission;

import com.ultreon.mods.lib.network.api.packet.BiDirectionalPacket;
import io.github.ultreon.mods.essentials.Constants;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.user.*;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class CreateRolePacket extends BiDirectionalPacket<CreateRolePacket> {
    private static final int LIMIT = Constants.ROLE_PERMISSION_LIMIT;
    private final String roleName;
    private final Set<String> permissions;

    public CreateRolePacket(FriendlyByteBuf buf) {
        super();
        this.roleName = buf.readUtf(Constants.MAX_ROLE_NAME_LEN);
        int len = buf.readInt();

        if (len > LIMIT) {
            throw new IndexOutOfBoundsException("Length above limit of " + LIMIT);
        }

        Set<String> permissions = new HashSet<>();
        for (int i = 0; i < len; i++) {
            permissions.add(buf.readUtf(Constants.MAX_PERMISSION_ID_LEN));
        }

        this.permissions = permissions;
    }

    public CreateRolePacket(Role role) {
        this(role, new HashSet<>());
    }

    public CreateRolePacket(Role role, Set<String> perms) {
        this.roleName = role.getName();
        this.permissions = perms;
    }

    @Override
    protected void handleClient() {
        ClientRoles.handlePacket(this);
    }

    @Override
    protected void handleServer(ServerPlayer sender) {
        ServerUser user = ServerUser.get(Objects.requireNonNull(sender));
        if (user.hasPermission(Permissions.CREATE_ROLES)) {
            Set<String> permissions = getPermissions();
            ServerRole.Properties properties = new ServerRole.Properties(this.roleName);

            for (String name : permissions) {
                Permission permission = ServerPermissions.getPermission(name);
                if (permission == null) {
                    Networking.handleIllegalPacket(user);
                }

                properties.permission(permission);
            }
            ServerRoles.createRole(properties);
        } else {
            Networking.handleIllegalPacket(user);
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.roleName, Constants.MAX_ROLE_NAME_LEN);
        buffer.writeInt(this.permissions.size());
        for (String perm : permissions) {
            buffer.writeUtf(perm, Constants.MAX_PERMISSION_ID_LEN);
        }
    }

}

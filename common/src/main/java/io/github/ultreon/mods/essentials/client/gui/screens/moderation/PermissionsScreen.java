package io.github.ultreon.mods.essentials.client.gui.screens.moderation;

import com.ultreon.mods.lib.client.gui.widget.Button;
import io.github.ultreon.mods.essentials.client.gui.widget.list.ListScreen;
import io.github.ultreon.mods.essentials.client.gui.widget.list.ListWidget;
import io.github.ultreon.mods.essentials.network.permission.SetRolePermissionPacket;
import io.github.ultreon.mods.essentials.network.permission.SetUserPermissionPacket;
import io.github.ultreon.mods.essentials.user.*;
import io.github.ultreon.mods.essentials.util.PermissionContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.*;

public class PermissionsScreen extends ListScreen {
    private final PermissionContainer targetPermHolder;
    private final Map<Permission, ListWidget.Entry> permissionMap = new HashMap<>();

    public PermissionsScreen(Minecraft minecraft, PermissionContainer targetPermHolder) {
        super(Component.translatable(
                targetPermHolder instanceof User ? "gui.ultimate_essentials.user_permissions.title" : (targetPermHolder instanceof Role ? "gui.ultimate_essentials.role_permissions.title" : "gui.ultimate_essentials.permissions.title"),
                targetPermHolder instanceof User ? ((User) targetPermHolder).getName() : (targetPermHolder instanceof Role ? ((Role) targetPermHolder).getName() : "")));

        setListFilter(this::searchFilter);

        List<Permission> allPermissions = new ArrayList<>(ClientPermissions.getAllPermissions());
        allPermissions.sort(Comparator.comparing(Permission::id));

        for (Permission permission : allPermissions) {
            Button button = new Button(0, 0, 60, 20, targetPermHolder.hasPermission(permission) ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF, ($) -> {
                if (LocalUser.get().canChangePermission(targetPermHolder, permission)) {
                    if (targetPermHolder.hasPermission(permission)) {
                        targetPermHolder.removePermission(permission);
                    } else {
                        targetPermHolder.addPermission(permission);
                    }
                }
            });
            button.active = LocalUser.get().canChangePermission(targetPermHolder, permission);
            ListWidget.Entry entry = new ListWidget.Entry(minecraft, this, I18n.get(permission.getTranslationId()), permission.id(), permission.id(), button);
            addEntry(entry);
            permissionMap.put(permission, entry);
        }

        this.targetPermHolder = targetPermHolder;
    }

    private boolean searchFilter(String query, String id, String title, String description) {
        if (Objects.equals(query, "")) {
            return true;
        }

        if (query.startsWith("&")) {
            for (int i = 0; i < description.length(); i++) {
                String s = description.toLowerCase(Locale.ROOT).substring(i);
                if (s.startsWith(query.substring(1).toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < title.length(); i++) {
                String s = title.toLowerCase(Locale.ROOT).substring(i);
                if (s.startsWith(query.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void tick() {
        List<Permission> allPermissions = new ArrayList<>(ClientPermissions.getAllPermissions());
        allPermissions.sort(Comparator.comparing(Permission::id));

        for (Permission permission : allPermissions) {
            ListWidget.Entry entry = permissionMap.get(permission);
            Button button = (Button) entry.children().get(0);
            button.active = LocalUser.get().canChangePermission(targetPermHolder, permission);
            button.setMessage(targetPermHolder.hasExactPermission(permission) ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
        }
    }

    public void handlePacket(SetRolePermissionPacket pak) {
        if (ClientRoles.getRole(pak.getRoleName()) == targetPermHolder) {
            Permission permission = new Permission(pak.getPermission());
            ListWidget.Entry entry = permissionMap.get(permission);
            Button button = (Button) entry.children().get(0);
            button.active = LocalUser.get().canChangePermission(targetPermHolder, permission);
            button.setMessage(pak.isEnable() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
        }
    }

    public void handlePacket(SetUserPermissionPacket pak) {
        if (RemoteUser.get(pak.getUser()) == targetPermHolder) {
            Permission permission = new Permission(pak.getPermission());
            ListWidget.Entry entry = permissionMap.get(new Permission(pak.getPermission()));
            Button button = (Button) entry.children().get(0);
            button.active = LocalUser.get().canChangePermission(targetPermHolder, permission);
            button.setMessage(pak.isEnable() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
        }
    }
}

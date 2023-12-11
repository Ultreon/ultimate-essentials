package io.github.ultreon.mods.essentials.mixin;

import com.mojang.authlib.GameProfile;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.permission.SetUserPermissionPacket;
import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.user.Permissions;
import io.github.ultreon.mods.essentials.user.ServerUser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.PlayerListComponent;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

@Mixin(PlayerListComponent.class)
public abstract class PlayerListComponentMixin extends JList<String> {
    @Shadow
    @Final
    private MinecraftServer server;
    @Unique
    private static final JPopupMenu UEssentials$POPUP_MENU;
    @Unique
    private static final JMenuItem UEssentials$COPY_CODE;
    @Unique
    private boolean uEssentials$firstTick = true;

    @Unique
    private static final PopupMenu uEssentials$popupMenu = new PopupMenu();
    @Unique
    private static final MenuItem uEssentials$makeMaster = new MenuItem("Make Master");
    @Unique
    private static final MenuItem uEssentials$revokeMaster = new MenuItem("Revoke Master");

    @Unique
    private static ServerPlayer uEssentials$player;

    static {
        UEssentials$POPUP_MENU = new JPopupMenu();
        UEssentials$COPY_CODE = new JMenuItem("Copy Invite Code");
        UEssentials$COPY_CODE.addActionListener(e -> {
            String myString = UEssentials.getInviteCode();
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
        UEssentials$POPUP_MENU.add(UEssentials$COPY_CODE);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void uEssentials$tick(CallbackInfo ci) {
        if (uEssentials$firstTick) {
            uEssentials$firstTick = false;

            JList<String> t = this;

            t.add(uEssentials$popupMenu);

            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    check(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    check(e);
                }

                private void check(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        Point point = e.getPoint();
                        System.out.println(point);
                        int i = locationToIndex(e.getPoint());
                        System.out.println(i);
                        if (i == -1) {
                            return;
                        }
                        Rectangle cellBounds = getCellBounds(i, i);
                        System.out.println(new Rectangle(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height)
                                .contains(point)
                        );
                        System.out.println(new Rectangle(getLocationOnScreen().x, getLocationOnScreen().y, cellBounds.width, cellBounds.height)
                                .contains(point)
                        );
                        System.out.println(new Rectangle(getLocationOnScreen().x + cellBounds.x, getLocationOnScreen().y + cellBounds.y, cellBounds.width, cellBounds.height)
                                .contains(point)
                        );
                        boolean contains = cellBounds.contains(point);
                        System.out.println(contains);
                        if (!contains) {
                            return;
                        }

                        String elementAt = getModel().getElementAt(i);
                        System.out.println(elementAt);
                        MinecraftServer server = UEssentials.server();
                        uEssentials$player = server.getPlayerList().getPlayerByName(elementAt);
                        uEssentials$popupMenu.show(t, point.x, point.y);
                    }
                }
            });

            uEssentials$makeMaster.addActionListener(evt -> {
                GameProfile profile = Objects.requireNonNull(uEssentials$player).getGameProfile();
                server.setSingleplayerProfile(profile);
                ServerUser user = ServerUser.get(uEssentials$player);
                user.addPermission(Permissions.MASTER);

                Networking.get().sendToClient(new SetUserPermissionPacket(user, Permissions.MASTER, true), user.player());
            });

            uEssentials$revokeMaster.addActionListener(evt -> {
                GameProfile profile = Objects.requireNonNull(uEssentials$player).getGameProfile();
                server.setSingleplayerProfile(profile);
                ServerUser user = ServerUser.get(uEssentials$player);
                user.removePermission(Permissions.MASTER);

                Networking.get().sendToClient(new SetUserPermissionPacket(user, Permissions.MASTER, false), user.player());
            });

            uEssentials$popupMenu.add(uEssentials$makeMaster);
            uEssentials$popupMenu.add(uEssentials$revokeMaster);
        }
    }
}
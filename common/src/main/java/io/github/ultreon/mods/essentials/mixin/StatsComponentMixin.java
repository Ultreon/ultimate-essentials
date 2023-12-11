package io.github.ultreon.mods.essentials.mixin;

import com.ultreon.mods.lib.util.ServerLifecycle;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.ultreon.mods.essentials.UEssentials;
import net.minecraft.CrashReport;
import net.minecraft.server.gui.StatsComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

@Mixin(StatsComponent.class)
public abstract class StatsComponentMixin extends JComponent {
    @Unique
    private static final JPopupMenu UEssentials$POPUP_MENU;
    @Unique
    private static final JMenuItem UEssentials$COPY_CODE;
    @Unique
    private boolean uEssentials$firstTick = true;
    @Unique
    private boolean uEssentials$inviteCodeLoaded = false;


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

    public StatsComponentMixin() {

    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void uEssentials$tick(CallbackInfo ci) {
        if (uEssentials$firstTick) {
            uEssentials$firstTick = false;
        }

        if (UEssentials.getInviteCode() != null && !uEssentials$inviteCodeLoaded) {
            uEssentials$inviteCodeLoaded = true;
            setComponentPopupMenu(UEssentials$POPUP_MENU);
        }

        if (UEssentials.getInviteCode() == null && uEssentials$inviteCodeLoaded) {
            uEssentials$inviteCodeLoaded = false;
            setComponentPopupMenu(null);
        }
    }

    @Inject(method = "paint", at = @At("RETURN"))
    public void uEssentials$paint(Graphics i, CallbackInfo ci) {
        try {
            EnvExecutor.runInEnv(Env.SERVER, () -> () -> uEssentials$paintWrap(i));
        } catch (Throwable t) {
            ServerLifecycle.getCurrentServer().onServerCrash(new CrashReport("Failed to setup Server UI", t));
        }
        Graphics2D g2d = (Graphics2D) i;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    @Inject(method = "paint", at = @At("HEAD"))
    public void uEssentials$paintHead(Graphics i, CallbackInfo ci) {
        Graphics2D g2d = (Graphics2D) i;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    @Unique
    private <T> T uEssentials$paintWrap(Graphics i) {
        String inviteCode = UEssentials.getInviteCode();
        if (inviteCode != null) {
            AttributedString attributedString = new AttributedString("Invite Code: " + (inviteCode == null ? "" : inviteCode));
            attributedString.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, 0, 13);
            i.drawString(attributedString.getIterator(), 32, 10);
            i.setFont(new Font(i.getFont().getFontName(), Font.ITALIC, i.getFont().getSize()));
            i.drawString("Use right-click menu to copy invite code.", 32, 25);
        }
        return null;
    }
}
package io.github.ultreon.mods.essentials.client.gui.screens.toast;

import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public class FailureToast extends SystemToast {
    public FailureToast(Component title, Component subtitle) {
        super(SystemToastIds.PACK_LOAD_FAILURE, title, subtitle);
    }
}

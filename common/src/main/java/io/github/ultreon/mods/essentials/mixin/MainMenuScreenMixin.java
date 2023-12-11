package io.github.ultreon.mods.essentials.mixin;

import com.ultreon.mods.lib.client.gui.screen.PanoramaScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MainMenuScreenMixin extends Screen {
    @Mutable
    @Final
    @Shadow
    private PanoramaRenderer panorama;

    protected MainMenuScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("HEAD"))
    public void uEssentials$init$head(CallbackInfo ci) {
        panorama = PanoramaScreen.PANORAMA;
    }
}
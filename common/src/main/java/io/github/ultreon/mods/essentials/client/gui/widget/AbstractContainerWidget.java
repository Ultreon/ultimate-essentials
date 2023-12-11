package io.github.ultreon.mods.essentials.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class AbstractContainerWidget extends AbstractWidget implements ContainerEventHandler {
   @Nullable
   private GuiEventListener focused;
   private boolean isDragging;

   public AbstractContainerWidget(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Component p_93633_) {
      super(p_93629_, p_93630_, p_93631_, p_93632_, p_93633_);
   }

   public final boolean isDragging() {
      return this.isDragging;
   }

   public final void setDragging(boolean p_94681_) {
      this.isDragging = p_94681_;
   }

   @Nullable
   public GuiEventListener getFocused() {
      return this.focused;
   }

   public void setFocused(@Nullable GuiEventListener p_94677_) {
      this.focused = p_94677_;
   }
}
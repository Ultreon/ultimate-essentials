package io.github.ultreon.mods.essentials.client.gui.screens;

import com.mojang.datafixers.util.Pair;
import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import com.ultreon.mods.lib.client.gui.widget.BaseButton;
import io.github.ultreon.mods.essentials.client.UEssentialsClient;
import io.github.ultreon.mods.essentials.util.Execution;
import io.github.ultreon.mods.essentials.util.Validation;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TextInsertScreen extends UEssentialsMenuScreen {
    public static final Component SEARCH_BACKDROP = Component.translatable("gui.ultimate_essentials.setHome.nameField");
    public static final ResourceLocation BACKGROUND_TEXTURE = UEssentialsClient.res("textures/gui/set_home.png");

    @Getter
    protected EditBox input;
    private final String placeholder;
    private final BiConsumer<TextInsertScreen, String> onDone;
    private final Consumer<TextInsertScreen> onCancel;
    private final Validation<String> validation;
    protected BaseButton doneBtn;

    protected TextInsertScreen(Component title, Consumer<TextInsertScreen> run, BiConsumer<TextInsertScreen, String> onDone, Consumer<TextInsertScreen> onCancel, Validation<String> validation) {
        this(title, "", run, onDone, onCancel, validation);
    }

    protected TextInsertScreen(Component title, String placeholder, Consumer<TextInsertScreen> run, BiConsumer<TextInsertScreen, String> onDone, Consumer<TextInsertScreen> onCancel, Validation<String> validation) {
        super(new Properties().title(title).titleStyle(TitleStyle.DETACHED));
        this.placeholder = placeholder;
        this.onDone = onDone;
        this.onCancel = onCancel.andThen(TextInsertScreen::onClose);
        this.validation = validation;
        run.accept(this);
    }

    public static Build open(String title, String placeholder) {
        return new Build(Component.literal(title), placeholder);
    } 

    public static Build open(Component title, String placeholder) {
        return new Build(title, placeholder);
    }

    public void tick() {
        super.tick();
        this.input.tick();
    }

    protected void init() {
        String s = this.input != null ? this.input.getValue() : "";
//        this.input = new EditBox(this.font, this.left() + 7, this.top() + 19, 196, 16, SEARCH_BACKDROP);
        this.input = new EditBox(this.font, 0, 0, 0, 0, SEARCH_BACKDROP);
        this.input.setMaxLength(16);
        this.input.setBordered(false);
        this.input.setVisible(true);
        this.input.setTextColor(0xffffff);
        this.input.setValue(s);

//        setPanorama(false);

        addInputRow(input);
        Pair<BaseButton, BaseButton> buttonRow = addButtonRow(Component.translatable("gui.done"), (button) -> {
            if (!button.visible) return;
            if (!button.active) return;
            done();
        }, Component.translatable("gui.cancel"), (button) -> {
            if (!button.visible) return;
            if (!button.active) return;
            cancel();
        });

        doneBtn = buttonRow.getFirst();
    }

    public void cancel() {
        onClose();
        this.onCancel.accept(this);
    }

    public void done() {
        String value = input.getValue();

        if (!validation.test(value)) {
            return;
        }

        onClose();
        this.onDone.accept(this, value);
    }

    public static void drawCenteredString(@NotNull GuiGraphics gfx, Font font, Component component, int text, int x, int y) {
        FormattedCharSequence charSequence = component.getVisualOrderText();
        gfx.drawString(font, charSequence, text - font.width(charSequence) / 2, x, y);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.input.isFocused()) {
            this.input.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button)/* || this.filterList.mouseClicked(mouseX, mouseY, button)*/;
    }

    @Override
    public boolean charTyped(char p_94683_, int p_94684_) {
        boolean b = super.charTyped(p_94683_, p_94684_);
        doneBtn.active = validation.test(input.getValue());
        return b;
    }

    @Override
    public boolean keyPressed(int p_96552_, int p_96553_, int p_96554_) {
        boolean b = super.keyPressed(p_96552_, p_96553_, p_96554_);
        doneBtn.active = validation.test(input.getValue());
        return b;
    }

    @Override
    public boolean keyReleased(int p_94715_, int p_94716_, int p_94717_) {
        boolean b = super.keyReleased(p_94715_, p_94716_, p_94717_);
        doneBtn.active = validation.test(input.getValue());
        return b;
    }

    @Override
    public void afterKeyboardAction() {
        doneBtn.active = validation.test(input.getValue());
        super.afterKeyboardAction();
    }

    public static class Build implements Execution {
        protected Consumer<TextInsertScreen> run = scr -> {};
        protected BiConsumer<TextInsertScreen, String> done = (scr, s) -> {};
        protected Consumer<TextInsertScreen> cancel = scr -> {};
        protected Validation<String> validate = Validation.initial();
        protected final Component component;
        protected final String placeholder;

        protected Build(Component component, String placeholder) {
            this.component = component;
            this.placeholder = placeholder;
        }
        
        public Build then(Consumer<TextInsertScreen> consumer) {
            if (run == null) {
                run = consumer;
                return this;
            }
            run = run.andThen(consumer);
            return this;
        }
        
        public Build done(BiConsumer<TextInsertScreen, String> consumer) {
            if (done == null) {
                done = consumer;
                return this;
            }
            done = done.andThen(consumer);
            return this;
        }
        
        public Build cancel(final Consumer<TextInsertScreen> consumer) {
            if (cancel == null) {
                cancel = consumer;
                return this;
            }
            cancel = cancel.andThen(consumer);
            return this;
        }

        @Override
        public void execute() {
            Minecraft.getInstance().setScreen(new TextInsertScreen(this.component, this.placeholder, run, done, cancel, validate));
        }

        public Build validate(Validation<String> consumer) {
            validate = validate.then(consumer);
            return this;
        }
    }
}

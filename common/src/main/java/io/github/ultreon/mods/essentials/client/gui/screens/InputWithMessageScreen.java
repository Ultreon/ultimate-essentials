package io.github.ultreon.mods.essentials.client.gui.screens;

import com.ultreon.mods.lib.client.gui.widget.Label;
import io.github.ultreon.mods.essentials.util.Validation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InputWithMessageScreen extends TextInsertScreen {
    private final MessageFunction onType;
    private Label label;

    protected InputWithMessageScreen(Component title, Consumer<TextInsertScreen> run, BiConsumer<TextInsertScreen, String> onDone, Consumer<TextInsertScreen> onCancel, MessageFunction onType, Validation<String> validation) {
        super(title, run, onDone, onCancel, validation);

        this.onType = onType;
    }

    protected InputWithMessageScreen(Component title, String placeholder, Consumer<TextInsertScreen> run, BiConsumer<TextInsertScreen, String> onDone, Consumer<TextInsertScreen> onCancel, MessageFunction onType, Validation<String> validation) {
        super(title, placeholder, run, onDone, onCancel, validation);

        this.onType = onType;
    }

    @Override
    protected void init() {
        super.init();

        label = addLabel("");

        input.setResponder(s -> {
            this.label.setMessage(onType.onType(s));
        });
    }

    public static Build open(String title, String placeholder) {
        return new Build(Component.literal(title), placeholder);
    }

    public static Build open(Component title, String placeholder) {
        return new Build(title, placeholder);
    }

    @FunctionalInterface
    public interface MessageFunction {
        Component onType(String input);
    }

    public static class Build extends TextInsertScreen.Build {
        private MessageFunction msgFunc;

        private Build(Component component, String placeholder) {
            super(component, placeholder);
        }

        public Build onType(MessageFunction function) {
            this.msgFunc = function;
            return this;
        }

        @Override
        public void execute() {
            Minecraft.getInstance().setScreen(new InputWithMessageScreen(component, placeholder, run, done, cancel, msgFunc, validate));
        }
    }
}

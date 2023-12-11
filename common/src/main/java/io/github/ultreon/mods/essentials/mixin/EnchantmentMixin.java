package io.github.ultreon.mods.essentials.mixin;

import io.github.ultreon.mods.essentials.util.RomanNumber;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Shadow
    public abstract int getMaxLevel();

    @Shadow
    public abstract boolean isCurse();

    @Shadow
    public abstract String getDescriptionId();

    /**
     * @author XyperCode
     * @reason Fixing roman number requires @Overwrite. Because of a change in a line that is not modifiable in return, and cannot be accessed.
     */
    @Overwrite
    public Component getFullname(int level) {
        MutableComponent name = Component.translatable(this.getDescriptionId());
        if (this.isCurse()) {
            name.withStyle(ChatFormatting.RED);
        } else {
            name.withStyle(ChatFormatting.GRAY);
        }

        if (level != 1 || this.getMaxLevel() != 1) {
            name.append(" ").append(Component.literal(RomanNumber.toRoman(level)));
        }

        return name;
    }
}

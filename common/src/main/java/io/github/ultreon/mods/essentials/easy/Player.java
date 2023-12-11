package io.github.ultreon.mods.essentials.easy;

import net.minecraft.world.level.GameType;

public interface Player {
    float getHealth();

    float getMaxHealth();

    float getMovementSpeed();

    float getFlySpeed();

    GameType getGameMode();
}

package io.github.ultreon.mods.essentials.easy;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface BlockState {
    Block getBlock();

    boolean canEntitySpawn(Level level, BlockPos blockPos, EntityType<?> entityType);

    boolean getOpacity(Level level, BlockPos blockPos);

    boolean isTransparent();

    boolean getHardness(Level level, BlockPos blockPos);

    boolean getHardness(Level level, BlockPos blockPos, Player player);
}

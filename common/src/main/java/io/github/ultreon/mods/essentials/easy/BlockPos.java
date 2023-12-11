package io.github.ultreon.mods.essentials.easy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockPos {
    int x;
    int y;
    int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}

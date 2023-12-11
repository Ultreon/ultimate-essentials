package io.github.ultreon.mods.essentials.data;

import io.github.ultreon.mods.essentials.UEssentials;
import io.github.ultreon.mods.essentials.shop.ServerShop;
import io.github.ultreon.mods.essentials.user.ServerUser;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileExistsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@UtilityClass
public final class DataManager {
    public static void save() throws IOException {
        File uEssentialsData = UEssentials.getDataFolder();
        if (uEssentialsData.exists()) {
            if (uEssentialsData.isFile()) {
                throw new FileExistsException("Expected directory, got a file: " + uEssentialsData.getAbsolutePath());
            }
        } else if (!uEssentialsData.mkdirs()) {
            throw new FileNotFoundException("Couldn't make directories: " + uEssentialsData.getAbsolutePath());
        }

        ServerUser.saveAll();
        UEssentials.get().save();
        ServerShop.save();
    }
}

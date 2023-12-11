package io.github.ultreon.mods.essentials.user;

import com.mojang.authlib.GameProfile;
import io.github.ultreon.mods.essentials.util.Named;
import io.github.ultreon.mods.essentials.util.PermissionContainer;
import io.github.ultreon.mods.essentials.util.UniqueObject;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public abstract class User implements UniqueObject, Named, PermissionContainer, io.github.ultreon.mods.essentials.util.Kickable, io.github.ultreon.mods.essentials.util.Tickable, io.github.ultreon.mods.essentials.util.MoneyContainer {
    private static final int EXCHANGE_MODIFIER = 4;
    protected final UUID id;
    protected float balance;

    public User(UUID id) {
        this.id = id;
    }

    @Override
    public void tick() {

    }

    public abstract GameProfile getProfile();

    @Override
    public final String getName() {
        return getProfile().getName();
    }

    @Override
    public final UUID uuid() {
        return getProfile().getId();
    }

    @Override
    public void kick(String reason) {

    }

    @Override
    public void kick(Component reason) {

    }

    public void fullHeal() {

    }

    public void fullFeed() {

    }

    public void showError(String title, String description) {

    }

    public void showError(Component title, Component description) {

    }

    @Override
    public boolean hasPermission(Permission perm) {
        return false;
    }

    public boolean hasOwnPermission(Permission perm) {
        return false;
    }

    public boolean hasRolePermission(Permission perm) {
        return false;
    }

    @Override
    public void addPermission(Permission perm) {

    }

    @Override
    public void removePermission(Permission perm) {

    }

    public void setPermission(Permission perm, boolean enable) {
        if (enable) addPermission(perm);
        else removePermission(perm);
    }

    public boolean canChangePermission(PermissionContainer target, Permission permission) {
        if (isRoot()) {
            return true;
        }

        if (permission.equals(Permissions.MASTER)) return false;
        if (this.hasPermission(Permissions.MASTER)) return true;

        if (target == this) return false;

        if (permission.isChildOf(Permissions.CHANGE_PERMISSIONS)) return false;
        if (permission.equals(Permissions.CHANGE_PERMISSIONS)) return false;
        if (this.hasPermission(Permissions.CHANGE_USER_PERMISSIONS)) return hasPermission(permission);
        return false;
    }

    protected abstract boolean isRoot();

    protected abstract boolean isAdmin();

    public abstract Set<Permission> getEnabledPermissions();

    @Override
    public double getBalance() {
        return balance;
    }

    public abstract boolean isModerator();

    public static int getXpNeededForNextLevel(int experienceLevel) {
        if (experienceLevel >= 30) {
            return 112 + (experienceLevel - 30) * 9;
        } else {
            return experienceLevel >= 15 ? 37 + (experienceLevel - 15) * 5 : 7 + experienceLevel * 2;
        }
    }

    @Nullable
    public static Experience getExpNeededForMoney(Player player, int money) {
        int expToExchange = getExpToExchange(money);
        int cur = player.experienceLevel;
        int left = expToExchange;

        if (getXpNeededForNextLevel(cur) > expToExchange) {
            return new Experience(0, expToExchange);
        } else if (getXpNeededForNextLevel(cur) == expToExchange) {
            return new Experience(1);
        } else {
            int points = 0;
            int levels = 0;

            int cp = Mth.floor(player.experienceProgress * (float) getXpNeededForNextLevel(cur));
            points += cp;

            while (cur >= 0) {
                int needed = getXpNeededForNextLevel(cur);
                if (needed > left) {
                    points += left;
                    return new Experience(levels, points);
                }

                left -= needed;

                levels++;

                if (left == 0) {
                    return new Experience(levels, points);
                }

                cur--;
            }
            return null;
        }
    }

    public static int getExpToExchange(int money) {
        return money * EXCHANGE_MODIFIER;
    }

    public static double getMoneyForExchange(double amount) {
        return amount / EXCHANGE_MODIFIER;
    }

    public static int getTotalExpPoints(Player basePlayer) {
        int level = basePlayer.experienceLevel;
        int points = Mth.floor(basePlayer.experienceProgress * (float) getXpNeededForNextLevel(level));
        level--;
        while (level >= 0) {
            int leveledPoints = Mth.floor(getXpNeededForNextLevel(level));
            points += leveledPoints;
            level--;
        }

        return points;
    }

    @Getter
    public static class Experience implements Serializable {
        @Serial
        private static final long serialVersionUID = 0x542b1e2a282e9ef1L;

        private final int levels;
        private final int points;

        public Experience(int levels, int points) {
            this.levels = levels;
            this.points = points;
        }

        public Experience(int levels) {
            this(levels, 0);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Experience that = (Experience) o;
            return levels == that.levels && points == that.points;
        }

        @Override
        public int hashCode() {
            return Objects.hash(levels, points);
        }
    }
}

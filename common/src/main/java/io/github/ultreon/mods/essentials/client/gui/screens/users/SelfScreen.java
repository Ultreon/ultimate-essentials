package io.github.ultreon.mods.essentials.client.gui.screens.users;

import com.ultreon.mods.lib.client.gui.screen.TitleStyle;
import com.ultreon.mods.lib.client.gui.widget.BaseButton;
import com.ultreon.mods.lib.client.gui.widget.Label;
import io.github.ultreon.mods.essentials.client.gui.screens.InputWithMessageScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.UEssentialsMenuScreen;
import io.github.ultreon.mods.essentials.client.gui.screens.users.cheats.UserCheatsScreen;
import io.github.ultreon.mods.essentials.network.Networking;
import io.github.ultreon.mods.essentials.network.economy.ExchangeExpPacket;
import io.github.ultreon.mods.essentials.user.LocalUser;
import io.github.ultreon.mods.essentials.user.User;
import io.github.ultreon.mods.essentials.util.MoneyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class SelfScreen extends UEssentialsMenuScreen {
    private final LocalUser user = LocalUser.get();
    private final Label experience;
    private final Label experienceTot;
    private final Label balance;

    public SelfScreen() {
        super(new Properties().titleStyle(TitleStyle.DETACHED).titleText("Options for You"));
        addUserRow(LocalUser.get());

        experience = addLabel();
        experienceTot = addLabel();
        balance = addLabel();

        addButtonRow(Component.literal("Cheats"), this::openCheatsScreen, Component.literal("Exchange"), this::openExchangeScreen);

    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void onPreRender() {
        experience.setMessage("Exp: lvl " + user.getExpLevel() + " (" + user.getExpPoints() + "/" + user.getExpNeededForLevelUp() + ")");
        experienceTot.setMessage("Exp: total " + user.getTotalExpPoints());
        balance.setMessage("Balance: " + MoneyUtils.format(LocalUser.get().getBalance()));

        super.onPreRender();
    }

    private void openExchangeScreen(BaseButton btn) {
        InputWithMessageScreen.open("Amount of money to get.", "exp").onType(
            (s) -> {
                int i;
                try {
                    i = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    return Component.literal("Invalid amount of money!");
                }

                LocalUser user = LocalUser.get();
                LocalPlayer player = user.player();
                if (player == null) {
                    return Component.literal("You don't exist").withStyle(ChatFormatting.RED);
                }
                int expToExchange = User.getExpToExchange(i);
                MutableComponent msg = Component.literal("Costs " + expToExchange + " exp points");
                if (user.canExchangeExp(expToExchange)) {
                    return msg.withStyle(ChatFormatting.GREEN);
                } else {
                    return msg.withStyle(ChatFormatting.RED);
                }
//                User.Experience exp = User.getExpNeededForMoney(player, i);
//                if (!user.canExchangeExp(expToExchange)) {
//                    Supplier<User.Experience> supplier = () -> {
//                        int left = i;
//
//                        int points = 0;
//                        int levels = 0;
//
//                        int cur = player.experienceLevel;
//
//                        int cp = Mth.floor(player.experienceProgress * (float) getXpNeededForNextLevel(cur));
//                        points += cp;
//
//                        if (points > i) {
//                            return new User.Experience(0, left);
//                        }
//
//                        while (true) {
//                            int needed = getXpNeededForNextLevel(cur);
//                            if (needed > left) {
//                                points += left;
//                                return new User.Experience(levels, points);
//                            }
//
//                            left -= needed;
//
//                            levels++;
//
//                            if (left == 0) {
//                                return new User.Experience(levels, points);
//                            }
//
//                            cur++;
//                        }
//                    };
//
//                    User.Experience xp = supplier.get();
//
//                    return new Component("You need " + xp.getLevels() + " lvl, and " + xp.getPoints() + " points more").withStyle(ChatFormatting.RED);
//                }
            }
        ).validate(s -> {
            try {
                return LocalUser.get().canExchangeExp(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return false;
            }
        }).done((textInsertScreen, s) -> {
            int i = Integer.parseInt(s);
            if (LocalUser.get().canExchangeExp(i)) {
                Networking.get().sendToServer(new ExchangeExpPacket(i));
            }
        }).execute();
    }

    private void openCheatsScreen(BaseButton btn) {
        UserCheatsScreen.open(LocalUser.get());
    }
}

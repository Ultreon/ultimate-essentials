package io.github.ultreon.mods.essentials.util;

public interface MoneyContainer {
    double getBalance();

    void setBalance(double amount);

    void reduceMoney(double amount);

    void addMoney(double amount);
}

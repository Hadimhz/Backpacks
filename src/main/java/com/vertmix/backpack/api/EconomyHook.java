package com.vertmix.backpack.api;

import org.bukkit.OfflinePlayer;

public interface EconomyHook {

    void setBalance(OfflinePlayer player, double value);

    double getBalance(OfflinePlayer player);

    default void addBalance(OfflinePlayer player, double value) {
        setBalance(player, getBalance(player) + value);
    }

    default void removeBalance(OfflinePlayer player, double value) {
        setBalance(player, Math.max(getBalance(player) - value, 0));
    }


}

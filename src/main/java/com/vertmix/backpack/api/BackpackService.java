package com.vertmix.backpack.api;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public interface BackpackService {
    double sell(@NotNull Backpack backpack);

    double sell(@NotNull OfflinePlayer player, @NotNull Backpack backpack);

    int getSpace(int level);
}

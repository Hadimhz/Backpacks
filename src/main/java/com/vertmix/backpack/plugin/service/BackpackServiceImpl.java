package com.vertmix.backpack.plugin.service;

import com.vertmix.backpack.api.Backpack;
import com.vertmix.backpack.api.BackpackService;
import com.vertmix.backpack.api.EconomyHook;
import com.vertmix.backpack.api.config.Config;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class BackpackServiceImpl implements BackpackService {

    private final Config config;
    private final EconomyHook economy;

    public BackpackServiceImpl(Config config, EconomyHook economy) {
        this.config = config;
        this.economy = economy;
    }

    @Override
    public double sell(@NotNull Backpack backpack) {
        final double value = backpack.get().entrySet().stream().mapToDouble(entries -> (config.prices.getOrDefault(entries.getKey(), .0) * entries.getValue())).sum();
        backpack.get().clear();

        return value;
    }

    @Override
    public double sell(@NotNull OfflinePlayer player, @NotNull Backpack backpack) {
        final double value = sell(backpack);

        this.economy.addBalance(player, value);

        return value;
    }

    @Override
    public int getSpace(int level) {
        return level * config.increaseSize;
    }

}

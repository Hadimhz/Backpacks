package com.vertmix.backpack.plugin.service;

import com.vertmix.backpack.api.EconomyHook;
import com.vertmix.backpack.api.exception.EconomyException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyHookImpl implements EconomyHook {

    private final Economy econ;

    public EconomyHookImpl() {

        if (Bukkit.getPluginManager().getPlugin("Vault") == null)
            throw new EconomyException("Vault plugin not detected. Add vault to your plugins directory.");


        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            throw new EconomyException("Vault Economy Hook detected. Add an economy plugin (like Essentials) to your plugins directory.");

        econ = rsp.getProvider();

    }

    @Override
    public void setBalance(OfflinePlayer player, double value) {
        double balance = getBalance(player);

        if (balance > value) removeBalance(player, balance - value);
        else if (balance < value) addBalance(player, value - balance);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return econ.getBalance(player);
    }

    @Override
    public void addBalance(OfflinePlayer player, double value) {
        econ.depositPlayer(player, value);
    }

    @Override
    public void removeBalance(OfflinePlayer player, double value) {
        econ.withdrawPlayer(player, Math.max(value, 0));
    }
}

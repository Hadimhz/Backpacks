package com.vertmix.backpack.plugin.placeholder;

import com.vertmix.backpack.api.Backpack;
import com.vertmix.backpack.api.registry.BackpackRegistry;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PAPIBackpack extends PlaceholderExpansion {

    private final BackpackRegistry registry;

    public PAPIBackpack(BackpackRegistry registry) { this.registry = registry; }

    @Override
    public @NotNull String getIdentifier() {
        return "backpack";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Backpack";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        Optional<Backpack> backpack = registry.get(player.getUniqueId());

        if (backpack.isEmpty()) return null;


        if (params.equalsIgnoreCase("capacity")) {
            return backpack.get().getCapacity() + "";
        }

        if (params.equalsIgnoreCase("holding")) {
            return backpack.get().total() + "";
        }

        if (params.equalsIgnoreCase("level")) {
            return backpack.get().getLevel() + "";
        }

        return null;
    }


}

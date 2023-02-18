package com.vertmix.backpack.plugin.entities;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vertmix.backpack.api.Backpack;
import com.vertmix.backpack.plugin.registry.CustomRegistryImpl;
import me.lucko.helper.Schedulers;
import me.lucko.helper.gson.JsonBuilder;
import me.lucko.helper.scheduler.Task;
import me.lucko.helper.text3.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Map;
import java.util.UUID;

public class BackpackImpl extends CustomRegistryImpl<Material, Integer> implements Backpack {

    private static final NumberFormat formatter = NumberFormat.getInstance();
    private static final Gson gson = new Gson();
    private final UUID uuid;
    private int level;
    private Task task;
    private boolean autoSell;

    public BackpackImpl(UUID uuid, int level, boolean autoSell, Map<Material, Integer> content) {
        this.level = level;
        this.uuid = uuid;
        get().putAll(content);

        setAutoSell(autoSell);
    }

    public BackpackImpl(UUID uuid, int level) {
        this.level = level;
        this.uuid = uuid;

        setAutoSell(true);
    }

    @Override
    public boolean isAutoSell() {
        return autoSell;
    }

    @Override
    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;

        if (autoSell)
            this.task = Schedulers.async().runRepeating(() -> {
                double price = sell();

                if (price == 0) return;

                OfflinePlayer player = Bukkit.getPlayer(uuid);

                assert player != null;
                if (player.isOnline())
                    player.getPlayer().sendMessage(Text.colorize("&7[&dAUTO-SELL&7] &aSold &6$" + formatter.format(price) + " &aworth of blocks!"));
            }, 30 * 20L, 30 * 20L);
        else task.close();
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @NotNull
    @Override
    public JsonElement serialize() {
        return JsonBuilder.object()
                .add("uuid", this.uuid.toString())
                .add("level", this.level)
                .add("auto-sell", this.autoSell)
                .add("content", gson.toJsonTree(get()))
                .build();
    }
}

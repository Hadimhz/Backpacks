package com.vertmix.backpack.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vertmix.backpack.api.registry.CustomRegistry;
import com.vertmix.backpack.plugin.entities.BackpackImpl;
import me.lucko.helper.Services;
import me.lucko.helper.gson.GsonSerializable;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public interface Backpack extends CustomRegistry<Material, Integer>, GsonSerializable {

    static @NotNull Backpack deserialize(JsonElement element) {
        Type gsonType = new TypeToken<Map<Material, Integer>>() {}.getType();

        Gson gson = new Gson();

        final JsonObject object = element.getAsJsonObject();

        final UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        final int level = object.get("level").getAsInt();
        final boolean autoSell = object.get("auto-sell").getAsBoolean();

        final Map<Material, Integer> content = gson.fromJson(object.get("content"), gsonType);

        return new BackpackImpl(uuid, level, autoSell, content);
    }

    int getLevel();

    void setLevel(int level);

    boolean isAutoSell();

    void setAutoSell(boolean state);

    default void upgrade() {
        setLevel(getLevel() + 1);
    }

    default int total() {
        return values().stream().mapToInt(x -> x).sum();
    }

    default void add(@NotNull Material material, int amount) {
        set(material, Math.min(get().getOrDefault(material, 0) + amount, Services.load(BackpackService.class).getSpace(getLevel())));
    }

    default void add(@NotNull Material material) {
        add(material, 1);
    }

    default double sell() {
        return Services.load(BackpackService.class).sell(this);
    }

}

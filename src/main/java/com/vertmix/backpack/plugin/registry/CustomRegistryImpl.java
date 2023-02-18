package com.vertmix.backpack.plugin.registry;

import com.vertmix.backpack.api.registry.CustomRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CustomRegistryImpl<V, T> implements CustomRegistry<V, T> {

    private final Map<V, T> collection = new LinkedHashMap<>();

    @Override
    public void add(@NotNull V key, @NotNull T value) {
        this.collection.put(key, value);
    }

    @Override
    public void remove(@NotNull V key) {
        this.collection.remove(key);
    }

    @Override
    public @NotNull Map<V, T> get() {
        return this.collection;
    }

    @Override
    public void set(V v, T t) {
        this.collection.put(v, t);
    }

    @Override
    public @NotNull Optional<T> get(@NotNull V key) {
        return Optional.ofNullable(this.collection.get(key));
    }

    @Override
    public @NotNull Collection<V> keys() {
        return this.collection.keySet();
    }

    @Override
    public @NotNull Collection<T> values() {
        return this.collection.values();
    }
}

package com.vertmix.backpack.api.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface CustomRegistry<V, T> {

    void add(@NotNull V key, @NotNull T value);

    void remove(@NotNull V key);

    @NotNull Map<V, T> get();

    void set(V v, T t);

    @NotNull Optional<T> get(@NotNull V key);

    @NotNull Collection<T> values();

    @NotNull Collection<V> keys();
}

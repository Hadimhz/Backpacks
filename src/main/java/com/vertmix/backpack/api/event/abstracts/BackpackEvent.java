package com.vertmix.backpack.api.event.abstracts;

import com.vertmix.backpack.api.Backpack;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class BackpackEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Backpack backpack;

    public BackpackEvent(Backpack backpack) {
        this(backpack, false);
    }

    public BackpackEvent(Backpack backpack, boolean async) {
        super(async);
        this.backpack = backpack;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}

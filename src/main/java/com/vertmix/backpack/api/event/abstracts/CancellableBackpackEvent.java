package com.vertmix.backpack.api.event.abstracts;

import com.vertmix.backpack.api.Backpack;
import org.bukkit.event.Cancellable;

public abstract class CancellableBackpackEvent extends BackpackEvent implements Cancellable {
    private boolean cancelled;

    public CancellableBackpackEvent(Backpack backpack, boolean async) {
        super(backpack, async);
        this.cancelled = false;
    }

    public CancellableBackpackEvent(Backpack backpack) {
        this(backpack, false);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}

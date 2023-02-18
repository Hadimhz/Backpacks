package com.vertmix.backpack.api.event;

import com.vertmix.backpack.api.Backpack;
import com.vertmix.backpack.api.event.abstracts.BackpackEvent;
import org.bukkit.Material;

public class BackpackAddEvent extends BackpackEvent {
    private final Material material;
    private int amount;

    public BackpackAddEvent(Backpack backpack, Material material, int amount) {
        super(backpack);
        this.material = material;
        this.amount = amount;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

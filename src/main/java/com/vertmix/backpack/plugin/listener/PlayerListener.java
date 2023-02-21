package com.vertmix.backpack.plugin.listener;

import com.vertmix.backpack.api.registry.BackpackRegistry;
import com.vertmix.backpack.plugin.entities.BackpackImpl;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements TerminableModule {

    private final BackpackRegistry registry;

    public PlayerListener(BackpackRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void setup(@NotNull TerminableConsumer consumer) {


        Events.subscribe(PlayerJoinEvent.class).handler(event -> {

            // Register a backpack if the player's missing one
            if (registry.get(event.getPlayer().getUniqueId()).isEmpty())
                registry.set(event.getPlayer().getUniqueId(), new BackpackImpl(event.getPlayer().getUniqueId(), 1));

        }).bindWith(consumer);

    }
}

package com.vertmix.backpack.plugin;

import co.aikar.commands.PaperCommandManager;
import com.google.common.reflect.TypeToken;
import com.vertmix.backpack.api.Backpack;
import com.vertmix.backpack.api.BackpackService;
import com.vertmix.backpack.api.EconomyHook;
import com.vertmix.backpack.api.config.Config;
import com.vertmix.backpack.api.registry.BackpackRegistry;
import com.vertmix.backpack.plugin.command.ACFBackpackCommand;
import com.vertmix.backpack.plugin.listener.PlayerListener;
import com.vertmix.backpack.plugin.registry.BackpackRegistryImpl;
import com.vertmix.backpack.plugin.service.BackpackServiceImpl;
import com.vertmix.backpack.plugin.service.EconomyHookImpl;
import com.vertmix.config.ConfigRegistry;
import com.vertmix.config.json.JsonConfigRegistry;
import me.lucko.helper.Services;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.serialize.GsonStorageHandler;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class BackpackPlugin extends ExtendedJavaPlugin {

    private final GsonStorageHandler<Map<UUID, Backpack>> backpackStorage = new GsonStorageHandler<>("backpacks", ".json", getDataFolder(), new TypeToken<>() {});

    @Override
    public void enable() {

        getDataFolder().mkdirs();

        final ConfigRegistry configRegistry = new JsonConfigRegistry();

        final Config config = configRegistry.register(Config.class, new Config(), new File(getDataFolder(), "config.json"));

        final EconomyHook economyHook = provideService(EconomyHook.class, new EconomyHookImpl());

        final BackpackService backpackService = provideService(BackpackService.class, new BackpackServiceImpl(config, economyHook));

        final BackpackRegistry backpackRegistry = provideService(BackpackRegistry.class, new BackpackRegistryImpl());

        provideService(Config.class, config);

        final PaperCommandManager commandService = provideService(PaperCommandManager.class, new PaperCommandManager(this));

        commandService.enableUnstableAPI("help");

        commandService.registerCommand(new ACFBackpackCommand(backpackRegistry, backpackService));

        bindModule(new PlayerListener(backpackRegistry));

        backpackStorage.load().ifPresent(data -> data.forEach(backpackRegistry::set));

    }


    @Override
    protected void disable() {

        getLogger().info("Disabling auto-save.");

        getLogger().info("Saving Player Registry data.");
        BackpackRegistry registry = Services.load(BackpackRegistry.class);

        backpackStorage.save(registry.get());

    }


}

package com.vertmix.backpack.plugin.menu;

import com.google.common.collect.ImmutableList;
import com.vertmix.backpack.api.Backpack;
import com.vertmix.backpack.api.EconomyHook;
import com.vertmix.backpack.api.config.Config;
import com.vertmix.backpack.api.registry.BackpackRegistry;
import com.vertmix.backpack.plugin.entities.BackpackImpl;
import me.lucko.helper.Services;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.text3.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class BackpackMenu extends Gui {

    private static final NumberFormat formatter = NumberFormat.getInstance();
    private static final MenuScheme UPGRADE_SLOTS = new MenuScheme().maskEmpty(1)
            .mask("011111110")
            .mask("011111110");

    private static final MenuScheme BUTTON_SLOTS = new MenuScheme().maskEmpty(4)
            .mask("001000100");

    private final Backpack backpack;
    private final Config config;
    private final EconomyHook economy;

    public BackpackMenu(Player player, BackpackRegistry regsRegistry) {
        super(player, 6, "&6&lUpgrade carrier slave");

        this.config = Services.load(Config.class);
        this.economy = Services.load(EconomyHook.class);
        this.backpack = regsRegistry.get(player.getUniqueId()).orElse(new BackpackImpl(getPlayer().getUniqueId(), 1));
    }

    @Override
    public void redraw() {

        clearItems();
        fillWith(ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name("&7").buildItem().build());

        int usableSize = UPGRADE_SLOTS.getMaskedIndexes().size();
        int page = Math.floorDiv(backpack.getLevel(), usableSize);

        MenuPopulator upgradePopulator = UPGRADE_SLOTS.newPopulator(this);
        MenuPopulator buttonPopulator = BUTTON_SLOTS.newPopulator(this);

        for (int i = page * usableSize; i < (page * usableSize) + usableSize; i++) {
            upgradePopulator.accept(getLevelItem(i + 1));
        }

        buttonPopulator.accept(ItemStackBuilder.of(Material.PAPER).name("&aSell Contents").build(() -> {
            double price = backpack.sell();

            getPlayer().sendMessage(Text.colorize("&7[&dBackpack&7] &aSold &6$" + formatter.format(price) + " &aworth of blocks!"));
        }));

        buttonPopulator.accept(ItemStackBuilder.of(Material.ANVIL).name((backpack.isAutoSell() ? "&a" : "&c") + "Toggle AutoSell").build(() -> {
            backpack.setAutoSell(!backpack.isAutoSell());

            redraw();
        }));


    }


    private Item getLevelItem(int level) {

        if (level > config.maxLevel)
            return ItemStackBuilder.of(Material.RED_CONCRETE)
                    .name("&cMAX LEVEL")
                    .buildItem().build();
        else if (backpack.getLevel() >= level)
            return ItemStackBuilder.of(Material.GRAY_STAINED_GLASS)
                    .name("&cBackpack Level " + level)
                    .lore(ImmutableList.of(
                            "&7",
                            "&aPrice: &e$&6" + (config.increasePrice * level),
                            "&aCapacity: &d" + (config.increaseSize * level),
                            "&7",
                            "&c&l-> PURCHASED"
                    ))
                    .buildItem().build();
        else if (level == (backpack.getLevel() + 1))
            return ItemStackBuilder.of(Material.ORANGE_STAINED_GLASS_PANE)
                    .name("&aBackpack Level " + level)
                    .lore(ImmutableList.of(
                            "&7",
                            "&aPrice: &e$&6" + (config.increasePrice * level),
                            "&aCapacity: &d" + (config.increaseSize * level),
                            "&7",
                            "&a&o(Click to unlock)"
                    ))
                    .build(() -> {

                        if (economy.getBalance(getPlayer()) < (config.increasePrice * level)) {
                            getPlayer().sendMessage(Text.colorize("&7[&dBackpack&7] &cInsufficient balance!"));
                            return;
                        }

                        economy.removeBalance(getPlayer(), (config.increasePrice * level));

                        backpack.upgrade();
                        redraw();
                    });

        else
            return ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE)
                    .name("&cBackpack Level " + level)
                    .lore(ImmutableList.of(
                            "&7",
                            "&aPrice: &e$&6" + (config.increasePrice * level),
                            "&aCapacity: &d" + (config.increaseSize * level),
                            "&7",
                            "&7&o(unlock previous levels to unlock this)"
                    ))
                    .buildItem().build();

    }

}

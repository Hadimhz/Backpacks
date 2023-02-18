package com.vertmix.backpack.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.vertmix.backpack.api.Backpack;
import com.vertmix.backpack.api.BackpackService;
import com.vertmix.backpack.api.registry.BackpackRegistry;
import com.vertmix.backpack.plugin.menu.BackpackMenu;
import me.lucko.helper.text3.Text;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

@CommandAlias("backpack|backpacks")
public class ACFBackpackCommand extends BaseCommand {

    private static final NumberFormat formatter = NumberFormat.getInstance();
    private final BackpackRegistry registry;
    private final BackpackService backpackService;

    public ACFBackpackCommand(BackpackRegistry registry, BackpackService backpackService) {
        this.backpackService = backpackService;
        this.registry = registry;
    }

    @HelpCommand
    @Default
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("sell")
    @Description("Sells the content of the backpack.")
    public void onSell(CommandSender sender, @Optional OfflinePlayer target) {

        OfflinePlayer player;

        if ((target == null || !target.hasPlayedBefore()) && sender instanceof Player)
            player = ((Player) sender);
        else if (!(sender instanceof Player)) {
            sender.sendMessage(Text.colorize("&7[&dBackpack&7] &aCould not find a player with that name!"));
            return;
        } else
            player = target;


        registry.get(player.getUniqueId()).ifPresent(backpack -> {
            double price = backpack.sell();

            if (player.isOnline())
                player.getPlayer().sendMessage(Text.colorize("&7[&dBackpack&7] &aSold &6$" + formatter.format(price) + " &aworth of blocks!"));
        });

    }

    @Subcommand("open")
    @Description("Open the backpack upgrade menu.")
    public void onOpen(CommandSender sender, @Optional OfflinePlayer target) {
        OfflinePlayer player;

        if ((target == null || !target.hasPlayedBefore()) && sender instanceof Player)
            player = ((Player) sender);
        else if (!(sender instanceof Player)) {
            sender.sendMessage(Text.colorize("&7[&dBackpack&7] &aCould not find a player with that name!"));
            return;
        } else
            player = target;

        if (!player.isOnline()) {
            sender.sendMessage(Text.colorize("&7[&dBackpack&7] &aThat player is currently offline!"));
            return;
        }
        new BackpackMenu(player.getPlayer(), registry).open();
    }

}

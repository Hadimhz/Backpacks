package com.vertmix.backpack.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
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
    public void onSell(Player player) {

        registry.get(player.getUniqueId()).ifPresent(backpack -> {
            double price = backpack.sell();
            player.getPlayer().sendMessage(Text.colorize("&7[&dBackpack&7] &aSold &6$" + formatter.format(price) + " &aworth of blocks!"));
        });

    }

    @Subcommand("open")
    @Description("Open the backpack upgrade menu.")
    public void onOpen(CommandSender sender, @Optional OfflinePlayer target) {

        Player player = null;

        if (target != null && target.hasPlayedBefore() && sender.hasPermission("backpacks.open.others") && target.isOnline())
            player = target.getPlayer();
        else if (!(sender instanceof Player))
            sender.sendMessage(Text.colorize("&7[&dBackpack&7] &aCould not find a player with that name!"));
        else if (!sender.hasPermission("backpacks.open.others"))
            sender.sendMessage(Text.colorize("&7[&dBackpack&7] &cYou do not have permissions to do that."));
        else player = (Player) sender;

        if (player == null) return;

        new BackpackMenu(player, registry).open();


    }

}

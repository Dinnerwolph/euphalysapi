package net.euphalys.core.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author Dinnerwolph
 */

public class InfoCommands extends AbstractCommands implements TabExecutor {

    public InfoCommands() {
        super("info", "euphalys.cmd.info");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 1)
            return false;
        UUID target = api.getUUUIDTranslator().getUUID(args[0]);
        IEuphalysPlayer targetplayer = api.getPlayer(target);
        Inventory inventory = Bukkit.createInventory(player, 9 * 6, args[0] + " sanctions");
        targetplayer.getSanctions().forEach(sanctions -> {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§cSanction n° §a#" + sanctions.getSanctionsId());
            itemMeta.setLore(Arrays.asList("§cType §a: " + sanctions.getType().name, "§cServeur §a: " + sanctions.getServer(), "§cDurée §a: " + sanctions.getDuration() + "", "§cRaison §a: " + sanctions.getMessage()));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        });
        player.openInventory(inventory);
        return true;
    }

    protected void displayHelp() {
        player.sendMessage("/info <player>");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> matches = new ArrayList();
        if (args.length == 1) {
            String search = args[0].toLowerCase(Locale.ROOT);
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.getName().toLowerCase(Locale.ROOT).startsWith(search))
                    matches.add(player.getName());
            });
        }
        return matches;
    }
}

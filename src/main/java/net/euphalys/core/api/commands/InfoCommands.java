package net.euphalys.core.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class InfoCommands extends AbstractCommands {

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
            itemMeta.setDisplayName("#" + sanctions.getSanctionsId());
            itemMeta.setLore(Arrays.asList(sanctions.getType().name, sanctions.getServer(), sanctions.getDuration() + "", sanctions.getMessage()));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        });
        player.openInventory(inventory);
        return true;
    }

    protected void displayHelp() {
        player.sendMessage("/info <player>");
    }
}

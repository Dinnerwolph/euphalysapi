package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class Invcommands extends AbstractCommands {

    public Invcommands() {
        super("inv", "euphalys.cmd.inv");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        if (args.length < 1) return false;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§4Ce joueur n'est pas connecté.");
            return true;
        }
        player.openInventory(target.getInventory());
        System.out.println("§6" player.getDisplayName() + "a ouvert l'inventaire de" + target.getDisplayName());
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("§cUsage : /inv <player>");
    }
}

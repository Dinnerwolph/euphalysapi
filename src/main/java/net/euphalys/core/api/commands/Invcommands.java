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
            player.sendMessage("Ce joueurs n'est pas connecté.");
            return true;
        }
        player.openInventory(target.getInventory());

        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/inv <player>");
    }
}

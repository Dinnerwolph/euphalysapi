package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class CPSCommands extends AbstractCommands {
    //TODO NOT WORKING FUCK YOU CMD
    public CPSCommands() {
        super("cps", "euphalys.cmd.cps");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cErreur : Ce joueur n'est pas connecté.");
            return true;
        }
        player.sendMessage("§6CPS de §e" + target.getLocation() + ":" + api.cps.get(target.getUniqueId()));
        return true;

    }

    @Override
    protected void displayHelp() {
        player.sendMessage("§cUsage :/cps <player>");
    }
}
package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class FreezeCommands extends AbstractCommands {

    public FreezeCommands() {
        super("freeze", "euphalys.cmd.freeze");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        if (args.length < 1) return false;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cErreur : Ce joueur n'est pas connecté.");
            return true;
        }
        if (!api.freezeList.contains(target.getUniqueId()))
            api.freezeList.add(target.getUniqueId());
        else
            api.freezeList.remove(target.getUniqueId());

        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("§cUsage : /freeze <player>");
    }
}

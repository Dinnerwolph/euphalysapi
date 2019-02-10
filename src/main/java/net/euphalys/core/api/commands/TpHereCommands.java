package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author Dinnerwolph
 */

public class TpHereCommands extends AbstractCommands {

    public TpHereCommands() {
        super("tphere", "euphalys.cmd.tphere");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("Ce joueurs n'est pas connecté.");
            return true;
        }
        target.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/tphere <player>");
    }
}

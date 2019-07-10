package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */
public class SALCommands extends AbstractCommands {

    public SALCommands() {
        super("sal", "euphalys.cmd.sal");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUsage : /sal <message>");
            return false;
        } else {
            String message = "";
            for (int i = 0; i < args.length; i++)
                message = message + args[i] + " ";
            Bukkit.broadcastMessage("§3[Annonce Staff] §r" + message.replaceAll("&", "§"));
        }
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("§cUsage : /sal <message>");
    }
}

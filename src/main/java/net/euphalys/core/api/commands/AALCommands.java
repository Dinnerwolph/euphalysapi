package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */
public class AALCommands extends AbstractCommands {

    public AALCommands() {
        super("aal", "euphalys.cmd.aal");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("/aal <message>");
            return false;
        } else {
            String message = "";
            for (int i = 0; i < args.length; i++)
                message = message + args[i] + " ";
            Bukkit.broadcastMessage(message.replaceAll("&", "§"));
        }
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/aal <message>");
    }
}

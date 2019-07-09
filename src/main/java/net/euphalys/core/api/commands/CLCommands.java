package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */
public class CLCommands extends AbstractCommands {

    public CLCommands() {
        super("cl", "euphalys.cmd.cl");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        if (args.length < 2)
            return false;
        String salon;
        if (args[1].equals("modo"))
            salon = "Convocation (Modération)";
        else if (args[1].equals("admin"))
            salon = "Administration";
        else {
            player.sendMessage("choix du salon modo ou admin.");
            return true;
        }
        Bukkit.broadcastMessage("[Convocation] >> " + args[0] + ", tu est convoqué sur Teamspeak (ts.euphalys.net). Merci de te présenter dans le channel \"Attente Staff ➜ " + salon + "\"");
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/cis <pseudo> <channel>");
    }
}

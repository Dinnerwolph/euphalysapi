package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Dinnerwolph
 */
public class CLCommands extends AbstractCommands implements TabExecutor {

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
        Bukkit.broadcastMessage("§e[Convocation] >> §c" + args[0] + "§e, tu est convoqué sur Teamspeak (§cts.euphalys.net§e). Merci de te présenter dans le channel \"§cAttente Staff ➜ " + salon + "§e\"");
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/cis <pseudo> <channel>");
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
        } else if (args.length == 2) {
            String search = args[1].toLowerCase(Locale.ROOT);
            if ("modo".toLowerCase(Locale.ROOT).startsWith(search))
                matches.add("modo");
            if ("admin".toLowerCase(Locale.ROOT).startsWith(search))
                matches.add("admin");
        }
        return matches;
    }
}

package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Dinnerwolph
 */

public class TpHereCommands extends AbstractCommands implements TabExecutor {

    public TpHereCommands() {
        super("tphere", "euphalys.cmd.tphere");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cErreur : ce joueur n'est pas connecté.");
            return true;
        }
        target.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/tphere <player>");
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
        }
        return matches;
    }
}

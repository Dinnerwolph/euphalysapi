package net.euphalys.core.api.commands;

import net.euphalys.api.sanctions.SanctionsType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class BanCommands extends AbstractCommands {

    public BanCommands() {
        super("ban", "euphalys.cmd.ban");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        if (args.length < 2)
            return false;
        UUID target = api.getUUUIDTranslator().getUUID(args[0]);
        String message = "";
        for (int i = 1; i < args.length; i++)
            message = message + args[i] + " ";

        api.getSanctionsManager().addsanction(api.getPlayer(target), SanctionsType.BAN, 0, message, api.getPlayer(player.getUniqueId()));
        Player targetp = Bukkit.getPlayer(target);
        if(targetp != null)
            targetp.kickPlayer("Vous êtes bannis");
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/ban <player> <message>");
    }
}

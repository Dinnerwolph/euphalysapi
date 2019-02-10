package net.euphalys.core.api.commands;

import net.euphalys.api.sanctions.SanctionsType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class GBanCommands extends AbstractCommands {

    public GBanCommands() {
        super("gban", "euphalys.cmd.gban");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 2)
            return false;
        UUID target = api.getUUUIDTranslator().getUUID(args[0]);
        String message = "";
        for (int i = 1; i < args.length; i++)
            message = message + args[i] + " ";

        api.getSanctionsManager().addGlobalSanction(api.getPlayer(target), SanctionsType.BAN, 0, message, api.getPlayer(player.getUniqueId()));
        Player targetp = Bukkit.getPlayer(target);
        if(targetp != null)
            targetp.kickPlayer("Vous êtes bannis");
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/gban <player> <message>");
    }
}

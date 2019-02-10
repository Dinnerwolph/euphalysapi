package net.euphalys.core.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class WarnCommands extends AbstractCommands {

    public WarnCommands() {
        super("warn", "euphalys.cmd.warn");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        IEuphalysPlayer azoPlayer = api.getPlayer(player.getUniqueId());
        if (args.length < 2)
            return false;
        UUID target = api.getUUUIDTranslator().getUUID(args[0]);
        String message = "";
        for (int i = 1; i < args.length; i++)
            message = message + args[i] + " ";

        api.getSanctionsManager().addGlobalSanction(api.getPlayer(target), SanctionsType.WARN, 0, message, azoPlayer);
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/warn <player> <message>");
    }
}

package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class WarnCommands extends AbstractSanctions {

    public WarnCommands() {
        super("warn", "euphalys.cmd.warn");
    }

    @Override
    boolean onCommand(IEuphalysPlayer player, String args, String message) {
        UUID target = Euphalys.getInstance().getUUUIDTranslator().getUUID(args);
        Euphalys.getInstance().getSanctionsManager().addGlobalSanction(Euphalys.getInstance().getPlayer(target), SanctionsType.WARN, 0, message, player);
        return true;
    }

    @Override
    protected void displayHelp() {
        sendMessage("/warn <player> <message>");
    }
}

package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class MuteCommands extends AbstractSanctions {


    public MuteCommands() {
        super("mute", "epycube.cmd.mute");
    }

    @Override
    boolean onCommand(IEuphalysPlayer player, String args, String message) {
        UUID target = Euphalys.getInstance().getUUUIDTranslator().getUUID(args);
        Euphalys.getInstance().getSanctionsManager().addGlobalSanction(Euphalys.getInstance().getPlayer(target), SanctionsType.MUTE, 0, message, player);
        ProxiedPlayer proxiedPlayer = Euphalys.getInstance().getProxy().getPlayer(target);
        sendMessage(proxiedPlayer, "Vous êtes mute, vous ne pouvez plus parler.");
        return true;
    }

    @Override
    void displayHelp() {
        sendMessage("/mute <player> <message>");
    }
}

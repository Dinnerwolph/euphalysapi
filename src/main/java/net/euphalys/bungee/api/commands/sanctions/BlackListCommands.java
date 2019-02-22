package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class BlackListCommands extends AbstractSanctions {

    public BlackListCommands() {
        super("blacklist", "euphalys.cmd.blacklist");
    }

    @Override
    boolean onCommand(IEuphalysPlayer player, String args, String message) {
        UUID target = Euphalys.getInstance().getUUUIDTranslator().getUUID(args);
        Euphalys.getInstance().getSanctionsManager().addGlobalSanction(Euphalys.getInstance().getPlayer(target), SanctionsType.BLACKLIST, 0, message, player);
        ProxiedPlayer targetp = Euphalys.getInstance().getProxy().getPlayer(target);
        if (targetp != null)
            targetp.disconnect(new TextComponent("Vous êtes blacklist."));
        return true;
    }

    @Override
    void displayHelp() {
        sendMessage("/blacklist <player> <message>");
    }
}

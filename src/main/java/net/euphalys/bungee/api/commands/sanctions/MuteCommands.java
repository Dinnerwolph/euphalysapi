package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author Dinnerwolph
 */

public class MuteCommands extends AbstractSanctions {


    public MuteCommands() {
        super("gmute", "euphalys.cmd.gmute", SanctionsType.MUTE);
    }

    @Override
    boolean onCommand(CommandSender sender, String target, String message) {
        addGlobalSanction(target, message, getUserId());
        ProxiedPlayer proxiedPlayer = Euphalys.getInstance().getProxy().getPlayer(target);
        sendMessage(proxiedPlayer, "Vous êtes mute, vous ne pouvez plus parler.");
        sendMessage("wesh poto t'as mute " + target);
        return true;
    }

    @Override
    void displayHelp() {
        sendMessage("/gmute <player> <message>");
    }
}

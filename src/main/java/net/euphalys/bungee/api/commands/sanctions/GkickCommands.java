package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author Dinnerwolph
 */
public class GkickCommands extends AbstractSanctions {

    public GkickCommands() {
        super("gkick", "euphalys.cmd.gkick", SanctionsType.NONE);
    }

    @Override
    boolean onCommand(CommandSender sender, String target, String message) {
        Euphalys.getInstance().getProxy().getPlayer(target).disconnect(new TextComponent(message));
        sendMessage("wesh poto t'as kick " + target);
        return false;
    }

    @Override
    void displayHelp() {
        sendMessage("/gkick <player> <message>");
    }
}

package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author Dinnerwolph
 */
public class GkickCommands extends AbstractSanctions {

    public GkickCommands() {
        super("gkick", "euphalys.cmd.gkick");
    }

    @Override
    boolean onCommand(IEuphalysPlayer player, String playerName, String message) {
        Euphalys.getInstance().getProxy().getPlayer(player.getUUID()).disconnect(new TextComponent(message));
        sendMessage("wesh poto t'as kick " + playerName);
        return false;
    }

    @Override
    void displayHelp() {
        sendMessage("/gkick <player> <message>");
    }
}

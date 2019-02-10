package net.euphalys.core.api.commands.chat;

import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.commands.AbstractCommands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class OpenChatCommands extends AbstractCommands {

    public OpenChatCommands() {
        super("openchat", "euphalys.cmd.openchat");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        EuphalysApi.getInstance().hasChat = true;
        player.sendMessage("Le chat est maintenant ouvert.");
        Bukkit.broadcastMessage(player.getName() + " viens d'ouvrir le chat. :)");
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

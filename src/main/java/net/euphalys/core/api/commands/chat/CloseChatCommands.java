package net.euphalys.core.api.commands.chat;

import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.commands.AbstractCommands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class CloseChatCommands extends AbstractCommands {
    public CloseChatCommands() {
        super("closechat", "euphalys.cmd.closechat");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        EuphalysApi.getInstance().hasChat = false;
        player.sendMessage("Le chat est maintenant fermé.");
        Bukkit.broadcastMessage(player.getName() + " viens de fermer le chat. :(");
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

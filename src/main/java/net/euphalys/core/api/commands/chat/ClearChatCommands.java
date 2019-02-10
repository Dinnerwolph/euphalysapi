package net.euphalys.core.api.commands.chat;

import net.euphalys.core.api.commands.AbstractCommands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class ClearChatCommands extends AbstractCommands {

    public ClearChatCommands() {
        super("clearchat", "euphalys.cmd.clearchat");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        for (int i = 0; i < 300; i++) {
            Bukkit.broadcastMessage(" ");
        }
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

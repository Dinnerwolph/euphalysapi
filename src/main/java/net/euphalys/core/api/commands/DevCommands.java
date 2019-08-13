package net.euphalys.core.api.commands;

import net.euphalys.core.api.EuphalysApi;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */
public class DevCommands extends AbstractCommands {

    public DevCommands() {
        super("dev", "euphalys.cmd.dev");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        EuphalysApi.getInstance().moduleHandler.disableModule("rank");
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

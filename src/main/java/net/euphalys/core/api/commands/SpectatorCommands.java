package net.euphalys.core.api.commands;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class SpectatorCommands extends AbstractCommands {

    public SpectatorCommands() {
        super("spectator", "euphalys.cmd.spectator");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {

        player.setGameMode(GameMode.SPECTATOR);
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

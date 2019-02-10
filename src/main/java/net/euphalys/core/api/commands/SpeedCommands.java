package net.euphalys.core.api.commands;

import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class SpeedCommands extends AbstractCommands {

    public SpeedCommands() {
        super("speed", "euphalys.cmd.speed");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        int speed = Integer.parseInt(args[0]);
        float ratio = speed * 1.8f/20.0f;
        player.setWalkSpeed(ratio);
        player.setFlySpeed(ratio);
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/speed <speed value>");
    }
}

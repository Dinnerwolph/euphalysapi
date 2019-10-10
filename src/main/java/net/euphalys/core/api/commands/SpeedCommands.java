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
        if (speed > 11 || speed < 1) {player.sendMessage ("§cErreur : La vitesse doit être comprise entre 1 et 11."); return true; };
        float ratio = speed * 1.8f/20.0f;
        player.setWalkSpeed(ratio);
        player.setFlySpeed(ratio);
        player.sendMessage("§6Vous venez de définir votre vitesse sur §e" + speed);
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("§c/speed <valeur> (La valeur doit être comprise entre 1 et 11) ");
    }
}

package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * @author Dinnerwolph
 */

public class RTpCommands extends AbstractCommands {

    public RTpCommands() {
        super("rtp", "euphalys.cmd.rtp");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        int i = new Random().nextInt(Bukkit.getOnlinePlayers().size());
        Player target = (Player) Bukkit.getOnlinePlayers().toArray()[i];
        player.teleport(target.getLocation());
        player.sendMessage("§6Vous venez de vous téléporter à" + target.getDisplayName());
        return true;

    }

    @Override
    protected void displayHelp() {

    }
}
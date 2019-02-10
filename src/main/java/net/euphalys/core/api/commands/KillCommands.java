package net.euphalys.core.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class KillCommands extends AbstractCommands {

    public KillCommands() {
        super("kill", "euphalys.cmd.kill");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 1) {player.sendMessage("§cErreur : Vous n'avez pas la permission {euphalys.cmd.kill}"); return false;};
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§4Ce joueur n'est pas connecté.");
            return true;
        }
        ((CraftPlayer) target).getHandle().Q();
        player.sendMessage("§6Vous venez de kill §6" + target.getDisplayName());
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("§cUsage : /kill <player>");
    }
}

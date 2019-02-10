package net.euphalys.core.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class VanishCommands extends AbstractCommands {

    public VanishCommands() {
        super("vanish", "euphalys.cmd.vanish");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        IEuphalysPlayer azoplayer = api.getPlayer(player.getUniqueId());
        if (player.hasPermission("euphalys.cmd.vanish")) {
            api.vanishList.add(player.getUniqueId());
            if (azoplayer.isVanished()) {
                api.vanishList.remove(azoplayer.getUUID());
                for (Player players : Bukkit.getOnlinePlayers())
                    players.showPlayer(player);
                api.getPlayerManager().setVanish(azoplayer.getEuphalysId(), 0);
            } else {
                for (Player players : Bukkit.getOnlinePlayers())
                    players.hidePlayer(player);
                api.getPlayerManager().setVanish(azoplayer.getEuphalysId(), 1);
            }
        }
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

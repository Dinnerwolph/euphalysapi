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
        IEuphalysPlayer euphalysPlayer = api.getPlayer(player.getUniqueId());
        if (player.hasPermission("euphalys.cmd.vanish")) {
            api.vanishList.add(player.getUniqueId());
            if (euphalysPlayer.isVanished()) {
                api.vanishList.remove(euphalysPlayer.getUUID());
                for (Player players : Bukkit.getOnlinePlayers())
                    players.showPlayer(player);
                euphalysPlayer.setVanish(false);
                player.sendMessage("§6Vous êtes de nouveau visible. Vous vous déplacez tel un ours qui se réveille :o");
            } else {
                for (Player players : Bukkit.getOnlinePlayers())
                    players.hidePlayer(player);
                euphalysPlayer.setVanish(true);
                player.sendMessage("§6Vous vous déplacez désormais tel un renard furtif. Tachez de ne pas vous faire repérer");
            }
        }
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

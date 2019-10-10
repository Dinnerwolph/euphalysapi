package net.euphalys.core.api.commands;

import net.euphalys.core.api.player.EuphalysPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class GroupCommands extends AbstractCommands {

    public GroupCommands() {
        super("group", "euphalys.cmd.group");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        api.resetGroup();
        api.getPlayerManager().loadAllGroup();
        Bukkit.getOnlinePlayers().forEach(players -> {
            api.removePlayer(players.getUniqueId());
            api.addPlayer(new EuphalysPlayer(players.getUniqueId(), players.getName(), api));
        });
        api.moduleHandler.disableModule("rank");
        api.moduleHandler.enableModuleRegistered("rank");
        player.sendMessage("§6Les groupes ont été rechargés avec succès !");
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

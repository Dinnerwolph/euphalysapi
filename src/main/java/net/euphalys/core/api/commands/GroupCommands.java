package net.euphalys.core.api.commands;

import net.euphalys.api.player.IGroup;
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

        if(args.length == 0) return false;
        else if(args[0].equalsIgnoreCase("refresh")) {
            api.resetGroup();
            api.getPlayerManager().loadAllGroup();
            Bukkit.getOnlinePlayers().forEach(players -> {
                api.removePlayer(players.getUniqueId());
                api.addPlayer(new EuphalysPlayer(players.getUniqueId(), players.getName(), api));
            });
            api.moduleHandler.disableModule("rank");
            api.moduleHandler.enableModuleRegistered("rank");
            player.sendMessage("§6Les groupes ont été rechargés avec succès !");
        } else if(args[0].equalsIgnoreCase("list"))
            for (IGroup group : api.getGroupsCache().values())
                player.sendMessage(group.getName() + " (Rank : " + group.getLadder() + ")");

        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/group refresh");
        player.sendMessage("/group list");
    }
}

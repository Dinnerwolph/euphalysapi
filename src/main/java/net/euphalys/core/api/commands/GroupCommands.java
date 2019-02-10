package net.euphalys.core.api.commands;

import net.euphalys.core.api.player.EuphalysPlayer;
import net.euphalys.core.api.utils.RankTabList;
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
        api.getServer().getScoreboardManager().getMainScoreboard().getTeams().forEach(team -> {
            team.unregister();
        });
        for (Player players : Bukkit.getOnlinePlayers()) {
            api.removePlayer(players.getUniqueId());
            api.addPlayer(new EuphalysPlayer(players.getUniqueId(), players.getName(), api));
            if (api.hasRankInTabList()) {
                RankTabList.updateRank(players);
            }
        }
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}

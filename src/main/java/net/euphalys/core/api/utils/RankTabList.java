package net.euphalys.core.api.utils;

import net.euphalys.core.api.EuphalysApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author Dinnerwolph
 */

public class RankTabList {

    public static void updateRank(Player player) {
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            update(player, p2);
        }
    }

    public static void update(Player player, Player target) {
        Scoreboard scoreboard = target.getScoreboard();
        Team team = null;
        int groupid = EuphalysApi.getInstance().getPlayer(player.getUniqueId()).getGroup().getGroupId();
        int scoreboardId = EuphalysApi.getInstance().getGroup(groupid).getLadder();
        if (scoreboard.getTeam(scoreboardId + "") == null) {
            team = scoreboard.registerNewTeam(scoreboardId + "");
            team.setPrefix(EuphalysApi.getInstance().getGroup(groupid).getPrefix() + " ");
        } else
            team = scoreboard.getTeam(scoreboardId + "");
        team.addPlayer(player);
    }
}

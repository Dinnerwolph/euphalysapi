package net.euphalys.core.api.player;

import net.euphalys.api.event.player.NickNameChangeEvent;
import net.euphalys.api.module.IModule;
import net.euphalys.api.module.Module;
import net.euphalys.core.api.EuphalysApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author Dinnerwolph
 */
@Module(id = "rank")
public class RankModule implements IModule, Listener {


    @Override
    public void onEnable() {
        EuphalysApi.getInstance().getServer().getPluginManager().registerEvents(this, EuphalysApi.getInstance());
        Bukkit.getOnlinePlayers().forEach(player -> {
            updateRank(player);
        });
    }

    @Override
    public void onDisable() {
        EuphalysApi.getInstance().getServer().getScoreboardManager().getMainScoreboard().getTeams().forEach(team -> {
            team.unregister();
        });
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.updateRank(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNickNameChangeEvent(NickNameChangeEvent event) {
        if (!event.isCancelled())
            this.updateRank(event.getPlayer());
    }

    private void updateRank(Player player) {
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            update(player, p2);
        }
    }


    private void update(Player player, Player target) {
        Scoreboard scoreboard = target.getScoreboard();
        Team team = null;
        int groupid = EuphalysApi.getInstance().getPlayer(player.getUniqueId()).getGroup().getGroupId();
        int scoreboardId = EuphalysApi.getInstance().getGroup(groupid).getLadder();
        if (scoreboard.getTeam(String.valueOf(scoreboardId)) == null) {
            team = scoreboard.registerNewTeam(String.valueOf(scoreboardId));
            team.setPrefix(EuphalysApi.getInstance().getGroup(groupid).getPrefix());
            team.setSuffix(EuphalysApi.getInstance().getGroup(groupid).getSuffix());
        } else
            team = scoreboard.getTeam(scoreboardId + "");
        team.addPlayer(player);
    }
}

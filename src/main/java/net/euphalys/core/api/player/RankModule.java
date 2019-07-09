package net.euphalys.core.api.player;

import net.euphalys.api.module.IModule;
import net.euphalys.api.module.Module;
import net.euphalys.core.api.EuphalysApi;
import org.bukkit.scoreboard.Team;

/**
 * @author Dinnerwolph
 */
@Module(id = "rank")
public class RankModule implements IModule {

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        for (Team team : EuphalysApi.getInstance().getServer().getScoreboardManager().getMainScoreboard().getTeams()) {
            team.unregister();
        }
    }
}

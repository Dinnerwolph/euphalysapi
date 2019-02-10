package net.euphalys.core.api.listener.player;

import net.euphalys.core.api.EuphalysApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Dinnerwolph
 */

public class Quit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        EuphalysApi.getInstance().removePlayer(event.getPlayer().getUniqueId());
        EuphalysApi.getInstance().vanishList.remove(event.getPlayer().getUniqueId());
       EuphalysApi.getInstance().getServer().getScoreboardManager().getMainScoreboard();
    }
}

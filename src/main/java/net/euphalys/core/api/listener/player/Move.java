package net.euphalys.core.api.listener.player;

import net.euphalys.core.api.EuphalysApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Dinnerwolph
 */

public class Move implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (EuphalysApi.getInstance().freezeList.contains(event.getPlayer().getUniqueId()))
            event.getPlayer().teleport(event.getFrom());
    }
}

package net.euphalys.core.api.listener.player;

import net.euphalys.core.api.EuphalysApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class Interact implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (EuphalysApi.getInstance().clickList.containsKey(id))
                EuphalysApi.getInstance().clickList.put(id, EuphalysApi.getInstance().clickList.get(id) + 1);
            else
                EuphalysApi.getInstance().clickList.put(id, 1);
        }
    }
}

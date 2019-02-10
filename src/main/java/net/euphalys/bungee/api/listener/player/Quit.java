package net.euphalys.bungee.api.listener.player;

import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Dinnerwolph
 */

public class Quit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        Euphalys.getInstance().getPlayerManager().setLastConnection(event.getPlayer().getUniqueId(), System.currentTimeMillis());
        Euphalys.getInstance().getPlayer(event.getPlayer().getUniqueId()).setTimePlayed();
        Euphalys.getInstance().removePlayer(event.getPlayer().getUniqueId());
    }
}

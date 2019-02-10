package net.euphalys.bungee.api.listener.proxy;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Dinnerwolph
 */

public class Ping implements Listener {

    @EventHandler
    public void onProxyPingEvent(ProxyPingEvent event) {
        //TODO empecher la 1.8
        event.getResponse().setVersion(new ServerPing.Protocol("Serveur en maintenance", -1));
        event.getResponse().setDescriptionComponent(new TextComponent("Serveur en maintenance"));
    }
}

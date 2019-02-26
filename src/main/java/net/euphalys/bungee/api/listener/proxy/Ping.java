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
        event.getResponse().setDescriptionComponent(new TextComponent("       \\u00a72\\u00a7m--\\u00a73 Projet EpyCube \\u00a7b\\u2022\\u00a79 [1.8 - 1.13]\\u00a7r \\u00a72\\u00a7m--\\u00a7r       \\n\\u00a72               Ouverture Prochaine !"));
    }
}

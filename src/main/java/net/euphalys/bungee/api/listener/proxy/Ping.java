package net.euphalys.bungee.api.listener.proxy;

import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Dinnerwolph
 */

public class Ping implements Listener {

    private final String desc;

    public Ping(String desc) {
        if(desc.isEmpty())
            desc = "       §2--§9 Projet EpyCube §2•§b [1.8 - 1.14] §2--       \n              §2Ouverture Prochaine !";
        this.desc = desc;
    }

    @EventHandler
    public void onProxyPingEvent(ProxyPingEvent event) {
        if (Euphalys.getInstance().isMaintenance())
            event.getResponse().setVersion(new ServerPing.Protocol("Serveur en maintenance", -1));
        event.getResponse().setDescriptionComponent(new TextComponent(desc));
    }
}

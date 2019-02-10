package net.euphalys.bungee.api.listener;

import net.euphalys.bungee.api.Euphalys;
import net.euphalys.bungee.api.listener.player.Join;
import net.euphalys.bungee.api.listener.player.Quit;
import net.euphalys.bungee.api.listener.proxy.Ping;

/**
 * @author Dinnerwolph
 */

public class ListenerManager {

    Euphalys instance;

    public ListenerManager() {
        instance = Euphalys.getInstance();
        init();
    }

    private void init() {
        instance.getProxy().getPluginManager().registerListener(instance, new Join(instance));
        instance.getProxy().getPluginManager().registerListener(instance, new Quit());
        instance.getProxy().getPluginManager().registerListener(instance, new Ping());
    }
}

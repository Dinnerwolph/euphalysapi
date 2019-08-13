package net.euphalys.bungee.api.listener;

import net.euphalys.bungee.api.Euphalys;
import net.euphalys.bungee.api.listener.player.Join;
import net.euphalys.bungee.api.listener.player.Quit;
import net.euphalys.bungee.api.listener.proxy.Ping;
import net.md_5.bungee.config.Configuration;

/**
 * @author Dinnerwolph
 */

public class ListenerManager {

    Euphalys instance;

    public ListenerManager(Configuration configuration) {
        instance = Euphalys.getInstance();
        init(configuration);
    }

    private void init(Configuration configuration) {
        instance.getProxy().getPluginManager().registerListener(instance, new Join(instance));
        instance.getProxy().getPluginManager().registerListener(instance, new Quit());
        instance.getProxy().getPluginManager().registerListener(instance, new Ping(configuration.getString("proxy-desc")));
    }
}

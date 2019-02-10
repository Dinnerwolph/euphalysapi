package net.euphalys.core.api.listener;

import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.listener.inventory.Click;
import net.euphalys.core.api.listener.player.*;
import org.bukkit.plugin.PluginManager;

/**
 * @author Dinnerwolph
 */

public class ListenerManager {

    EuphalysApi api;
    PluginManager pm;

    public ListenerManager() {
        this.api = EuphalysApi.getInstance();
        this.pm = api.getServer().getPluginManager();
        this.init();
    }

    private void init() {
        pm.registerEvents(new Join(api), this.api);
        pm.registerEvents(new Chat(), this.api);
        pm.registerEvents(new Quit(), this.api);
        pm.registerEvents(new Click(), this.api);
        pm.registerEvents(new Move(), this.api);
        pm.registerEvents(new Interact(), this.api);
    }
}

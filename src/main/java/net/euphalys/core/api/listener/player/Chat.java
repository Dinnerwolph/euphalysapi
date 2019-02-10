package net.euphalys.core.api.listener.player;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.ISanctions;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.player.EuphalysPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

/**
 * @author Dinnerwolph
 */

public class Chat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!EuphalysApi.getInstance().hasChat) {
            event.setCancelled(true);
            return;
        }
        IEuphalysPlayer player = new EuphalysPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName(), EuphalysApi.getInstance());
        List<ISanctions> sanctions = player.getSanctions();
        for (ISanctions sanction : sanctions) {
            if (sanction.getType().equals(SanctionsType.MUTE)) {
                if (sanction.getDuration() != 0) {
                    if (sanction.getDuration() < System.currentTimeMillis()) {
                        Euphalys.getInstance().getSanctionsManager().removesanction(sanction.getSanctionsId());
                        continue;
                    }
                }
                if (sanction.getServer().equalsIgnoreCase("global") || sanction.getServer().equalsIgnoreCase(EuphalysApi.getInstance().getServerName())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("Vous êtes mute, vous ne pouvez pas parlez.");
                    return;
                }
            }
        }
    }
}

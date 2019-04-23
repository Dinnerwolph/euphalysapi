package net.euphalys.core.api.listener.player;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.player.EuphalysPlayer;
import net.euphalys.core.api.utils.RankTabList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class Join implements Listener {

    private EuphalysApi api;

    public Join(EuphalysApi euphalysApi) {
        this.api = euphalysApi;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        IEuphalysPlayer player = new EuphalysPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName(), api);
        api.addPlayer(player);
        api.getFriendsManager().loadPlayer(event.getPlayer().getUniqueId());
        if (api.hasRankInTabList())
            RankTabList.updateRank(event.getPlayer());
        if (player.isVanished()) {
            api.vanishList.add(player.getUUID());
            for (Player players : Bukkit.getOnlinePlayers())
                players.hidePlayer(event.getPlayer());
        }
        for (UUID uuid : api.vanishList)
            event.getPlayer().hidePlayer(Bukkit.getPlayer(uuid));
        if(!player.getNickName().isEmpty()) {
            EuphalysApi.getInstance().getNickUtils().setNickName(event.getPlayer(), player.getNickName());
            player.setNickName(player.getNickName());
        }
    }
}

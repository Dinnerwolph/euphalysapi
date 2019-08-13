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
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

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
        Player p = event.getPlayer();
        IEuphalysPlayer player = new EuphalysPlayer(p.getUniqueId(), p.getName(), api);
        api.addPlayer(player);
        api.getFriendsManager().loadPlayer(event.getPlayer().getUniqueId());

        /**
         * Nick
         */
        if (!player.getNickName().isEmpty()) {
            EuphalysApi.getInstance().getNickUtils().setNickName(p, player.getNickName());
            player.setNickName(player.getNickName());
        }

        /**
         * Vanish
         */
        if (player.isVanished()) {
            api.vanishList.add(player.getUUID());
            for (Player players : Bukkit.getOnlinePlayers())
                players.hidePlayer(p);
        }
        for (UUID uuid : api.vanishList)
            p.hidePlayer(Bukkit.getPlayer(uuid));

        /**
         * Permission
         */
        PermissionAttachment attachment = p.addAttachment(EuphalysApi.getInstance());
        if (player.hasPermission("*"))
            for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
                attachment.setPermission(permission, true);
            }
        else
            for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
                if (player.hasPermission(permission.getName()))
                    attachment.setPermission(permission, true);
            }
        api.attachmentMap.put(player.getUUID(), attachment);
    }
}

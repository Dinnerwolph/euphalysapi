package net.euphalys.bungee.api.listener.player;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.ISanctions;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.euphalys.core.api.player.EuphalysPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Dinnerwolph
 */

public class Join implements Listener {

    private final Euphalys plugin;
    private final Integer[] version;
    private final String[] server;
    private final String BLACKLIST;
    private final String BAN;
    private final String TEMPBAN;

    public Join(Euphalys plugin) {
        this.plugin = plugin;
        this.version = new Integer[]{47, 110, 340, 498};
        this.server = new String[]{"Hub1-8", "Hub1-9", "Hub1-12", "Hub1-14"};
        this.BLACKLIST = "§2[Euphalys] \n§cVous êtes blacklisté ! \n\n§6Raison : §7%1$s \n\n\n§7Vous ne pouvez pas formuler de demande de débanissement. Un blacklist est définitif et sans appel.";
        this.BAN = "§2[Euphalys] \n§cVous avez été banni définitivement. \n§6Raison : §7%1$s \n§6Expiration : §7Banissement définitif\n\n\n§7Si vous souhaitez être débanni, nous vous laissons faire une demande de débanissement sur notre site. \nhttps://unban.euphalys.net/";
        this.TEMPBAN = "§2[Euphalys] \n§cVous avez été banni. \n§6Raison : §7%1$s \n§6Expiration : §7%2$s\n\n\n§7Si vous souhaitez être débanni, nous vous laissons faire une demande de débanissement sur notre site. \nhttps://unban.euphalys.net/";
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        IEuphalysPlayer euphaPlayer = new EuphalysPlayer(player.getUniqueId(), player.getName(), plugin);

        /*
         * MAINTENANCE
         */

        if (plugin.isMaintenance() && euphaPlayer.getGroup().getGroupId() < 10) {
            player.disconnect(new TextComponent("Serveur en maintenance."));
            return;
        }

        /*
         * SANCTION
         */
        List<ISanctions> sanctions = plugin.getSanctionsManager().getSanction(euphaPlayer);
        for (ISanctions sanction : sanctions) {
            if (sanction.getDuration() != 0) {
                if (sanction.getDuration() < System.currentTimeMillis()) {
                    plugin.getSanctionsManager().removesanction(sanction.getSanctionsId());
                    continue;
                }
            }
            if (sanction.getType().equals(SanctionsType.BAN)) {
                if (sanction.getServer().equalsIgnoreCase("global")) {
                    if (sanction.getDuration() == 0)
                        player.disconnect(new TextComponent(String.format(BAN, sanction.getMessage())));
                    else
                        player.disconnect(new TextComponent(String.format(TEMPBAN, sanction.getMessage(), getTime(sanction.getDuration()))));
                    return;
                }
            } else if (sanction.getType().equals(SanctionsType.BANIP)) {
                if (sanction.getServer().equalsIgnoreCase("global")) {
                    if (sanction.getDuration() == 0)
                        player.disconnect(new TextComponent(String.format(BAN, sanction.getMessage())));
                    else
                        player.disconnect(new TextComponent(String.format(TEMPBAN, sanction.getMessage(), getTime(sanction.getDuration()))));
                    return;
                }
            } else if (sanction.getType().equals(SanctionsType.BLACKLIST)) {
                player.disconnect(new TextComponent(String.format(BLACKLIST, sanction.getMessage())));
                return;
            }
        }


        /*
         *  PERMISSION
         */
        for (Map.Entry<String, Command> a : Euphalys.getInstance().getProxy().getPluginManager().getCommands()) {
            if (a.getValue().getPermission() != null)
                if (euphaPlayer.hasPermission("*") || euphaPlayer.hasPermission(a.getValue().getPermission()))
                    player.setPermission(a.getValue().getPermission(), true);
        }


        plugin.addPlayer(euphaPlayer);
        plugin.getPlayerManager().setLastConnection(player.getUniqueId(), 0);
        plugin.getPlayerManager().setLastAddress(player.getUniqueId(), player.getAddress().getHostString());

    }

    @EventHandler
    public void a(ServerConnectEvent event) {
        int playerVer = event.getPlayer().getPendingConnection().getVersion();
        int count = -1;
        for (int i : version)
            if (playerVer >= i)
                count++;
        ProxiedPlayer player = event.getPlayer();
        if (player.getServer() == null) {
            Map<String, ServerInfo> map = plugin.getProxy().getServers();
            List<String> servers = new ArrayList<>();
            while (servers.size() == 0) {
                for (String s : map.keySet())
                    if (s.startsWith(server[count]))
                        servers.add(s);
                count--;
            }
            Random r = new Random();
            ServerInfo info = map.get(servers.get(r.nextInt(servers.size())));
            event.setTarget(info);
        }
    }

    @EventHandler
    public void a(ServerSwitchEvent event) {
        Euphalys.getInstance().getPlayerManager().setServer(Euphalys.getInstance().getPlayer(event.getPlayer().getUniqueId()).getEuphalysId(), event.getPlayer().getServer().getInfo().getName());
    }

    private String getTime(Long time) {
        time -= System.currentTimeMillis();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df;
        int day = 0;
        while (time >= 86_400_000) {
            time -= 86_400_000;
            day++;
        }
        if (day >= 1)
            df = new SimpleDateFormat(day + " HH:mm:ss");
        else
            df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(timeZone);
        return df.format(time);
    }
}

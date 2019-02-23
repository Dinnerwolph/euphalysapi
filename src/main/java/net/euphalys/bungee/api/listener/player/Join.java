package net.euphalys.bungee.api.listener.player;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.ISanctions;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.euphalys.core.api.player.EuphalysPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Dinnerwolph
 */

public class Join implements Listener {

    private final Euphalys plugin;
    private final int version[] = {47, 110, 340, 404};
    private final String server[] = {"Hub1-8", "Hub1-9", "Hub1-12", "Hub1-13"};

    public Join(Euphalys plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        IEuphalysPlayer euphaPlayer = new EuphalysPlayer(player.getUniqueId(), player.getName(), plugin);
        if (euphaPlayer.getGroup().getGroupId() < 10) {
            player.disconnect(new TextComponent("Serveur en maintenance."));
            return;
        }
        List<ISanctions> sanctions = plugin.getSanctionsManager().getSanction(euphaPlayer);
        for (ISanctions sanction : sanctions) {
            if (sanction.getType().equals(SanctionsType.BAN)) {
                if (sanction.getDuration() != 0) {
                    if (sanction.getDuration() < System.currentTimeMillis()) {
                        plugin.getSanctionsManager().removesanction(sanction.getSanctionsId());
                        continue;
                    }
                }
                if (sanction.getServer().equalsIgnoreCase("global")) {
                    player.disconnect(new TextComponent("Vous êtes bannis du serveur"));
                    return;
                }
            } else if (sanction.getType().equals(SanctionsType.BANIP)) {
                if (sanction.getDuration() != 0) {
                    if (sanction.getDuration() < System.currentTimeMillis()) {
                        plugin.getSanctionsManager().removesanction(sanction.getSanctionsId());
                        continue;
                    }
                }
                if (sanction.getServer().equalsIgnoreCase("global")) {
                    player.disconnect(new TextComponent("Vous êtes bannis du serveur"));
                    return;
                }
            } else if (sanction.getType().equals(SanctionsType.BLACKLIST)) {
                player.disconnect(new TextComponent("Vous ne pouvez pas vous connecter."));
                return;
            }
        }
        plugin.addPlayer(euphaPlayer);
        plugin.getPlayerManager().setLastConnection(player.getUniqueId(), 0);
        plugin.getPlayerManager().setLastAddress(player.getUniqueId(), player.getAddress().getHostString());
        int playerVer = event.getPlayer().getPendingConnection().getVersion();
        int count = -1;
        for (int i : version)
            if (playerVer >= i)
                count++;
        if (player.getServer() == null) {
            Map<String, ServerInfo> map = plugin.getProxy().getServers();
            List<String> servers = new ArrayList();
            for (String s : map.keySet())
                if (s.startsWith(server[count]))
                    servers.add(s);
            Random r = new Random();
            ServerInfo info = map.get(servers.get(r.nextInt(servers.size())));
            event.setTarget(info);
        }
    }

    @EventHandler
    public void a(ServerSwitchEvent event) {
        Euphalys.getInstance().getPlayerManager().setServer(Euphalys.getInstance().getPlayer(event.getPlayer().getUniqueId()).getEuphalysId(), event.getPlayer().getServer().getInfo().getName());
    }
}

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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Dinnerwolph
 */

public class Join implements Listener {

    private final Euphalys plugin;
    private final int version[] = {47, 110, 340, 490};
    private final String server[] = {"Hub1-8", "Hub1-9", "Hub1-12", "Hub1-14"};

    public Join(Euphalys plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        IEuphalysPlayer euphaPlayer = new EuphalysPlayer(player.getUniqueId(), player.getName(), plugin);
        if (euphaPlayer.getGroup().getGroupId() < 10) {
            event.setCancelled(true);
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
                    event.setCancelled(true);
                    player.disconnect(new TextComponent("§2[Euphalys] \n§cVous avez été banni définitivement. \n§6Raison : §7" + sanction.getMessage() + "\n§6Expiration : §7Banissement définitif\n\n\n§7Si vous souhaitez être débanni, nous vous laissons faire une demande de débanissement sur notre site. \nhttps://unban.euphalys.net/"));
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
                    event.setCancelled(true);
                    player.disconnect(new TextComponent("Vous êtes bannis du serveur"));
                    return;
                }
            } else if (sanction.getType().equals(SanctionsType.BLACKLIST)) {
                event.setCancelled(true);
                player.disconnect(new TextComponent("§2[Euphalys] \n§cVous êtes blacklisté ! \n\n§6Raison : §7" + sanction.getMessage() + "\n\n\n§7Vous ne pouvez pas formuler de demande de débanissement. Un blacklist est définitif et sans appel."));
                return;
            }
        }
        plugin.addPlayer(euphaPlayer);
        plugin.getPlayerManager().setLastConnection(player.getUniqueId(), 0);
        plugin.getPlayerManager().setLastAddress(player.getUniqueId(), player.getAddress().getHostString());
        try {
            //InetAddress address = InetAddress.getLocalHost();
            InetAddress address = player.getAddress().getAddress();

            /*
             * Get NetworkInterface for the current host and then read
             * the hardware address.
             */
            NetworkInterface ni =  NetworkInterface.getByInetAddress(address);
            if (ni != null) {
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    /*
                     * Extract each array of mac address and convert it
                     * to hexadecimal with the following format
                     * 08-00-27-DC-4A-9E.
                     */
                    for (int i = 0; i < mac.length; i++) {
                        System.out.format("%02X%s",
                                mac[i], (i < mac.length - 1) ? "-" : "");
                    }
                } else {
                    System.out.println("Address doesn't exist or is not " +
                            "accessible.");
                }
            } else {
                System.out.println("Network Interface for the specified " +
                        "address is not found.");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        int playerVer = event.getPlayer().getPendingConnection().getVersion();
        int count = -1;
        for (int i : version)
            if (playerVer >= i)
                count++;
        if (player.getServer() == null) {
            Map<String, ServerInfo> map = plugin.getProxy().getServers();
            List<String> servers = new ArrayList();
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
}

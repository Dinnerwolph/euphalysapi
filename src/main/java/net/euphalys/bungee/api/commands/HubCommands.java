package net.euphalys.bungee.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Dinnerwolph
 */

public class HubCommands extends Command {

    private final int version[] = {47, 110, 340, 477};
    private final String server[] = {"Hub1-8", "Hub1-9", "Hub1-12", "Hub1-14"};

    public HubCommands() {
        super("hub", "", "lobby");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            IEuphalysPlayer player = Euphalys.getInstance().getPlayer(((ProxiedPlayer) commandSender).getUniqueId());
            if (player.hasPermission("euphalys.cmd.hub")) {
                if (args.length < 1) {
                    ((ProxiedPlayer) commandSender).connect(getRandomHub(((ProxiedPlayer) commandSender).getPendingConnection().getVersion()));
                    return;
                }
                ProxiedPlayer target = Euphalys.getInstance().getProxy().getPlayer(args[0]);
                if (target == null) {
                    commandSender.sendMessage("Ce joueurs n'est pas connecté.");
                    return;
                }
                target.connect(getRandomHub(target.getPendingConnection().getVersion()));
            }
        }
    }

    private ServerInfo getRandomHub(int playerVer) {
        int count = -1;
        for (int i : version)
            if (playerVer >= i)
                count++;
        Map<String, ServerInfo> map = Euphalys.getInstance().getProxy().getServers();
        List<String> servers = new ArrayList();
        while (servers.size() == 0) {
            for (String s : map.keySet())
                if (s.startsWith(server[count]))
                    servers.add(s);
            count--;
        }
        Random r = new Random();
        ServerInfo info = map.get(servers.get(r.nextInt(servers.size())));
        return info;
    }
}

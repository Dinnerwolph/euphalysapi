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

    public HubCommands() {
        super("hub", "", "lobby");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            IEuphalysPlayer player = Euphalys.getInstance().getPlayer(((ProxiedPlayer) commandSender).getUniqueId());
            if (player.hasPermission("euphalys.cmd.hub")) {
                if (args.length < 1) {
                    ((ProxiedPlayer) commandSender).connect(getRandomHub());
                    return;
                }
                ProxiedPlayer target = Euphalys.getInstance().getProxy().getPlayer(args[0]);
                if (target == null) {
                    commandSender.sendMessage("Ce joueurs n'est pas connecté.");
                    return;
                }
                target.connect(getRandomHub());
            }
        }
    }

    private ServerInfo getRandomHub() {
        Map<String, ServerInfo> map = Euphalys.getInstance().getProxy().getServers();
        List<String> servers = new ArrayList();
        for (String s : map.keySet()) {
            if (s.startsWith("Hub"))
                servers.add(s);
        }
        Random r = new Random();
        ServerInfo info = map.get(servers.get(r.nextInt(servers.size())));
        return info;
    }
}

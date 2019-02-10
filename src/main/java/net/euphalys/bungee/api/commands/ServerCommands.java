package net.euphalys.bungee.api.commands;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Dinnerwolph
 */

public class ServerCommands extends Command implements TabExecutor {

    public ServerCommands() {
        super("server", "euphalys.cmd.server", new String[0]);
    }

    public void execute(CommandSender sender, String[] args) {
        Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                sender.sendMessage(ProxyServer.getInstance().getTranslation("current_server", new Object[]{((ProxiedPlayer) sender).getServer().getInfo().getName()}));
            }

            TextComponent serverList = new TextComponent(ProxyServer.getInstance().getTranslation("server_list", new Object[0]));
            serverList.setColor(ChatColor.GOLD);
            boolean first = true;
            Iterator var6 = servers.values().iterator();

            while (var6.hasNext()) {
                ServerInfo server = (ServerInfo) var6.next();
                if (server.canAccess(sender)) {
                    TextComponent serverTextComponent = new TextComponent(first ? server.getName() : ", " + server.getName());
                    int count = server.getPlayers().size();
                    serverTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(count + (count == 1 ? " player" : " players") + "\n")).append("Click to connect to the server").italic(true).create()));
                    serverTextComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/server " + server.getName()));
                    serverList.addExtra(serverTextComponent);
                    first = false;
                }
            }

            sender.sendMessage(serverList);
        } else {
            if (!(sender instanceof ProxiedPlayer)) {
                return;
            }

            ProxiedPlayer player = (ProxiedPlayer) sender;
            ServerInfo server = (ServerInfo) servers.get(args[0]);
            if (server == null) {
                player.sendMessage(ProxyServer.getInstance().getTranslation("no_server", new Object[0]));
            } else if (!server.canAccess(player)) {
                player.sendMessage(ProxyServer.getInstance().getTranslation("no_server_permission", new Object[0]));
            } else {
                if (args.length == 2)
                    if (args[1].equalsIgnoreCase("report"))
                        for (ProxiedPlayer players : Euphalys.getInstance().getProxy().getPlayers()) {
                            if (Euphalys.getInstance().getPlayer(players.getUniqueId()).hasPermission("euphalys.moderation.viewreport")) {
                                TextComponent component = new TextComponent(sender.getName() + " c'est téléporté au server " + server.getName() + "");
                                players.sendMessage(component);
                            }
                        }
                player.connect(server);
            }
        }

    }

    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        return (Iterable) (args.length > 1 ? Collections.EMPTY_LIST : Iterables.transform(Iterables.filter(ProxyServer.getInstance().getServers().values(), new Predicate<ServerInfo>() {
            private final String lower = args.length == 0 ? "" : args[0].toLowerCase();

            public boolean apply(ServerInfo input) {
                return input.getName().toLowerCase().startsWith(this.lower) && input.canAccess(sender);
            }
        }), new Function<ServerInfo, String>() {
            public String apply(ServerInfo input) {
                return input.getName();
            }
        }));
    }
}

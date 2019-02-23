package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author Dinnerwolph
 */
public abstract class AbstractTempSanctions extends Command implements TabExecutor {

    private final String permission;
    private CommandSender commandSender;

    public AbstractTempSanctions(String name, String permission) {
        super(name);
        this.permission = permission;
    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        this.commandSender = commandSender;
        if (commandSender instanceof ProxiedPlayer) {
            IEuphalysPlayer player = Euphalys.getInstance().getPlayer(((ProxiedPlayer) commandSender).getUniqueId());
            if (player.hasPermission(permission))
                if (!(strings.length < 3)) {
                    String message = "";
                    for (int i = 2; i < strings.length; i++)
                        message = message + strings[i] + " ";
                    if (!onCommand(player, strings[0], strings[1], message))
                        displayHelp();
                }
        }
    }

    abstract boolean onCommand(IEuphalysPlayer player, String playerName, String duration, String message);

    abstract void displayHelp();

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        Set<String> matches = new HashSet<>();
        if (args.length == 1) {
            String search = args[0].toLowerCase(Locale.ROOT);
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(player.getName());
                }
            }
        }
        return matches;
    }

    protected void sendMessage(CommandSender player, String... messages) {
        for (String message : messages)
            player.sendMessage(new TextComponent(message));
    }

    protected void sendMessage(String... messages) {
        sendMessage(commandSender, messages);
    }
}

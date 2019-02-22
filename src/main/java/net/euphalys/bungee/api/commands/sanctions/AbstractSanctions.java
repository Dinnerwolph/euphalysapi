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
public abstract class AbstractSanctions extends Command implements TabExecutor {

    private final String permission;
    private final int start;
    private CommandSender commandSender;

    AbstractSanctions(String name, String permission) {
        this(name, permission, 1);
    }

    public AbstractSanctions(String name, String permission, int i) {
        super(name);
        this.permission = permission;
        this.start = i;
    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        this.commandSender = commandSender;
        if (commandSender instanceof ProxiedPlayer) {
            IEuphalysPlayer player = Euphalys.getInstance().getPlayer(((ProxiedPlayer) commandSender).getUniqueId());
            if (player.hasPermission(permission))
                if (!(strings.length < start + 1)) {
                    String message = "";
                    for (int i = start; i < strings.length; i++)
                        message = message + strings[i] + " ";
                    if (!onCommand(player, strings[0], message))
                        displayHelp();
                }
        }
    }

    abstract boolean onCommand(IEuphalysPlayer player, String playerName, String message);

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

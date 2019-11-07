package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.euphalys.core.api.EuphalysApi;
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

    protected CommandSender commandSender;
    protected final SanctionsType sanctionsType;

    protected AbstractSanctions(String name, String permission, SanctionsType sanctionsType) {
        super(name, permission);
        this.sanctionsType = sanctionsType;
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        this.commandSender = commandSender;
        if (args.length == 0)
            displayHelp();
        else {
            if (!(args.length < 2)) {
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++)
                    message.append(args[i]).append(" ");
                if (!onCommand(commandSender, args[0], message.toString()))
                    displayHelp();
            }
        }
    }

    abstract boolean onCommand(CommandSender sender, String playerName, String message);


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

    protected void addGlobalSanction(String targetName, String message, int playerId) {
        EuphalysApi.getInstance().getSanctionsManager().addGlobalSanction(EuphalysApi.getInstance().getPlayer(targetName), sanctionsType, 0, message, playerId);
    }

    protected int getUserId() {
        if (commandSender instanceof ProxiedPlayer)
            return Euphalys.getInstance().getPlayer(commandSender.getName()).getEuphalysId();
        else
            return 0;
    }
}

package net.euphalys.bungee.api.commands;

import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Random;

/**
 * @author Dinnerwolph
 */

public class GRTpCommands extends Command {

    public GRTpCommands() {
        super("grtp", "euphalys.cmd.grtp");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            int i = new Random().nextInt(Euphalys.getInstance().getProxy().getPlayers().size());
            ProxiedPlayer target = (ProxiedPlayer) Euphalys.getInstance().getProxy().getPlayers().toArray()[i];
            ((ProxiedPlayer) commandSender).connect(target.getServer().getInfo());
            return;
        }
    }
}

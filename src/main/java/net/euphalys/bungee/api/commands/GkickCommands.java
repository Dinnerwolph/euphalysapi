package net.euphalys.bungee.api.commands;

import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Dinnerwolph
 */
public class GkickCommands extends Command {

    public GkickCommands() {
            super("gkick", "euphalys.cmd.gkick");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Euphalys.getInstance().getProxy().getPlayer(args[0]).disconnect(new TextComponent("test"));
    }
}

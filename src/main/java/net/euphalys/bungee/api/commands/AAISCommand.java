package net.euphalys.bungee.api.commands;

import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Dinnerwolph
 */
public class AAISCommand extends Command {

    public AAISCommand() {
        super("aais", "euphalys.cmd.aais");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(args.length < 1) {
            commandSender.sendMessage(new TextComponent("/aais <message>"));
            return;
        } else {
            String message = "";
            for (int i = 0; i < args.length; i++)
                message = message + args[i] + " ";
            Euphalys.getInstance().getProxy().broadcast(new TextComponent(message.replaceAll("&", "§")));
        }
    }
}

package net.euphalys.bungee.api.commands;

import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Dinnerwolph
 */
public class CISCommand extends Command {

    public CISCommand() {
        super("cis", "euphalys.cmd.cis");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(args.length < 2) {
            commandSender.sendMessage(new TextComponent("/cis <pseudo> <channel>"));
            return;
        } else {
            String salon;
            if(args[1].equals("modo"))
                salon = "Convocation (Modération)";
            else if(args[1].equals("admin"))
                salon = "Administration";
            else {
                commandSender.sendMessage(new TextComponent("choix du salon modo ou admin."));
                return;
            }
            Euphalys.getInstance().getProxy().broadcast(new TextComponent("[Convocation] >> " + args[0] + ", tu est convoqué sur Teamspeak (ts.euphalys.net). Merci de te présenter dans le channel \"Attente Staff ➜ " + salon + "\""));
        }
    }
}

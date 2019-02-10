package net.euphalys.bungee.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Dinnerwolph
 */

public class SeeModsCommands extends Command {

    public SeeModsCommands() {
        super("seemods");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            IEuphalysPlayer player = Euphalys.getInstance().getPlayer(((ProxiedPlayer) commandSender).getUniqueId());
            if (player.hasPermission("euphalys.cmd.seemods")) {
                if (args.length < 1) {commandSender.sendMessage("§cErreur : Vous n'avez pas la permission d'effectuer cette commande {euphalys.cmd.seemods"); return;}
                ProxiedPlayer target = Euphalys.getInstance().getProxy().getPlayer(args[0]);
                if (target == null) {
                    commandSender.sendMessage("Ce joueur n'est pas connecté.");
                    return;
                }
                for (String mods : target.getModList().keySet())
                    commandSender.sendMessage(new TextComponent("§6Mods pour le joueur" target.getDisplayName() ":" + mods + " " + target.getModList().get(mods)));
            }
        }
    }
}

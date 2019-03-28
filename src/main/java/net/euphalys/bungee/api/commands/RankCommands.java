package net.euphalys.bungee.api.commands;

import net.euphalys.api.player.IPlayerManager;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */
public class RankCommands extends Command {

    public RankCommands() {
        super("rank", "");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            displayHelp(commandSender);
            return;
        } else {
            IPlayerManager manager = Euphalys.getInstance().getPlayerManager();
            UUID uuid = Euphalys.getInstance().getUUUIDTranslator().getUUID(args[0]);
            if(!manager.exist(uuid)) {
                commandSender.sendMessage("Ce joueur n'existe pas.");
                return;
            }else {
                manager.setRank(manager.getAzonaryaId(uuid), 11);
                commandSender.sendMessage("commande effectuée avec succès.");
                System.out.println(commandSender.getName() + " rank " + args[0]);
            }
        }
    }

    private void displayHelp(CommandSender commandSender) {
        commandSender.sendMessage("/rank <pseudo> : permet de mettre le grade staff test à un joueur");
    }
}

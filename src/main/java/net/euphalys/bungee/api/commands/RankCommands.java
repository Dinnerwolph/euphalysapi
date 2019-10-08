package net.euphalys.bungee.api.commands;

import net.euphalys.api.player.IPlayerManager;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */
public class RankCommands extends Command {

    public RankCommands() {
        super("rank", "euphalys.manager.rank");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            displayHelp(commandSender);
            return;
        } else if (args[0].equalsIgnoreCase("set")) {
            IPlayerManager manager = Euphalys.getInstance().getPlayerManager();
            UUID uuid = Euphalys.getInstance().getUUUIDTranslator().getUUID(args[1]);
            if (!manager.exist(uuid)) {
                commandSender.sendMessage("Ce joueur n'existe pas.");
                return;
            } else {
                if (args[2] == null)
                    manager.setRank(manager.getAzonaryaId(uuid), 11);
                else
                    manager.setRank(manager.getAzonaryaId(uuid), Integer.parseInt(args[2]));
                commandSender.sendMessage("commande effectuée avec succès.");
                System.out.println(commandSender.getName() + " rank " + args[1]);
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            for (int i = 0; i < 100; i++) {
                if (Euphalys.getInstance().getGroup(i) != null) {
                    commandSender.sendMessage(new TextComponent(Euphalys.getInstance().getGroup(i).getScore() + " : " + Euphalys.getInstance().getGroup(i).getGroupId()));
                }
            }
        }
    }

    private void displayHelp(CommandSender commandSender) {
        commandSender.sendMessage(new TextComponent("/rank list : permet de connaitre le grade et son numéro"));
        commandSender.sendMessage(new TextComponent("/rank set <pseudo> : permet de mettre le grade staff test à un joueur"));
        commandSender.sendMessage(new TextComponent("/rank set <pseudo> <rankId> : permet de mettre le grade en fonction du numéro à un joueur"));
    }
}

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
        super("seemods", "euphalys.cmd.seemods");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
                if (args.length < 1) {
                    commandSender.sendMessage("§cSynthaxe : /seemods <joueur>");
                    return;
                }
                ProxiedPlayer target = Euphalys.getInstance().getProxy().getPlayer(args[0]);
                if (target == null) {
                    commandSender.sendMessage("§cErreur : Ce joueur n'est pas connecté.");
                    return;
                }
                if (target.getModList().size() == 0) {
                    commandSender.sendMessage("§a✔ §6Aucun mod détecté pour ce joueur.");
                    return;
                }
                for (String mods : target.getModList().keySet())
                    commandSender.sendMessage(new TextComponent("§6Mods pour le joueur" + target.getDisplayName() + ":" + mods + " " + target.getModList().get(mods)));
            /**} else {
                commandSender.sendMessage("§cErreur : Vous n'avez pas la permission d'effectuer cette commande {euphalys.cmd.seemods");
            }*/
        }
    }

    /**public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
     return (Iterable) (args.length > 1 ? Collections.EMPTY_LIST : Iterables.transform(Iterables.filter(ProxyServer.getInstance().getPlayers(), new Predicate<UserConnection>() {
     private final String lower = args.length == 0 ? "" : args[0].toLowerCase();

     public boolean apply(ProxiedPlayer input) {
     return input.getName().toLowerCase().startsWith(this.lower);
     }
     }), new Function<UserConnection, String>() {
     public String apply(ProxiedPlayer input) {
     return input.getName();
     }
     }));
     }*/
}

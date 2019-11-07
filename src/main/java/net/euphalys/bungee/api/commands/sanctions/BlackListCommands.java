package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author Dinnerwolph
 */

public class BlackListCommands extends AbstractSanctions {

    public BlackListCommands() {
        super("blacklist", "euphalys.cmd.blacklist", SanctionsType.BLACKLIST);
    }

    @Override
    boolean onCommand(CommandSender sender, String target, String message) {
        addGlobalSanction(target, message, getUserId());
        ProxiedPlayer targetp = Euphalys.getInstance().getProxy().getPlayer(target);
        if (targetp != null)
            targetp.disconnect(new TextComponent("§2[Euphalys] \n§cVous êtes blacklisté ! \n\n§6Raison : §7" + message + " \n\n\n§7Vous ne pouvez pas formuler de demande de débanissement. Un blacklist est définitif et sans appel."));
        sendMessage("wesh poto t'as blacklist " + target);
        return true;
    }

    @Override
    void displayHelp() {
        sendMessage("/blacklist <player> <message>");
    }
}

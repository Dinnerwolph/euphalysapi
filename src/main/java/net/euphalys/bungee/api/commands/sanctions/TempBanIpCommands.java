package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author Dinnerwolph
 */

public class TempBanIpCommands extends AbstractTempSanctions {

    public TempBanIpCommands() {
        super("gtempbanip", "euphalys.cmd.gtempbanip", SanctionsType.BANIP);
    }

    @Override
    boolean onCommand(CommandSender sender, String target, long duration, String message) {
        addGlobalSanction(target, message, getUserId());
        ProxiedPlayer proxiedPlayer = Euphalys.getInstance().getProxy().getPlayer(target);
        if (proxiedPlayer != null)
            //TODO set expiration time
            proxiedPlayer.disconnect(new TextComponent("§2[Euphalys] \n§cVous avez été banni. \n§6Raison : §7" + message + "\n§6Expiration : §7Un certain temps)\n\n\n§7Si vous souhaitez être débanni, nous vous laissons faire une demande de débanissement sur notre site. \nhttps://unban.euphalys.net/"));
        sendMessage("wesh poto t'as banip " + target);
        return true;
    }


    @Override
    protected void displayHelp() {
        sendMessage("/gtempbanip <player> <duration> <message>");
    }
}

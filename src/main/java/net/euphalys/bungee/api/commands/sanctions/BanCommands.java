package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class BanCommands extends AbstractSanctions {

    public BanCommands() {
        super("ban", "epycube.cmd.ban");
    }

    @Override
    boolean onCommand(IEuphalysPlayer player, String args, String message) {
        UUID target = Euphalys.getInstance().getUUUIDTranslator().getUUID(args);
        Euphalys.getInstance().getSanctionsManager().addGlobalSanction(Euphalys.getInstance().getPlayer(target), SanctionsType.BAN, 0, message, player);
        ProxiedPlayer proxiedPlayer = Euphalys.getInstance().getProxy().getPlayer(target);
        if (proxiedPlayer != null)
            proxiedPlayer.disconnect("§2[Euphalys] \n§cVous avez été banni définitivement. \n§6Raison : §7" + message + "\n§6Expiration : §7Banissement définitif\n\n\n§7Si vous souhaitez être débanni, nous vous laissons faire une demande de débanissement sur notre site. \nhttps://unban.euphalys.net/");
        return true;
    }

    @Override
    void displayHelp() {
        sendMessage("/ban <player> <message>");
    }
}

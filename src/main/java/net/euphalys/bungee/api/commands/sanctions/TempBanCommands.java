package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class TempBanCommands extends AbstractTempSanctions {

    public TempBanCommands() {
        super("gtempban", "euphalys.cmd.gtempban");
    }

    private SanctionsType sanctionsType = SanctionsType.BAN;

    @Override
    boolean onCommand(IEuphalysPlayer player, String playerName, long duration, String message) {
        UUID target = Euphalys.getInstance().getUUUIDTranslator().getUUID(playerName);
        Euphalys.getInstance().getSanctionsManager().addGlobalSanction(Euphalys.getInstance().getPlayer(target), sanctionsType, duration, message, player);
        ProxiedPlayer proxiedPlayer = Euphalys.getInstance().getProxy().getPlayer(target);
        if (proxiedPlayer != null)
            //TODO set expiration time
            proxiedPlayer.disconnect(new TextComponent("§2[Euphalys] \n§cVous avez été banni. \n§6Raison : §7" + message + "\n§6Expiration : §7Un certain temps)\n\n\n§7Si vous souhaitez être débanni, nous vous laissons faire une demande de débanissement sur notre site. \nhttps://unban.euphalys.net/"));
        sendMessage("wesh poto t'as ban " + playerName);
        return true;
    }


    @Override
    protected void displayHelp() {
        sendMessage("/gtempban <player> <duration> <message>");
    }
}

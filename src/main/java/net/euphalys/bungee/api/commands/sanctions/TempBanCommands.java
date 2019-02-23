package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;

/**
 * @author Dinnerwolph
 */

public class TempBanCommands extends AbstractTempSanctions {

    public TempBanCommands() {
        super("tempban", "epycube.cmd.tempban");
    }

    private SanctionsType sanctionsType = SanctionsType.MUTE;

    @Override
    boolean onCommand(IEuphalysPlayer player, String playerName, String duration, String message) {
        Euphalys api = Euphalys.getInstance();
        IEuphalysPlayer targetplayer = Euphalys.getInstance().getPlayer(api.getUUUIDTranslator().getUUID(playerName));
        int first = Integer.parseInt(duration.substring(0, 1));
        switch (duration.substring(1)) {
            case "h":
                long i = 60 * 60 * first * 1000;
                i = i + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, i, message, player);
                return true;
            case "d":
                long d = 60 * 60 * 24 * first * 1000;
                d = d + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, d, message, player);
                return true;
            case "w":
                long w = 60 * 60 * 24 * 7 * first * 1000;
                w = w + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, w, message, player);
                return true;
            case "m":
                long m = 60 * 60 * 24 * 30 * first * 1000;
                m = m + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, m, message, player);
                return true;
            case "y":
                long y = 60 * 60 * 24 * 365 * first * 1000;
                y = y + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, y, message, player);
                return true;
        }
        return false;
    }


    @Override
    protected void displayHelp() {
        sendMessage("/tempban <player> <duration> <message>");
    }
}

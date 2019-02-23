package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;

/**
 * @author Dinnerwolph
 */

public class TempMuteCommands extends AbstractTempSanctions {

    public TempMuteCommands() {
        super("tempmute", "epycube.cmd.tempmute");
    }

    private SanctionsType sanctionsType = SanctionsType.MUTE;

    @Override
    boolean onCommand(IEuphalysPlayer player, String playerName, String duration, String message) {
        Euphalys api = Euphalys.getInstance();
        IEuphalysPlayer targetplayer = Euphalys.getInstance().getPlayer(api.getUUUIDTranslator().getUUID(playerName));
        int first = Integer.parseInt(duration.substring(0, 1));
        switch (duration.substring(1)) {
            case "h":
                long i = 60 * 60 * first;
                i = i + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) i, message, player);
                return true;
            case "d":
                long d = 60 * 60 * 24 * first;
                d = d + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) d, message, player);
                return true;
            case "w":
                long w = 60 * 60 * 24 * 7 * first;
                w = w + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) w, message, player);
                return true;
            case "m":
                long m = 60 * 60 * 24 * 30 * first;
                m = m + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) m, message, player);
                return true;
            case "y":
                long y = 60 * 60 * 24 * 365 * first;
                y = y + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) y, message, player);
                return true;
        }
        return false;
    }


    @Override
    protected void displayHelp() {
        sendMessage("/gtemmute <player> <duration> <message>");
    }
}

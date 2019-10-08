package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;

/**
 * @author Dinnerwolph
 */

public class TempMuteCommands extends AbstractTempSanctions {

    public TempMuteCommands() {
        super("gtempmute", "euphalys.cmd.gtempmute");
    }

    private SanctionsType sanctionsType = SanctionsType.MUTE;

    @Override
    boolean onCommand(IEuphalysPlayer player, String playerName, long duration, String message) {
        Euphalys api = Euphalys.getInstance();
        IEuphalysPlayer targetplayer = Euphalys.getInstance().getPlayer(api.getUUUIDTranslator().getUUID(playerName));
        api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, duration, message, player);
        sendMessage("wesh poto t'as mute " + playerName);
        return true;
    }


    @Override
    protected void displayHelp() {
        sendMessage("/gtemmute <player> <duration> <message>");
    }
}

package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.sanctions.SanctionsType;
import net.md_5.bungee.api.CommandSender;

/**
 * @author Dinnerwolph
 */

public class TempMuteCommands extends AbstractTempSanctions {

    public TempMuteCommands() {
        super("gtempmute", "euphalys.cmd.gtempmute", SanctionsType.MUTE);
    }

    @Override
    boolean onCommand(CommandSender sender, String target, long duration, String message) {
        addGlobalSanction(target, message, getUserId());
        sendMessage(sender, "Vous êtes mute, vous ne pouvez plus parler.");
        sendMessage("wesh poto t'as mute " + target);
        return true;
    }


    @Override
    protected void displayHelp() {
        sendMessage("/gtemmute <player> <duration> <message>");
    }
}

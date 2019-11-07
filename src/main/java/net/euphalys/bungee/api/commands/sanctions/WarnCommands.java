package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.sanctions.SanctionsType;
import net.md_5.bungee.api.CommandSender;

/**
 * @author Dinnerwolph
 */

public class WarnCommands extends AbstractSanctions {

    public WarnCommands() {
        super("gwarn", "euphalys.cmd.warn", SanctionsType.WARN);
    }

    @Override
    boolean onCommand(CommandSender sender, String target, String message) {
        addGlobalSanction(target, message, getUserId());
        sendMessage("wesh poto t'as warn " + target);
        return true;
    }

    @Override
    protected void displayHelp() {
        sendMessage("/gwarn <player> <message>");
    }
}

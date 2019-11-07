package net.euphalys.core.api.commands;

import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class CloseReportCommands extends AbstractCommands {

    public CloseReportCommands() {
        super("closereport", "euphalys.cmd.closereport");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 1)
            return false;
        int id = Integer.parseInt(args[0]);
        api.getReportManager().closeReport(id);
        player.sendMessage("Vous venez de fermer le report n°" + id);
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/closereport <reportId>");
    }
}

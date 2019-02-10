package net.euphalys.core.api.commands;

import net.euphalys.api.report.IReport;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class InfoReportCommands extends AbstractCommands {

    public InfoReportCommands() {
        super("inforeport", "euphalys.cmd.inforeport");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        if (args.length < 1) return false;
        int id = Integer.parseInt(args[0]);
        IReport report = api.getReportManager().getReport(id);
        player.sendMessage("ReportId: " + args[0]);
        player.sendMessage("Reported: " + report.getTarget());
        player.sendMessage("By: " + report.getReportedBy());
        player.sendMessage("Reason: " + report.getMessage());
        return true;
    }

    @Override
    protected void displayHelp() {
        player.sendMessage("/inforeport <reportId>");
    }
}

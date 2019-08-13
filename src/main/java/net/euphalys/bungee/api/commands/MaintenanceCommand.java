package net.euphalys.bungee.api.commands;

import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Dinnerwolph
 */
public class MaintenanceCommand extends Command {

    public MaintenanceCommand() {
        super("maintenance", "euphalys.cmd.maintenance");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(new TextComponent("/maintenance <on|off>"));
        } else {
            if (args[0].equalsIgnoreCase("on")) {
                for (ProxiedPlayer player : Euphalys.getInstance().getProxy().getPlayers()) {
                    if (Euphalys.getInstance().getPlayer(player.getUniqueId()).getGroup().getGroupId() < 10)
                        player.disconnect(new TextComponent("Maintenance."));
                }
                Euphalys.getInstance().setMaintenance(true);
            } else if (args[0].equalsIgnoreCase("off")) {
                Euphalys.getInstance().setMaintenance(false);
            } else {
                commandSender.sendMessage(new TextComponent("/maintenance <on|off>"));
            }
        }
    }
}

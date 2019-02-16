package net.euphalys.bungee.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Dinnerwolph
 */

public class ReportCommands extends Command {

    public ReportCommands() {
        super("report");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            IEuphalysPlayer player = Euphalys.getInstance().getPlayer(((ProxiedPlayer) commandSender).getUniqueId());
            if (player.hasPermission("euphalys.cmd.report")) {
                if (args.length < 2) {
                    commandSender.sendMessage("§cPour reporter un joueur, merci de suivre cette synthaxe : /report <joueur> <raison>");
                    return;
                }
                ProxiedPlayer target = Euphalys.getInstance().getProxy().getPlayer(args[0]);
                if (target == null) {
                    commandSender.sendMessage("§cCe joueur n'est pas connecté.");
                    return;
                }
                String message = "";
                for (int i = 1; i < args.length; i++) {
                    message = message + args[i] + " ";
                }
                Euphalys.getInstance().getReportManager().addReport(Euphalys.getInstance().getPlayer(target.getUniqueId()), player, message);
                for (ProxiedPlayer players : Euphalys.getInstance().getProxy().getPlayers()) {
                    if (Euphalys.getInstance().getPlayer(players.getUniqueId()).hasPermission("euphalys.moderation.viewreport")) {
                        TextComponent component = new TextComponent("§6[Report] §b" + commandSender.getName() + " à report §b" + target.getName() + " pour la raison §b" + message);
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + ((ProxiedPlayer) commandSender).getServer().getInfo().getName() + " report"));
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§a[Se téléporter]")}));
                        players.sendMessage(component);
                    }
                }
            } else {
                commandSender.sendMessage("§cErreur : vous n'avez pas la permission {euphalys.cmd.report}");
            }
        }
    }
}

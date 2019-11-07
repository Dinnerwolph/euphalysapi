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
        super("report", "euphalys.cmd.report");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            IEuphalysPlayer player = Euphalys.getInstance().getPlayer(((ProxiedPlayer) commandSender).getUniqueId());
                if (args.length < 2) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cPour reporter un joueur, merci de suivre cette synthaxe : /report <joueur> <raison>"));
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
            TextComponent component = new TextComponent("§6⚠ Signalement ⚠ > §e" + commandSender.getName() + "§6 a report §e" + target.getName() + "§➡ §e" + message + "\n §7(ID : " + Euphalys.getInstance().getReportManager().getNextId() + ")(Serveur :" + target.getServer().getInfo().getName() + ")" );
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + target.getServer().getInfo().getName() + " report"));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§a[Se téléporter]")}));
                for (ProxiedPlayer players : Euphalys.getInstance().getProxy().getPlayers()) {
                    if (Euphalys.getInstance().getPlayer(players.getUniqueId()).hasPermission("euphalys.moderation.viewreport")) {
                        players.sendMessage(component);
                    }
                }
                commandSender.sendMessage(TextComponent.fromLegacyText("wesh poto t'as signaler " + target.getName()));
                Euphalys.getInstance().getReportManager().addReport(Euphalys.getInstance().getPlayer(target.getUniqueId()), player, message);
            /**} else {
                commandSender.sendMessage("§cErreur : vous n'avez pas la permission {euphalys.cmd.report}");
            }*/
        }
    }
}

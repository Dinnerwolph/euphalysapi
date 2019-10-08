package net.euphalys.bungee.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.ISanctions;
import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class GUnmuteCommands extends Command {

    public GUnmuteCommands() {
        super("gunmute", "euphalys.cmd.gunmute");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(new TextComponent("/gunmute <playerName>"));
            return;
        }
        IEuphalysPlayer player = Euphalys.getInstance().getPlayer(args[0]);
        for (ISanctions sanction : player.getSanctions()) {
            if (sanction.getType().equals(SanctionsType.MUTE))
                Euphalys.getInstance().getSanctionsManager().removesanction(sanction.getSanctionsId());

        }
    }
}

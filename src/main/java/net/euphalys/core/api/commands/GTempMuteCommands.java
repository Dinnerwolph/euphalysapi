package net.euphalys.core.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.sanctions.SanctionsType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class GTempMuteCommands extends AbstractCommands {

    public GTempMuteCommands() {
        super("gtempmute", "euphalys.cmd.gtempmute");
    }

    private SanctionsType sanctionsType = SanctionsType.MUTE;

    @Override
    public boolean onCommand(Player player, String[] args) {
        IEuphalysPlayer azoPlayer = api.getPlayer(player.getUniqueId());
        if (args.length < 3)
            return false;
        UUID target = api.getUUUIDTranslator().getUUID(args[0]);
        IEuphalysPlayer targetplayer = api.getPlayer(target);
        String message = "";
        for (int i = 2; i < args.length; i++)
            message = message + args[i] + " ";
        String first = args[1].substring(0, 1);
        Player targetp = Bukkit.getPlayer(target);
        switch (args[1].substring(1)) {
            case "h":
                long i = 60 * 60 * Integer.parseInt(first);
                i = i + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) i, message, azoPlayer);
                return true;
            case "d":
                long d = 60 * 60 * 24 * Integer.parseInt(first);
                d = d + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) d, message, azoPlayer);
                return true;
            case "w":
                long w = 60 * 60 * 24 * 7 * Integer.parseInt(first);
                w = w + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) w, message, azoPlayer);
                return true;
            case "m":
                long m = 60 * 60 * 24 * 30 * Integer.parseInt(first);
                m = m + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) m, message, azoPlayer);
                return true;
            case "y":
                long y = 60 * 60 * 24 * 365 * Integer.parseInt(first);
                y = y + System.currentTimeMillis();
                api.getSanctionsManager().addGlobalSanction(targetplayer, sanctionsType, (int) y, message, azoPlayer);
                return true;
        }
        return false;
    }


    @Override
    protected void displayHelp() {
        player.sendMessage("/gtemmute <player> <duration> <message>");
    }
}

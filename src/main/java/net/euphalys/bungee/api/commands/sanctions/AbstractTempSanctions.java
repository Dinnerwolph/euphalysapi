package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.sanctions.SanctionsType;
import net.euphalys.core.api.EuphalysApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author Dinnerwolph
 */
public abstract class AbstractTempSanctions extends AbstractSanctions {

    private long duration;

    public AbstractTempSanctions(String name, String permission, SanctionsType sanctionsType) {
        super(name, permission, sanctionsType);
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        this.commandSender = commandSender;
        if (args.length == 0)
            displayHelp();
        else {
            if (!(args.length < 3)) {
                String message = "";
                for (int i = 2; i < args.length; i++)
                    message = message + args[i] + " ";
                try {
                    if (!onCommand(commandSender, args[0], getDuration(args[1]), message))
                        displayHelp();
                    duration = getDuration(args[1]);
                } catch (Exception e) {
                    commandSender.sendMessage(new TextComponent(e.getMessage()));
                }
            }
        }
    }

    private long getDuration(String duration) throws Exception {
        int first = Integer.parseInt(duration.substring(0, 1));
        switch (duration.substring(1)) {
            case "h":
                long i = 60 * 60 * first * 1000;
                i = i + System.currentTimeMillis();
                return i;
            case "d":
                long d = 60 * 60 * 24 * first * 1000;
                d = d + System.currentTimeMillis();
                return d;
            case "w":
                long w = 60 * 60 * 24 * 7 * first * 1000;
                w = w + System.currentTimeMillis();
                return w;
            case "m":
                long m = 60 * 60 * 24 * 30 * first * 1000;
                m = m + System.currentTimeMillis();
                return m;
            case "y":
                long y = 60 * 60 * 24 * 365 * first * 1000;
                y = y + System.currentTimeMillis();
                return y;
        }
        throw new Exception("Error time syntax.");
    }

    abstract boolean onCommand(CommandSender sender, String playerName, long duration, String message);

    @Override
    boolean onCommand(CommandSender player, String playerName, String message) {
        return false;
    }

    @Override
    protected void addGlobalSanction(String targetName, String message, int playerId) {
        EuphalysApi.getInstance().getSanctionsManager().addGlobalSanction(EuphalysApi.getInstance().getPlayer(targetName), sanctionsType, duration, message, playerId);
    }
}

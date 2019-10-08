package net.euphalys.bungee.api.commands.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.bungee.api.Euphalys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author Dinnerwolph
 */
public abstract class AbstractTempSanctions extends AbstractSanctions {

    public AbstractTempSanctions(String name, String permission) {
        super(name, permission);
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        this.commandSender = commandSender;
        if (args.length == 0)
            displayHelp();
        else {
            if (commandSender instanceof ProxiedPlayer) {
                IEuphalysPlayer player = Euphalys.getInstance().getPlayer(((ProxiedPlayer) commandSender).getUniqueId());
                if (!(args.length < 3)) {
                    String message = "";
                    for (int i = 2; i < args.length; i++)
                        message = message + args[i] + " ";
                    try {
                        if (!onCommand(player, args[0], getDuration(args[1]), message))
                            displayHelp();
                    } catch (Exception e) {
                        commandSender.sendMessage(new TextComponent(e.getMessage()));
                    }
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

    abstract boolean onCommand(IEuphalysPlayer player, String playerName, long duration, String message);

    @Override
    boolean onCommand(IEuphalysPlayer player, String playerName, String message) {
        return false;
    }
}

package net.euphalys.core.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.core.api.EuphalysApi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public abstract class AbstractCommands implements CommandExecutor {

    private final String permission;
    protected Player player;
    protected EuphalysApi api = EuphalysApi.getInstance();

    public AbstractCommands(String label, String permission) {
        this.permission = permission;
        api.getCommand(label).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            player = (Player) commandSender;
            IEuphalysPlayer azoplayer = api.getPlayer(player.getUniqueId());
            if (azoplayer.hasPermission(permission)) {
                if (!onCommand(player, strings)) {
                    displayHelp();
                }
                return true;
            }
            commandSender.sendMessage("§cVous n'avez pas le droit de faire ça.");
            return true;
        }
        commandSender.sendMessage("La console ne peut faire de commande.");
        return true;
    }

    protected abstract boolean onCommand(Player player, String[] args);

    protected abstract void displayHelp();
}

package net.euphalys.core.api.commands;

import net.euphalys.core.api.EuphalysApi;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

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
        api.getServer().getPluginManager().addPermission(new Permission(permission));
        if(this instanceof TabCompleter)
            api.getCommand(label).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            player = (Player) commandSender;
            if (commandSender.hasPermission(permission)) {
                if (!onCommand(player, strings)) {
                    displayHelp();
                }
                return true;
            }
            commandSender.sendMessage("§cErreur : Vous n'avez pas la permission (" + permission + ")");
            return true;
        }
        commandSender.sendMessage("La console ne peut faire de commande.");
        return true;
    }

    protected abstract boolean onCommand(Player player, String[] args);

    protected abstract void displayHelp();
}

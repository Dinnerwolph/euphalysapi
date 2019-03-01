package net.euphalys.core.api.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.utils.RankTabList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */
public class NickCommands extends AbstractCommands {

    public NickCommands() {
        super("nick", "cmd.epycube.nick");
    }


    @Override
    public boolean onCommand(Player player, String[] args) {
        IEuphalysPlayer euphalysPlayer = EuphalysApi.getInstance().getPlayer(player.getUniqueId());
        String arg = null;

        if (args.length == 0 || args.length > 1) {
            return false;
        }
        if (args[0].equalsIgnoreCase("off")) {
            arg = euphalysPlayer.getName();
        } else {
            arg = args[0];
        }
        if (args[0].length() > 16) {
            player.sendMessage("§cVous devez utilisez un pseudo de 16 charactere maximum !");
            return true;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (EuphalysApi.getInstance().getPlayerManager().exist(offlinePlayer.getUniqueId())) {
            player.sendMessage("Ce joueurs s'est déjà connecté sur le serveur, vous ne pouvez pas prendre ce pseudo.");
            return true;
        }
        EuphalysApi.getInstance().getNickUtils().setNickName(player, arg);
        player.setDisplayName(arg);
        euphalysPlayer.setNickName(arg);
        if (arg.equalsIgnoreCase(euphalysPlayer.getName())) {
            EuphalysApi.getInstance().getPlayerManager().setNickName(euphalysPlayer.getEuphalysId(), "");
        } else {
            EuphalysApi.getInstance().getPlayerManager().setNickName(euphalysPlayer.getEuphalysId(), arg);

        }
        RankTabList.updateRank(player);
        return true;
    }


    @Override
    public void displayHelp() {
        player.sendMessage("/nick <pseudo> : permet de changer de pseudo");
        player.sendMessage("/nick off : permet de reprendre son pseudo initial");
    }
}

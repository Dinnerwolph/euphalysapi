package net.euphalys.core.api.commands;

import net.euphalys.api.event.player.NickNameChangeEvent;
import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.player.EuphalysPlayer;
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
        String nickname;

        if (args.length == 0 || args.length > 1) {
            return false;
        }
        if (args[0].equalsIgnoreCase("off")) {
            nickname = euphalysPlayer.getName();
        } else {
            nickname = args[0];
        }
        if (args[0].length() > 16) {
            player.sendMessage("§cErreur : Votre pseudonyme doit faire 16 caractères maximum.");
            return true;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (EuphalysApi.getInstance().getPlayerManager().exist(offlinePlayer.getUniqueId())) {
            player.sendMessage("§cErreur : Ce joueur s'est déjà connecté sur le serveur. Vous ne pouvez pas prendre son pseudo.");
            return true;
        }
        NickNameChangeEvent event = new NickNameChangeEvent(player, nickname);
        String last = euphalysPlayer.getNickName();
        euphalysPlayer.setNickName(nickname);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            euphalysPlayer.setNickName(last);
            return true;
        }
        nickname = event.getNickname();

        EuphalysApi.getInstance().getNickUtils().setNickName(player, nickname);
        player.setDisplayName(nickname);
        if (nickname.equalsIgnoreCase(euphalysPlayer.getName())) {
            EuphalysApi.getInstance().getPlayerManager().setNickName(euphalysPlayer.getEuphalysId(), "");
        } else {
            EuphalysApi.getInstance().getPlayerManager().setNickName(euphalysPlayer.getEuphalysId(), nickname);

        }
        return true;
    }


    @Override
    public void displayHelp() {
        player.sendMessage("/nick <pseudo> : permet de changer de pseudo");
        player.sendMessage("/nick off : permet de reprendre son pseudo initial");
    }
}

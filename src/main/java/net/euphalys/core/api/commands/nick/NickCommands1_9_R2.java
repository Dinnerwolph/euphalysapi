package net.euphalys.core.api.commands.nick;

import com.mojang.authlib.GameProfile;
import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.bungee.api.Euphalys;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.commands.AbstractCommands;
import net.euphalys.core.api.utils.ProfileLoader;
import net.euphalys.core.api.utils.RankTabList;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author Dinnerwolph
 */
public class NickCommands1_9_R2 extends AbstractCommands {

    public NickCommands1_9_R2() {
        super("nick", "cmd.epycube.nick");
    }


    @Override
    public boolean onCommand(Player player, String[] args) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
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

        PacketPlayOutPlayerInfo removePacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        try {
            updateSkin(player, arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PacketPlayOutPlayerInfo addPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        for (Player players : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) players).getHandle().playerConnection.sendPacket(removePacket);
            ((CraftPlayer) players).getHandle().playerConnection.sendPacket(addPacket);
        }
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(entityPlayer.getId());
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(destroyPacket);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
            }
        }

        player.setDisplayName(arg);
        euphalysPlayer.setNickName(arg);
        //EuphalysApi.getInstance().playerMap.put(player.getUniqueId(), euphalysPlayer);
        if (arg.equalsIgnoreCase(entityPlayer.getName())) {
            EuphalysApi.getInstance().getPlayerManager().setNickName(euphalysPlayer.getGroup().getGroupId(), "null");
        } else {
            EuphalysApi.getInstance().getPlayerManager().setNickName(euphalysPlayer.getGroup().getGroupId(), arg);

        }
        RankTabList.updateRank(player);
        return true;
    }


    @Override
    public void displayHelp() {
        player.sendMessage("/nick <pseudo> : permet de changer de pseudo");
        player.sendMessage("/nick off : permet de reprendre son pseudo initial");
    }

    private void updateSkin(Player player, String name) throws IOException {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        //entityPlayer.getStatisticManager().e();
        if (entityPlayer.getChatFlags() == EntityHuman.EnumChatVisibility.HIDDEN) {
            throw new IOException("Player denis skin packets!");
        }
        ProfileLoader profileLoader = new ProfileLoader(player.getUniqueId().toString(), name, name);
        GameProfile gameProfile = profileLoader.loadProfile();
        try {
            Field field = entityPlayer.getClass().getSuperclass().getDeclaredField("bS");
            field.setAccessible(true);
            field.set(entityPlayer, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

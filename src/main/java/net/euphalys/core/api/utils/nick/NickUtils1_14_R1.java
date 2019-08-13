package net.euphalys.core.api.utils.nick;

import com.mojang.authlib.GameProfile;
import net.euphalys.core.api.utils.ProfileLoader;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author Dinnerwolph
 */
public class NickUtils1_14_R1 implements INickUtils {

    @Override
    public void setNickName(Player player, String nickname) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        try {
            updateSkin(entityPlayer, nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PacketPlayOutPlayerInfo removePacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutPlayerInfo addPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(entityPlayer.getId());
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!online.equals(player)) {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(removePacket);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(addPacket);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(destroyPacket);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
            }
        });
    }

    private void updateSkin(EntityPlayer player, String name) throws IOException {
        if (player.getChatFlags() == EnumChatVisibility.HIDDEN) {
            throw new IOException("Player denis skin packets!");
        }
        ProfileLoader profileLoader = new ProfileLoader(player.getUniqueID().toString(), name, name);
        GameProfile gameProfile = profileLoader.loadProfile();
        try {
            Field field = player.getClass().getSuperclass().getDeclaredField("bW");
            field.setAccessible(true);
            field.set(player, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

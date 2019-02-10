package net.euphalys.core.api.customentities;

import net.minecraft.server.v1_9_R2.EntityLiving;
import net.minecraft.server.v1_9_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;


/**
 * @author Dinnerwolph
 */

public class CustomEntityManager {

    public static void make(EntityLiving nmsEntity, Location location) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        nmsEntity.setPosition(location.getX(), location.getY(), location.getZ());
        nmsWorld.addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
}

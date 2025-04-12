package com.gmail.goosius.siegewar.listeners;

import com.gmail.goosius.siegewar.utils.SiegeWarDistanceUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Set;

public class CreatureSpawnEventListener implements Listener {

    private static final Set<EntityType> HOSTILE_MOBS = Set.of(
            // === Overworld Hostile Mobs ===
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.ENDERMAN,
            EntityType.WITCH,
            EntityType.SLIME,
            EntityType.DROWNED,
            EntityType.PHANTOM,
            EntityType.SILVERFISH,
            EntityType.PILLAGER,
            EntityType.VEX,
            EntityType.EVOKER,
            EntityType.ILLUSIONER,
            EntityType.RAVAGER,

            // === Nether Hostile Mobs ===
            EntityType.BLAZE,
            EntityType.GHAST,
            EntityType.MAGMA_CUBE,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.HOGLIN,
            EntityType.ZOGLIN,
            EntityType.WITHER_SKELETON,
            EntityType.ZOMBIFIED_PIGLIN,

            // === End Hostile Mobs ===
            EntityType.SHULKER,
            EntityType.ENDERMITE,

            // === Universal or Boss Hostile Mobs ===
            // EntityType.WARDEN,
            EntityType.WITHER,
            EntityType.ELDER_GUARDIAN,
            EntityType.GUARDIAN
    );

    private static boolean isMonster(EntityType entityType) {
        return HOSTILE_MOBS.contains(entityType);
    }


    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent creatureSpawnEvent){

        if( isMonster(creatureSpawnEvent.getEntityType()) && SiegeWarDistanceUtil.isLocationInActiveSiegeZone(creatureSpawnEvent.getLocation()) ){
            creatureSpawnEvent.setCancelled(true);
        }

    }

}

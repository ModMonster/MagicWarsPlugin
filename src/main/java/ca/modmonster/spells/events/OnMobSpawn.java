package ca.modmonster.spells.events;

import ca.modmonster.spells.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Arrays;
import java.util.List;

public class OnMobSpawn implements Listener {
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        List<CreatureSpawnEvent.SpawnReason> spawnsToCancel = Arrays.asList(
            CreatureSpawnEvent.SpawnReason.NATURAL,
            CreatureSpawnEvent.SpawnReason.BEEHIVE,
            CreatureSpawnEvent.SpawnReason.MOUNT,
            CreatureSpawnEvent.SpawnReason.VILLAGE_INVASION,
            CreatureSpawnEvent.SpawnReason.NETHER_PORTAL,
            CreatureSpawnEvent.SpawnReason.ENDER_PEARL,
            CreatureSpawnEvent.SpawnReason.JOCKEY,
            CreatureSpawnEvent.SpawnReason.PATROL,
            CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE
        );

        if (spawnsToCancel.contains(event.getSpawnReason())) event.setCancelled(true);
    }
}

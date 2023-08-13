package ca.modmonster.spells.events;

import ca.modmonster.spells.item.spell.spells.minion.Minion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class OnPlayerInteractWithEntity implements Listener {
    @EventHandler
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event) {
        preventSpawningBabyMobsWithMinions(event);
    }

    void preventSpawningBabyMobsWithMinions(PlayerInteractEntityEvent event) {
        if (Minion.isMinion(event.getRightClicked())) event.setCancelled(true);
    }
}

package ca.modmonster.spells.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class OnPlayerCraft implements Listener {
    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }
}

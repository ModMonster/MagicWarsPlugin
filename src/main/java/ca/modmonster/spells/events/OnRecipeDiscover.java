package ca.modmonster.spells.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;

public class OnRecipeDiscover implements Listener {
    @EventHandler
    public void onRecipeDiscover(PlayerRecipeDiscoverEvent event) {
        event.setCancelled(true);
    }
}

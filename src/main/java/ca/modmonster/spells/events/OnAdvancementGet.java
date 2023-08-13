package ca.modmonster.spells.events;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnAdvancementGet implements Listener {
    @EventHandler
    public void onAdvancementGet(PlayerAdvancementCriterionGrantEvent event) {
        event.setCancelled(true);
    }
}

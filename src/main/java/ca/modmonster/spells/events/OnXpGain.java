package ca.modmonster.spells.events;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnXpGain implements Listener {
    @EventHandler
    public void onXpGain(PlayerPickupExperienceEvent event) {
        event.getExperienceOrb().setExperience(0);
    }
}

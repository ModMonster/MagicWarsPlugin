package ca.modmonster.spells.events;

import ca.modmonster.spells.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.UUID;

public class OnEntityTarget implements Listener {
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        String minionOwnerUUID = Utilities.getPersistentEntityTagString(entity, "minion_owner");

        // not a minion
        if (minionOwnerUUID == null) {
            return;
        }

        Player minionOwner = Bukkit.getPlayer(UUID.fromString(minionOwnerUUID));

        if (event.getTarget() != null && event.getTarget().equals(minionOwner)) {
            event.setCancelled(true);
        }
    }
}

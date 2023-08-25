package ca.modmonster.spells.events;

import ca.modmonster.spells.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission("magicwars.breakblocks") && GameManager.allowedBuildingPlayers.contains(event.getPlayer())) return;
        event.setCancelled(true);
    }
}

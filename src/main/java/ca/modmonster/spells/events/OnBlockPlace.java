package ca.modmonster.spells.events;

import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission("magicwars.breakblocks") && GameManager.allowedBuildingPlayers.contains(event.getPlayer())) return;

        // prevent building too high
        if (event.getBlockPlaced().getY() > GameManager.activeGame.world.map.maxBuildHeight) {
            event.setCancelled(true);
            event.getPlayer().sendActionBar(Utilities.stringToComponent("&cYou can't build this high!"));
        }
    }
}

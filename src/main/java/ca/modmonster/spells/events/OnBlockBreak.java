package ca.modmonster.spells.events;

import ca.modmonster.spells.game.GameManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        preventBlockBreakingInGame(event);
    }

    void preventBlockBreakingInGame(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (GameManager.blockBreakWhitelist.contains(block.getType())) {
            event.setDropItems(false);
            return;
        }
        if (player.hasPermission("magicwars.breakblocks") && GameManager.allowedBuildingPlayers.contains(player)) return;

        event.setCancelled(true);
    }
}

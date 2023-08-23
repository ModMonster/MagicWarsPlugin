package ca.modmonster.spells.events;

import ca.modmonster.spells.Spells;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class OnInventoryClose implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        handleDeletingEmptyChests(event);
    }

    void handleDeletingEmptyChests(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (!inventory.getType().equals(InventoryType.CHEST)) return;
        if (!inventory.isEmpty()) return;
        if (inventory.getLocation() == null) return;

        // remove chest
        new BukkitRunnable() {
            @Override
            public void run() {
                Block chest = inventory.getLocation().getBlock();
                chest.setType(Material.AIR);

                chest.getLocation().getWorld().spawnParticle(Particle.REDSTONE, chest.getLocation().clone().add(0.5, 0.5, 0.5), 5, 0.25, 0.25, 0.25, new Particle.DustOptions(Color.WHITE, 1));
            }
        }.runTaskLater(Spells.main, 12);
    }
}

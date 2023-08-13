package ca.modmonster.spells.events;

import ca.modmonster.spells.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class OnDropItem implements Listener {
    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (droppedItem.getItemMeta().equals(GameManager.getLobbyCompassUsableInWorld().getItemMeta())) {event.setCancelled(true); return;}
    }
}

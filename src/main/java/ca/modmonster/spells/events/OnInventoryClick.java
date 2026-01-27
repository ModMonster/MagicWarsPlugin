package ca.modmonster.spells.events;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.game.LootChest;
import ca.modmonster.spells.gui.BrowseEnchantmentsGui;
import ca.modmonster.spells.gui.BrowseSpellsGui;
import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.spell.SpellManager;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        handleEnchanting(event);
        BrowseSpellsGui.onClick(event);
        BrowseEnchantmentsGui.onClick(event);
        handleBlockedSlots(event);
        handleRemovingEquipArmorLore(event);
        handleAutoEquippingArmor(event);

        handleTrashSlot(event);
        doLobbyCompass(event);
    }

    void handleEnchanting(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack book = player.getItemOnCursor();
        if (!(book.getItemMeta() instanceof EnchantmentStorageMeta)) return;

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().equals(Material.AIR)) return;
        if (SpellManager.isSpell(item)) return;
        if (item.getItemMeta().equals(Game.blockerStack.getItemMeta())) return;
        if (item.getItemMeta().equals(Game.trashStack.getItemMeta())) {
            return;
        }

        event.setCancelled(true);

        // get the enchantment on the book
        Map<CustomEnchantment, Integer> bookEnchants = EnchantmentManager.getEnchantmentsFromItem(book);
        Map.Entry<CustomEnchantment, Integer> newEnchant = bookEnchants.entrySet().iterator().next();

        List<Material> applicableMaterials = newEnchant.getKey().getType().getEnchantableMaterials();

        // item in item slot isn't applicable to enchantment
        if (!applicableMaterials.contains(item.getType())) {
            PlaySound.error(player);
            return;
        }

        // get item's current enchantments
        Map<CustomEnchantment, Integer> itemEnchants = EnchantmentManager.getEnchantmentsFromItem(item);

        // enchantment conflicts with existing enchantment
        for (Map.Entry<CustomEnchantment, Integer> entry : itemEnchants.entrySet()) {
            if (entry.getKey().conflictsWith(newEnchant.getKey())) {
                PlaySound.error(player);
                return;
            }
        }

        EnchantmentManager.addEnchant(item, newEnchant.getKey(), newEnchant.getValue());
        player.setItemOnCursor(null); // delete book
    }

    void doLobbyCompass(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
        ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
        if (!itemMeta.equals(GameManager.getLobbyCompass().getItemMeta()) && !itemMeta.equals(GameManager.getLobbyCompassUsableInWorld().getItemMeta())) return;

        player.sendMessage(Utilities.getStatusMessage(Utilities.StatusMessageType.TELEPORT, "Sending you to &lLobby"));
        Utilities.bungeeServerSend(player, Spells.mainConfig.getString("lobby-server"));
    }

    void handleRemovingEquipArmorLore(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode().equals(GameMode.SPECTATOR)) return;
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (item.lore() == null) return;
        if (!item.lore().contains(LootChest.ARMOR_EQUIP_LORE)) return;

        if (Utilities.isMaterialArmor(item.getType())) item.lore(null);
    }

    void handleAutoEquippingArmor(InventoryClickEvent event) {
        if (!event.isShiftClick()) return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().equals(event.getWhoClicked().getInventory())) return;
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (item == null) return;
        if (!Utilities.isMaterialArmor(item.getType())) return;
        if (player.getGameMode().equals(GameMode.SPECTATOR)) return;

        EquipmentSlot armorSlot = Utilities.getArmorEquipmentSlotOfMaterial(item.getType());
        if (armorSlot == null) return;

        ItemStack itemInArmorSlot = player.getEquipment().getItem(armorSlot);
        int slot = event.getSlot();

        event.getClickedInventory().setItem(slot, itemInArmorSlot);
        player.getEquipment().setItem(armorSlot, item);
        event.setCancelled(true);
    }

    void handleTrashSlot(InventoryClickEvent event) {
        if (event.isCancelled()) return;

        ItemStack item = event.getCurrentItem();

        if (item == null) return;
        if (item.getItemMeta() == null) return;

        if (event.isRightClick()) {
            if (item.getItemMeta().equals(Game.blockerStack.getItemMeta())) return;
            if (item.getItemMeta().equals(Game.trashStack.getItemMeta())) {
                event.setCancelled(true);
                return;
            }

            // right-clicked item to trash slot
            event.setCurrentItem(null);
            event.setCancelled(true);
        } else {
            if (!item.getItemMeta().equals(Game.trashStack.getItemMeta())) return;

            // clicked on trash slot
            event.getWhoClicked().setItemOnCursor(null);
            event.setCancelled(true);
        }
    }

    void handleBlockedSlots(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();

        if (item == null) return;
        if (item.getItemMeta() == null) return;
        if (!item.getItemMeta().equals(Game.blockerStack.getItemMeta())) return;

        // slot is blocked
        event.setCancelled(true);
    }
}

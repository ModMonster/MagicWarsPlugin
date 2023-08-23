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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        ItemStack itemOnCursor = player.getItemOnCursor();
        if (!(itemOnCursor.getItemMeta() instanceof EnchantmentStorageMeta)) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;
        if (SpellManager.isSpell(clickedItem)) return;
        if (clickedItem.getItemMeta().equals(Game.blockerStack.getItemMeta())) return;
        if (clickedItem.getItemMeta().equals(Game.trashStack.getItemMeta())) {
            return;
        }

        event.setCancelled(true);

        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) itemOnCursor.getItemMeta();
        Enchantment bookEnchantment = bookMeta.getStoredEnchants().keySet().stream().findFirst().get();
        CustomEnchantment bookCustomEnchantment = EnchantmentManager.getEnchantmentFromBukkit(bookEnchantment);

        assert bookCustomEnchantment != null;
        List<Material> applicableMaterials = bookCustomEnchantment.type.getEnchantableMaterials();

        // item in item slot isn't applicable to enchantment
        if (!applicableMaterials.contains(clickedItem.getType())) {
            PlaySound.error(player);
            return;
        }

        // enchantment conflicts with existing enchantment
        for (Enchantment enchantment : clickedItem.getEnchantments().keySet()) {
            CustomEnchantment customEnchantment = EnchantmentManager.getEnchantmentFromBukkit(enchantment);
            if (customEnchantment == null) continue;
            if (customEnchantment.conflictsWith(bookCustomEnchantment)) {
                PlaySound.error(player);
                return;
            }
        }

        EnchantmentManager.enchantItem(clickedItem, bookMeta.getStoredEnchants()); // set enchantments on item
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
        if (!item.lore().contains(LootChest.armorEquipLore)) return;

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

package ca.modmonster.spells.gui;

import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BrowseEnchantmentsGui {
    private static Inventory inventory;
    public static final Component title = Component.text("Browse Enchantments");

    public BrowseEnchantmentsGui() {
        // create the inventory
        inventory = Bukkit.createInventory(null, 36, title);

        resetInventory();
    }

    public static void resetInventory() {
        inventory.clear();

        for (CustomEnchantment enchantment : EnchantmentManager.enchantments) {
            for (int i = 1; i < enchantment.maxLevel + 1; i++) {
                inventory.addItem(EnchantmentManager.getEnchantedBook(enchantment, i));
            }
        }
    }

    /**
     * Gets a material that can be enchanted with the given enchantment.
     * @param type the EnchantmentType to get item for
     * @return best material applicable to the item
     */
    public static Material getApplicableItem(@NotNull EnchantmentType type) {
        Map<EnchantmentType, Material> applicableItemMap = new HashMap<EnchantmentType, Material>() {
            {
                put(EnchantmentType.SWORD, Material.DIAMOND_SWORD);
                put(EnchantmentType.ARMOR, Material.DIAMOND_CHESTPLATE);
                put(EnchantmentType.HELMET, Material.DIAMOND_HELMET);
                put(EnchantmentType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
                put(EnchantmentType.LEGGINGS, Material.DIAMOND_LEGGINGS);
                put(EnchantmentType.BOOTS, Material.DIAMOND_BOOTS);
            }
        };

        return applicableItemMap.get(type);
    }

    public static void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();

        if (!event.getView().title().equals(title) || event.getRawSlot() >= event.getView().getTopInventory().getSize()) {
            return;
        }

        // no item in slot
        if (item == null || item.getType().isAir()) {
            return;
        }

        if (event.isShiftClick()) {
            // give player enchanted item
            Map<CustomEnchantment, Integer> enchantments = EnchantmentManager.getEnchantmentsFromBook(event.getCurrentItem());

            ItemStack newItem = new ItemStack(getApplicableItem(enchantments.keySet().stream().findFirst().get().type));

            EnchantmentManager.enchantItem(newItem, enchantments.keySet().stream().findFirst().get().bukkitEnchantment, enchantments.get(enchantments.keySet().stream().findFirst().get()));

            event.getWhoClicked().getInventory().addItem(newItem);
        } else {
            // give player book
            event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
        }

        // cancel event
        event.setCancelled(true);
    }

    public void openInventory(final HumanEntity entity) {
        entity.openInventory(inventory);
    }
}

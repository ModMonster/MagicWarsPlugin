package ca.modmonster.spells.gui;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.spell.SpellManager;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class EnchanterGui {
    private static Inventory inventory;
    public static final Component title = Component.text("Enchant Items");

    public static final Integer itemSlot = 10;
    public static final Integer bookSlot = 12;
    public static final Integer outputSlot = 16;

    public static ItemStack itemPlaceholderItem;
    public static ItemStack bookPlaceholderItem;
    public static ItemStack failedToEnchantItem;
    public static ItemStack blockedSlotStack;

    public EnchanterGui() {
        // create the inventory
        inventory = Bukkit.createInventory(null, 27, title);

        resetInventory();
    }

    public static void resetInventory() {
        inventory.clear();

        blockedSlotStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, blockedSlotStack);
        }

        // add item placeholder item
        itemPlaceholderItem = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = itemPlaceholderItem.getItemMeta(); // get item metadata
        meta.displayName(Utilities.stringToComponentWithoutItalic("&3Insert item here to enchant.")); // set display name
        itemPlaceholderItem.setItemMeta(meta); // set item metadata

        // add book placeholder item
        bookPlaceholderItem = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        meta = bookPlaceholderItem.getItemMeta(); // get item metadata
        meta.displayName(Utilities.stringToComponentWithoutItalic("&3Insert enchanted book here.")); // set display name
        bookPlaceholderItem.setItemMeta(meta); // set item metadata

        // add failed to enchant item
        failedToEnchantItem = new ItemStack(Material.BARRIER); // make new barrier item
        meta = failedToEnchantItem.getItemMeta(); // get item metadata
        meta.displayName(Utilities.stringToComponentWithoutItalic("&cCan't enchant!")); // set display name
        failedToEnchantItem.setItemMeta(meta); // set item metadata

        // add placeholder items
        inventory.setItem(itemSlot, itemPlaceholderItem);
        inventory.setItem(bookSlot, bookPlaceholderItem);
        inventory.setItem(outputSlot, failedToEnchantItem);
    }

    static void update(InventoryClickEvent event) {
        ItemStack itemInItemSlot = event.getInventory().getItem(itemSlot);
        ItemStack itemInBookSlot = event.getInventory().getItem(bookSlot);

        // some items are placeholders
        if (itemInItemSlot.equals(itemPlaceholderItem) || itemInBookSlot.equals(bookPlaceholderItem)) {
            event.getInventory().setItem(outputSlot, failedToEnchantItem);
            return;
        }

        // item in book slot isn't enchanted book
        if (!(itemInBookSlot.getItemMeta() instanceof EnchantmentStorageMeta)) {
            event.getInventory().setItem(outputSlot, failedToEnchantItem);
            return;
        }

        // item in slot is a spell
        if (SpellManager.isSpell(itemInItemSlot) || SpellManager.isSpell(itemInBookSlot)) {
            event.getInventory().setItem(outputSlot, failedToEnchantItem);
            return;
        }

        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) itemInBookSlot.getItemMeta();
        Enchantment bookEnchantment = bookMeta.getStoredEnchants().keySet().stream().findFirst().get();
        CustomEnchantment bookCustomEnchantment = EnchantmentManager.getEnchantmentFromBukkit(bookEnchantment);
        List<Material> applicableMaterials = bookCustomEnchantment.type.getEnchantableMaterials();

        // item in item slot isn't applicable to enchantment
        if (!applicableMaterials.contains(itemInItemSlot.getType())) {
            event.getInventory().setItem(outputSlot, failedToEnchantItem);
            return;
        }

        // enchantment conflicts with existing enchantment
        for (Enchantment enchantment : itemInItemSlot.getEnchantments().keySet()) {
            CustomEnchantment customEnchantment = EnchantmentManager.getEnchantmentFromBukkit(enchantment);

            if (customEnchantment.conflictsWith(bookCustomEnchantment)) {
                event.getInventory().setItem(outputSlot, failedToEnchantItem);
                return;
            }
        }

        ItemStack outputItem = itemInItemSlot.clone(); // get output item
        EnchantmentManager.enchantItem(outputItem, bookMeta.getStoredEnchants()); // set enchantments on item

        event.getInventory().setItem(outputSlot, outputItem);
    }

    public static void onClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(title)) return;

        ItemStack item = event.getCurrentItem();
        ItemStack itemOnCursor = event.getWhoClicked().getItemOnCursor();

        if (item == null) return;

        // clicked a number key to add to slot
        if (event.getClick().equals(ClickType.NUMBER_KEY) && (item.equals(itemPlaceholderItem) || item.equals(bookPlaceholderItem))) {
            Inventory topInventory = event.getInventory();
            ItemStack itemToMove = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());

            // move item to slot
            topInventory.setItem(event.getSlot(), itemToMove);
            event.getWhoClicked().getInventory().setItem(event.getHotbarButton(), null);

            update(event);
            event.setCancelled(true);
            return;
        }

        // clicked on item or book placeholder without holding item
        if (itemOnCursor.getType().equals(Material.AIR) && (item.equals(itemPlaceholderItem) || item.equals(bookPlaceholderItem))) {
            event.setCancelled(true);
            return;
        }

        // clicked on blocked slot or failed to enchant item
        if (item.equals(blockedSlotStack) || item.equals(failedToEnchantItem)) {
            event.setCancelled(true);
            return;
        }

        // clicked on output slot with item on cursor
        if (event.getRawSlot() < event.getInventory().getSize() && event.getSlot() == outputSlot && !itemOnCursor.getType().equals(Material.AIR)) {
            event.setCancelled(true);
            return;
        }

        // removed item from item or book slot
        if (itemOnCursor.getType().equals(Material.AIR) && event.getRawSlot() < event.getInventory().getSize() && (event.getSlot() == itemSlot || event.getSlot() == bookSlot)) {
            if (event.isShiftClick()) {
                // shift click
                event.getWhoClicked().getInventory().setItem(Utilities.getValidShiftClickSlot(event.getWhoClicked().getInventory()), item);
            } else if (event.getClick().equals(ClickType.NUMBER_KEY)) {
                // number key
                ItemStack itemInSwapSlot = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());

                // is slot full
                if (itemInSwapSlot != null && !itemInSwapSlot.getType().equals(Material.AIR)) {
                    event.setCancelled(true);
                    return;
                }

                // move item to slot
                event.getWhoClicked().getInventory().setItem(event.getHotbarButton(), item);
            } else {
                // regular click
                event.getWhoClicked().setItemOnCursor(item);
            }

            if (event.getSlot() == itemSlot) {
                event.setCurrentItem(itemPlaceholderItem);
            } else {
                event.setCurrentItem(bookPlaceholderItem);
            }

            update(event);
            event.setCancelled(true);
            return;
        }

        // removed item from output slot
        if (itemOnCursor.getType().equals(Material.AIR) && event.getRawSlot() < event.getInventory().getSize() && event.getSlot() == outputSlot) {
            if (event.isShiftClick()) {
                // shift click
                event.getWhoClicked().getInventory().setItem(Utilities.getValidShiftClickSlot(event.getWhoClicked().getInventory()), item);
            } else if (event.getClick().equals(ClickType.NUMBER_KEY)) {
                // number key
                ItemStack itemInSwapSlot = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());

                // is slot full
                if (itemInSwapSlot != null && !itemInSwapSlot.getType().equals(Material.AIR)) {
                    event.setCancelled(true);
                    return;
                }

                // move item to slot
                event.getWhoClicked().getInventory().setItem(event.getHotbarButton(), item);
            } else {
                // regular click
                event.getWhoClicked().setItemOnCursor(item);
            }

            event.getInventory().setItem(itemSlot, itemPlaceholderItem);
            event.getInventory().setItem(bookSlot, bookPlaceholderItem);

            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);

            update(event);
            event.setCancelled(true);
            return;
        }

        // clicked on item or book placeholder with item
        if (item.equals(itemPlaceholderItem) || item.equals(bookPlaceholderItem)) {
            event.setCurrentItem(itemOnCursor);
            event.getWhoClicked().setItemOnCursor(null);
            update(event);
            event.setCancelled(true);
            return;
        }

        // shift-clicked on item to add to slot
        if (event.isShiftClick()) {
            Inventory topInventory = event.getInventory();

            if (item.getType().equals(Material.ENCHANTED_BOOK) && (topInventory.getItem(bookSlot) != null && topInventory.getItem(bookSlot).equals(bookPlaceholderItem))) {
                // move item to book slot
                topInventory.setItem(bookSlot, item);
                event.getClickedInventory().setItem(event.getSlot(), null);
            } else if (topInventory.getItem(itemSlot) != null && topInventory.getItem(itemSlot).equals(itemPlaceholderItem)) {
                // move item to item slot
                topInventory.setItem(itemSlot, item);
                event.getClickedInventory().setItem(event.getSlot(), null);
            }

            update(event);
            event.setCancelled(true);
            return;
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                update(event);
            }
        }.runTaskLater(Spells.main, 1);
    }

    public void openInventory(final HumanEntity entity) {
        entity.openInventory(inventory);
    }
}

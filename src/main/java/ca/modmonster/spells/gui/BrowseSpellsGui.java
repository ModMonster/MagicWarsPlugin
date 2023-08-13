package ca.modmonster.spells.gui;

import ca.modmonster.spells.item.spell.Power;
import ca.modmonster.spells.item.spell.Spell;
import ca.modmonster.spells.item.spell.SpellManager;
import ca.modmonster.spells.util.PlaySound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BrowseSpellsGui {
    private static Inventory inventory;
    public static final Component title = Component.text("Browse Spells");

    public static ItemStack minusButton;
    public static ItemStack plusButton;

    public static Power currentPower = Power.POWERFUL;

    public BrowseSpellsGui() {
        // get size
        int spellCount = SpellManager.spells.size();
        int rowCount = (int) Math.ceil((double) spellCount / 9.0);

        // create the inventory
        inventory = Bukkit.createInventory(null, rowCount * 9 + 9, title);

        resetInventory();
    }

    public static void resetInventory() {
        inventory.clear();

        for (Spell spell : SpellManager.spells) {
            inventory.addItem(SpellManager.getSpell(spell, currentPower));
        }

        // set power bar at bottom
        int powerBarStartSlot = inventory.getSize() - 9;

        // set black bar
        inventory.setItem(powerBarStartSlot, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        inventory.setItem(powerBarStartSlot + 2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        inventory.setItem(powerBarStartSlot + 8, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        inventory.setItem(powerBarStartSlot + 6, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // add minus button
        ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE); // get itemstack
        ItemMeta meta = itemStack.getItemMeta(); // get metadata
        meta.displayName(Component.text("Decrease Power", NamedTextColor.RED, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)); // set display name
        itemStack.setItemMeta(meta); // set metadata
        inventory.setItem(powerBarStartSlot + 1, itemStack); // add item to inventory

        minusButton = itemStack;

        // add plus button
        itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE); // get itemstack
        meta = itemStack.getItemMeta(); // get metadata
        meta.displayName(Component.text("Increase Power", NamedTextColor.GREEN, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)); // set display name
        itemStack.setItemMeta(meta); // set metadata
        inventory.setItem(powerBarStartSlot + 7, itemStack); // add item to inventory

        plusButton = itemStack;

        // add power bar
        for (int i = 0; i < 3; i++) {
            if (i < currentPower.number) {
                itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            } else {
                itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            }

            meta = itemStack.getItemMeta(); // get metadata
            meta.displayName(Component.text(currentPower.name, currentPower.color, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)); // set display name
            itemStack.setItemMeta(meta);

            inventory.setItem(powerBarStartSlot + 3 + i, itemStack);
        }
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

        // remove power
        if (item.equals(minusButton)) {
            if (!(currentPower == Power.WEAK)) {
                currentPower = Power.get(currentPower.number - 1);
                resetInventory();
            }

            PlaySound.click((Player) event.getWhoClicked());

            event.setCancelled(true);
            return;
        }

        // add power
        if (item.equals(plusButton)) {
            if (!(currentPower == Power.POWERFUL)) {
                currentPower = Power.get(currentPower.number + 1);
                resetInventory();
            }

            PlaySound.click((Player) event.getWhoClicked());

            event.setCancelled(true);
            return;
        }

        // power bar
        if (item.getType().equals(Material.LIME_STAINED_GLASS_PANE) || item.getType().equals(Material.RED_STAINED_GLASS_PANE) || item.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
            event.setCancelled(true);
            return;
        }

        Spell spell = SpellManager.getSpellFromItem(item);

        // give player item
        if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            if (spell != null && !spell.getStackable()) {
                event.getWhoClicked().getInventory().addItem(SpellManager.getSpell(spell, currentPower));
            } else {
                event.getWhoClicked().getInventory().addItem(SpellManager.getSpell(spell, currentPower).asQuantity(64));
            }
        } else {
            event.getWhoClicked().getInventory().addItem(SpellManager.getSpell(spell, currentPower));
        }

        // cancel event
        event.setCancelled(true);
    }

    public void openInventory(final HumanEntity entity) {
        entity.openInventory(inventory);
    }
}

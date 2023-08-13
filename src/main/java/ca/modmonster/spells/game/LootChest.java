package ca.modmonster.spells.game;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.spell.Power;
import ca.modmonster.spells.item.spell.Spell;
import ca.modmonster.spells.item.spell.SpellManager;
import ca.modmonster.spells.util.Icons;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LootChest {
    public static final List<WeightedItem> vanillaItems = new ArrayList<>();
    public final Vector location;
    public final boolean canContainEnchantedBooks;

    public static Component armorEquipLore = Utilities.stringToComponentWithoutItalic("&8" + Icons.RIGHT_ARROW + " &b&lSHIFT + CLICK&3 to equip");

    public static void init() {
        @SuppressWarnings("unchecked")
        List<List<Object>> vanillaLootItems = (List<List<Object>>) Spells.mainConfig.getList("loot");

        assert vanillaLootItems != null;
        for (List<Object> list : vanillaLootItems) {
            WeightedItem item = new WeightedItem(
                Material.valueOf((String) list.get(0)),
                null,
                null,
                (Integer) list.get(1),
                (Integer) list.get(2)
            );

            vanillaItems.add(item);
        }
    }

    public LootChest(Vector location, boolean canContainEnchantedBooks) {
        this.location = location;
        this.canContainEnchantedBooks = canContainEnchantedBooks;
    }

    public static void spawnLootChest(World world, LootChest chest, boolean alwaysShow) {
        Location location = Utilities.vectorToBlockLocation(world, chest.location);
        double notShowChance = alwaysShow? 0 : 0.5;

        if (new Random().nextDouble() < notShowChance) {
            if (!location.getBlock().getType().equals(Material.AIR)) {
                location.getBlock().setType(Material.AIR);
            }
            return;
        }

        // set block to chest
        if (!location.getBlock().getType().equals(Material.CHEST)) {
            location.getBlock().setType(Material.CHEST);
        }

        // add items to chest
        Inventory inventory = ((Chest) location.getBlock().getState()).getBlockInventory();
        fillChest(inventory, chest.canContainEnchantedBooks);
    }

    static void fillChest(Inventory inventory, boolean canContainEnchantedBooks) {
        // constants
        final int minItems = canContainEnchantedBooks? 2 : 4;
        final int maxItems = canContainEnchantedBooks? 5 : 10;

        inventory.clear();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // get number of items to spawn
        int itemCount = random.nextInt((maxItems + 1) - minItems) + minItems;

        // get list of items
        List<WeightedItem> items = new ArrayList<>();

        // add spells to list
        for (Spell spell : SpellManager.spells) {
            Integer maxStackSize = spell.getStackableInChests()? spell.getRarity().stackableMaxStackSize : 1;
            WeightedItem spellWeightedItem = new WeightedItem(null, spell, null, spell.getRarity().rarityWeight, maxStackSize);

            items.add(spellWeightedItem);
        }

        // add vanilla items to list
        items.addAll(vanillaItems);

        // add enchantments to list
        if (canContainEnchantedBooks) {
            for (CustomEnchantment enchantment : EnchantmentManager.enchantments) {
                Integer weight = Math.round(Float.valueOf(enchantment.rarity.rarityWeight) / 2);

                WeightedItem enchantmentWeightedItem = new WeightedItem(null, null, enchantment, weight, 1);

                items.add(enchantmentWeightedItem);
            }
        }

        // loop for each item to add
        for (int i = 0; i < itemCount; i++) {
            // choose item from weight
            WeightedItem chosenItem = chooseItem(items, random);

            // choose count
            int count = random.nextInt(chosenItem.maxStackSize) + 1;

            // choose random slot
            Integer slot = chooseSlot(inventory, random);

            if (chosenItem.isSpell()) {
                // choose power randomly
                Power power = Power.get(random.nextInt(3) + 1);

                // set item in slot
                inventory.setItem(slot, SpellManager.getSpell(chosenItem.spell, power).asQuantity(count));
            } else if (chosenItem.isEnchantment()) {
                // choose level randomly
                Integer level = random.nextInt(chosenItem.enchantment.maxLevel) + 1;

                inventory.setItem(slot, EnchantmentManager.getEnchantedBook(chosenItem.enchantment, level));
            } else {
                ItemStack itemStack = new ItemStack(chosenItem.material, count);
                ItemMeta meta = itemStack.getItemMeta();

                // make item unbreakable
                if (itemStack.getType().getMaxDurability() != 0) meta.setUnbreakable(true);

                // add item equip lore
                if (Utilities.isMaterialArmor(itemStack.getType())) {
                    meta.lore(Collections.singletonList(armorEquipLore));
                }

                itemStack.setItemMeta(meta);

                // set item in slot
                inventory.setItem(slot, itemStack);
            }

            // remove from list to prevent duplicates
            items.remove(chosenItem);
        }
    }

    static Integer chooseSlot(Inventory inventory, ThreadLocalRandom random) {
        int slot = -1;

        while (slot == -1 || (!(inventory.getItem(slot) == null) && !inventory.getItem(slot).getType().isAir())) {
            slot = random.nextInt(inventory.getSize());
        }

        return slot;
    }

    static WeightedItem chooseItem(List<WeightedItem> items, ThreadLocalRandom random) {
        // i have literally no idea how this code works
        // https://stackoverflow.com/questions/6737283/weighted-randomness-in-java

        // set up item weights
        double totalWeight = 0;
        for (WeightedItem item : items) {
            totalWeight += item.weight;
        }

        // choose item randomly
        int index = 0;
        for (double r = random.nextDouble(totalWeight); index < items.size() - 1; ++index) {
            r -= items.get(index).weight;
            if (r <= 0) break;
        }

        return items.get(index);
    }
}

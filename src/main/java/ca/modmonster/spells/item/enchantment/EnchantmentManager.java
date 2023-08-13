package ca.modmonster.spells.item.enchantment;

import ca.modmonster.spells.item.enchantment.enchantments.*;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EnchantmentManager {
    public static final List<CustomEnchantment> enchantments = Arrays.asList(
        new PoisonEnchantment(),
        new LaunchEnchantment(),
        new ZeusFuryEnchantment(),
        new BlindingEnchantment(),
        new MoltenEnchantment(),
        new RejuvenationEnchantment(),
        new SelfDestructEnchantment(),
        new UndyingEnchantment(),
        new EnderInfusedEnchantment(),
        new CloakEnchantment(),
        new NightVisionEnchantment(),
        new SpeedEnchantment(),
        new SpringsEnchantment(),
        new BerserkEnchantment(),
        new LeechingEnchantment()
    );

    public static void init() {
        for (CustomEnchantment enchantment : enchantments) {
            boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchantment.bukkitEnchantment);

            if (!registered) {
                registerEnchant(enchantment);
            }
        }
    }

    public static void registerEnchant(CustomEnchantment enchantment) {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment.bukkitEnchantment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CustomEnchantment getEnchantmentFromBukkit(Enchantment enchantment) {
        for (CustomEnchantment customEnchantment : enchantments) {
            if (customEnchantment.bukkitEnchantment.equals(enchantment)) {
                return customEnchantment;
            }
        }

        return null;
    }

    /**
     * Gets all enchantments on a book, represented as custom enchantments.
     * If the provided ItemStack is not an enchanted book, returns null.
     * @param book itemstack representing the enchanted book
     * @return all stored enchantments on the book as custom enchantments
     */
    public static Map<CustomEnchantment, Integer> getEnchantmentsFromBook(ItemStack book) {
        if (!(book.getItemMeta() instanceof EnchantmentStorageMeta)) return null;

        Map<Enchantment, Integer> bookEnchants = ((EnchantmentStorageMeta) book.getItemMeta()).getStoredEnchants();
        Map<CustomEnchantment, Integer> customBookEnchants = new HashMap<>();

        for (Map.Entry<Enchantment, Integer> entry : bookEnchants.entrySet()) {
            customBookEnchants.put(getEnchantmentFromBukkit(entry.getKey()), entry.getValue());
        }

        return customBookEnchants;
    }

    /**
     * Gets all enchantments on an item, represented as custom enchantments.
     * @param item itemstack to get enchantments from
     * @return all enchantments on the item as custom enchantments
     */
    public static Map<CustomEnchantment, Integer> getEnchantmentsFromItem(ItemStack item) {
        Map<Enchantment, Integer> itemEnchants = item.getEnchantments();
        Map<CustomEnchantment, Integer> customItemEnchants = new HashMap<>();

        for (Map.Entry<Enchantment, Integer> entry : itemEnchants.entrySet()) {
            customItemEnchants.put(getEnchantmentFromBukkit(entry.getKey()), entry.getValue());
        }

        return customItemEnchants;
    }

    /**
     * @param player player to check on
     * @param enchantment enchantment to check for
     * @return whether the player has the given enchantment on any of their armor pieces
     */
    public static boolean playerHasArmorEnchantment(Player player, Enchantment enchantment) {
        ItemStack[] armor = player.getEquipment().getArmorContents();

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null || armorPiece.getType().equals(Material.AIR)) continue;
            if (armorPiece.getItemMeta().hasEnchant(enchantment)) return true;
        }

        return false;
    }

    public static @Nullable Integer getPlayerArmorEnchantmentLevel(Player player, Enchantment enchantment) {
        ItemStack[] armor = player.getEquipment().getArmorContents();

        int bestLevel = 0;
        for (ItemStack armorPiece : armor) {
            if (armorPiece == null || armorPiece.getType().equals(Material.AIR)) continue;
            if (armorPiece.getItemMeta().hasEnchant(enchantment)) {
                int newLevel = armorPiece.getItemMeta().getEnchantLevel(enchantment);

                if (newLevel > bestLevel) {
                    bestLevel = newLevel;
                }
            }
        }

        if (bestLevel == 0) {
            return null;
        }
        return bestLevel;
    }

    /**
     * Update enchantment lore on items
     * @param itemStack the item to update
     */
    public static void updateEnchantedItemLore(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta(); // get item metadata

        List<Component> lore = new ArrayList<>();

        // loop through all enchantments on item and add to lore
        for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
            lore.add(entry.getKey().displayName(entry.getValue()).decoration(TextDecoration.ITALIC, false));
        }

        meta.lore(lore); // set item lore
        itemStack.setItemMeta(meta); // set item metadata
    }

    public static void enchantItem(ItemStack itemStack, Enchantment enchantment, Integer level) {
        itemStack.addUnsafeEnchantment(enchantment, level); // set enchantments on item
        updateEnchantedItemLore(itemStack);
    }

    public static void enchantItem(ItemStack itemStack, Map<Enchantment, Integer> enchantments) {
        itemStack.addUnsafeEnchantments(enchantments); // set enchantments on item
        updateEnchantedItemLore(itemStack);
    }

    public static ItemStack getEnchantedBook(CustomEnchantment enchantment, Integer level) {
        // make itemstack with spell material
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);

        // get item metadata
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();

        // set enchantment
        meta.addStoredEnchant(enchantment.bukkitEnchantment, level, true);

        // set name
        if (enchantment.hasLevel()) {
            meta.displayName(
                Component.text("Enchanted Book", enchantment.rarity.color).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(enchantment.name + " " + Utilities.integerToRoman(level), enchantment.rarity.color))
            );
        } else {
            meta.displayName(
                Component.text("Enchanted Book", enchantment.rarity.color).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(enchantment.name, enchantment.rarity.color))
            );
        }

        // set lore
        List<Component> lore = new ArrayList<>();

        // show item enchantment is used on
        lore.add(Utilities.stringToComponentWithoutItalic("&8Apply to: " + enchantment.type.name));

        lore.add(Component.space());

        // set description
        if (enchantment.getDescription(level) != null) {
            for (String line : Utilities.addNewlinesToStringForLore(enchantment.getDescription(level))) {
                lore.add(Utilities.stringToComponentWithoutItalic("&7" + line));
            }
        }

        lore.add(Component.space());

        // add rarity lore
        lore.add(enchantment.rarity.getDecoratedComponent().append(Component.text(" ENCHANTMENT")));

        meta.lore(lore);

        // set new metadata
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}

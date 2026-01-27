package ca.modmonster.spells.item.enchantment;

import ca.modmonster.spells.item.enchantment.enchantments.*;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    /**
     * Gets all enchantments on an item, represented as custom enchantments.
     * @param item itemstack to get enchantments from
     * @return all enchantments on the item as custom enchantments
     */
    public static Map<CustomEnchantment, Integer> getEnchantmentsFromItem(ItemStack item) {
        int[] enchants = Utilities.getPersistentItemTagIntArray(item, "enchantments");
        int[] enchantLevels = Utilities.getPersistentItemTagIntArray(item, "enchantment_levels");
        Map<CustomEnchantment, Integer> enchantMap = new HashMap<>();
        if (enchants == null || enchantLevels == null) return enchantMap;

        for (int i = 0; i < enchants.length; i++) {
            CustomEnchantment enchant = enchantments.get(enchants[i]);
            enchantMap.put(enchant, enchantLevels[i]);
        }

        return enchantMap;
    }

    /**
     * @param item item to check
     * @param enchantment enchantment to check
     * @return whether the enchantment exists on this item
     */
    public static boolean hasEnchant(ItemStack item, CustomEnchantment enchantment) {
        return getEnchantmentsFromItem(item).containsKey(enchantment);
    }

    /**
     * @param item item to check
     * @param enchantmentId ID of the enchantment to check
     * @return whether the enchantment exists on this item
     */
    public static boolean hasEnchant(ItemStack item, String enchantmentId) {
        CustomEnchantment enchantment = customEnchantmentFromId(enchantmentId);
        if (enchantment == null) return false;
        return hasEnchant(item, enchantment);
    }

    /**
     * @param id ID of enchantment to get
     * @return CustomEnchantment represented by provided ID
     */
    public static CustomEnchantment customEnchantmentFromId(String id) {
        // get CustomEnchantment from ID
        CustomEnchantment enchantment = null;
        for (CustomEnchantment e : EnchantmentManager.enchantments) {
            if (id.equals(e.getId())) {
                enchantment = e;
                break;
            }
        }
        return enchantment;
    }

    /**
     * @param item item to check
     * @param enchantment enchantment to check
     * @return the level of the enchantment on this item or 0 if the enchantment does not exist
     */
    public static int getEnchantLevel(ItemStack item, CustomEnchantment enchantment) {
        return getEnchantmentsFromItem(item).getOrDefault(enchantment, 0);
    }

    /**
     * @param player player to check on
     * @param enchantmentId ID of the enchantment to check for
     * @return whether the player has the given enchantment on any of their armor pieces
     */
    public static boolean playerHasArmorEnchantment(Player player, String enchantmentId) {
        ItemStack[] armor = player.getEquipment().getArmorContents();

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null || armorPiece.getType().equals(Material.AIR)) continue;
            if (hasEnchant(armorPiece, enchantmentId)) return true;
        }

        return false;
    }

    public static @Nullable Integer getPlayerArmorEnchantmentLevel(Player player, String enchantmentId) {
        ItemStack[] armor = player.getEquipment().getArmorContents();

        int bestLevel = 0;
        for (ItemStack armorPiece : armor) {
            if (armorPiece == null || armorPiece.getType().equals(Material.AIR)) continue;

            CustomEnchantment enchantment = customEnchantmentFromId(enchantmentId);
            if (hasEnchant(armorPiece, enchantment)) {
                int newLevel = getEnchantLevel(armorPiece, enchantment);

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
        for (Map.Entry<CustomEnchantment, Integer> entry : EnchantmentManager.getEnchantmentsFromItem(itemStack).entrySet()) {
            lore.add(entry.getKey().getDisplayName(entry.getValue()).decoration(TextDecoration.ITALIC, false));
        }

        meta.lore(lore); // set item lore
        itemStack.setItemMeta(meta); // set item metadata
    }

    public static void addEnchant(ItemStack itemStack, CustomEnchantment enchantment, Integer level) {
        Map<CustomEnchantment, Integer> map = new HashMap<>();
        map.put(enchantment, level);

        addEnchant(itemStack, map);
    }

    public static void addEnchant(ItemStack itemStack, Map<CustomEnchantment, Integer> enchantments) {
        // get enchantments currently on item
        Map<CustomEnchantment, Integer> currentEnchants = getEnchantmentsFromItem(itemStack);
        currentEnchants.putAll(enchantments); // add new enchantments

        setEnchants(itemStack, currentEnchants);
    }

    public static void removeEnchant(ItemStack itemStack, String enchantmentId) {
        // get CustomEnchantment from ID
        CustomEnchantment enchantment = customEnchantmentFromId(enchantmentId);
        if (enchantment == null) return;

        // get enchantments currently on item
        Map<CustomEnchantment, Integer> currentEnchants = getEnchantmentsFromItem(itemStack);
        currentEnchants.remove(enchantment);

        setEnchants(itemStack, currentEnchants);
    }

    public static void setEnchants(ItemStack itemStack, Map<CustomEnchantment, Integer> enchantments) {
        // build arrays of enchantment IDs and levels
        int[] enchantIds = new int[enchantments.size()];
        int[] enchantLevels = new int[enchantments.size()];

        int i = 0;
        for (Map.Entry<CustomEnchantment, Integer> entry : enchantments.entrySet()) {
            // get the position of desired enchantment in array; this becomes its ID
            int id = -1;
            for (int j = 0; j < EnchantmentManager.enchantments.size(); j++) {
                if (entry.getKey() == EnchantmentManager.enchantments.get(j)) {
                    id = j;
                    break;
                }
            }
            if (id == -1) continue; // this should not be possible

            enchantIds[i] = id;
            enchantLevels[i] = entry.getValue();

            i++;
        }

        // set enchantments on item
        Utilities.setPersistentItemTag(itemStack, "enchantments", enchantIds);
        Utilities.setPersistentItemTag(itemStack, "enchantment_levels", enchantLevels);

        // make item glow!
        if (!itemStack.getType().equals(Material.ENCHANTED_BOOK))
            Utilities.setGlowing(itemStack, enchantIds.length > 0);

        updateEnchantedItemLore(itemStack);
    }

    public static ItemStack getEnchantedBook(CustomEnchantment enchantment, Integer level) {
        // make itemstack with spell material
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);

        // set enchantment
        addEnchant(itemStack, enchantment, level);

        // get item metadata
        ItemMeta meta = itemStack.getItemMeta();

        // set name
        if (enchantment.hasLevel()) {
            meta.displayName(
                Component.text("Enchanted Book", enchantment.getRarity().color).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(enchantment.getName() + " " + Utilities.integerToRoman(level), enchantment.getRarity().color))
            );
        } else {
            meta.displayName(
                Component.text("Enchanted Book", enchantment.getRarity().color).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(enchantment.getName(), enchantment.getRarity().color))
            );
        }

        // set lore
        List<Component> lore = new ArrayList<>();

        // show item enchantment is used on
        lore.add(Utilities.stringToComponentWithoutItalic("&8Apply to: " + enchantment.getType().name));

        lore.add(Component.space());

        // set description
        if (enchantment.getDescription(level) != null) {
            for (String line : Utilities.addNewlinesToStringForLore(enchantment.getDescription(level))) {
                lore.add(Utilities.stringToComponentWithoutItalic("&7" + line));
            }
        }

        lore.add(Component.space());

        // add rarity lore
        lore.add(enchantment.getRarity().getDecoratedComponent().append(Component.text(" ENCHANTMENT")));

        meta.lore(lore);

        // set new metadata
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}

package ca.modmonster.spells.item.enchantment;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnchantmentType {
    SWORD("&3Sword \uD83D\uDDE1", EnchantmentTarget.WEAPON),
    ARMOR("&3Armor \uD83D\uDEE1", EnchantmentTarget.ARMOR),
    HELMET("&3Helmet \uD83D\uDEE1", EnchantmentTarget.ARMOR_HEAD),
    CHESTPLATE("&3Chestplate \uD83D\uDEE1", EnchantmentTarget.ARMOR_TORSO),
    LEGGINGS("&3Leggings \uD83D\uDEE1", EnchantmentTarget.ARMOR_LEGS),
    BOOTS("&3Boots \uD83D\uDEE1", EnchantmentTarget.ARMOR_FEET);

    public final String name;
    public final EnchantmentTarget target;

    EnchantmentType(String name, EnchantmentTarget target) {
        this.name = name;
        this.target = target;
    }

    /**
     * @return list of materials that can be enchanted with this type of enchantment
     */
    public List<Material> getEnchantableMaterials() {
        switch (this) {
            case SWORD:
                return Arrays.asList(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD);
            case ARMOR:
                return Arrays.asList(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET, Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS);
            case HELMET:
                return Arrays.asList(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET);
            case CHESTPLATE:
                return Arrays.asList(Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE);
            case LEGGINGS:
                return Arrays.asList(Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS);
            case BOOTS:
                return Arrays.asList(Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS);
        }

        return new ArrayList<>();
    }
}
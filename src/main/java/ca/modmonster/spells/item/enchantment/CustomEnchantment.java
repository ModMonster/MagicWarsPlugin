package ca.modmonster.spells.item.enchantment;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.util.Utilities;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public abstract class CustomEnchantment {
    public final String id;
    public final String name;
    public final Rarity rarity;
    public final EnchantmentType type;
    public final Integer maxLevel;
    public final List<NamespacedKey> conflictingEnchantments;
    public final Enchantment bukkitEnchantment;

    public CustomEnchantment(String id, String name, Rarity rarity, EnchantmentType type, Integer maxLevel, List<NamespacedKey> conflictingEnchantments) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.type = type;
        this.maxLevel = maxLevel;
        this.conflictingEnchantments = conflictingEnchantments;

        this.bukkitEnchantment = new Enchantment(NamespacedKey.minecraft(id)) {
            @Override
            public @NotNull String getName() {
                return id;
            }

            @Override
            public int getMaxLevel() {
                return maxLevel;
            }

            @Override
            public int getStartLevel() {
                return 1;
            }

            @Override
            public @NotNull EnchantmentTarget getItemTarget() {
                return type.target;
            }

            @Override
            public boolean isTreasure() {
                return false;
            }

            @Override
            public boolean isCursed() {
                return false;
            }

            @Override
            public boolean conflictsWith(@NotNull Enchantment other) {
                return false;
            }

            @Override
            public boolean canEnchantItem(@NotNull ItemStack item) {
                return true;
            }

            @Override
            public @NotNull Component displayName(int level) {
                if (hasLevel()) {
                    return Utilities.stringToComponent("&7" + name + " " + Utilities.integerToRoman(level));
                } else {
                    return Utilities.stringToComponent("&7" + name);
                }
            }

            @Override
            public boolean isTradeable() {
                return false;
            }

            @Override
            public boolean isDiscoverable() {
                return false;
            }

            @Override
            public @NotNull EnchantmentRarity getRarity() {
                return EnchantmentRarity.COMMON;
            }

            @Override
            public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
                return 0;
            }

            @Override
            public @NotNull Set<EquipmentSlot> getActiveSlots() {
                return null;
            }

            @Override
            public @NotNull String translationKey() {
                return null;
            }
        };
    }

    public abstract String getDescription(Integer level);

    /**
     * @return Whether the CustomEnchantment has more than 1 level
     */
    public boolean hasLevel() {
        return maxLevel != 1;
    }

    /**
     * @param enchantment CustomEnchantment to check
     * @return whether the provided CustomEnchantment conflicts with this CustomEnchantment
     */
    public boolean conflictsWith(CustomEnchantment enchantment) {
        return conflictingEnchantments.contains(enchantment.bukkitEnchantment.getKey()) || enchantment.conflictingEnchantments.contains(this.bukkitEnchantment.getKey());
    }
}

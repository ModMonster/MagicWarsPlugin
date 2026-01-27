package ca.modmonster.spells.item.enchantment;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;

public abstract class CustomEnchantment {
    public abstract String getId();
    public abstract String getName();
    public abstract Rarity getRarity();
    public abstract EnchantmentType getType();
    public abstract int getMaxLevel();
    public abstract String getDescription(Integer level);

    public String[] getConflictingEnchantments() {
        return new String[0];
    }

    /**
     * @param level Level of the enchantment
     * @return Display name to be added to lore
     */
    public Component getDisplayName(int level) {
        if (hasLevel()) {
            return Utilities.stringToComponent("&7" + getName() + " " + Utilities.integerToRoman(level));
        } else {
            return Utilities.stringToComponent("&7" + getName());
        }
    }

    /**
     * @return Whether the CustomEnchantment has more than 1 level
     */
    public boolean hasLevel() {
        return getMaxLevel() != 1;
    }

    /**
     * @param enchantment CustomEnchantment to check
     * @return whether the provided CustomEnchantment conflicts with this CustomEnchantment
     */
    public boolean conflictsWith(CustomEnchantment enchantment) {
        // Check if this enchantment conflicts with other enchantment
        for (String conflict : this.getConflictingEnchantments()) {
            if (conflict.equals(enchantment.getId())) return true;
        }

        // Check if other enchantment conflicts with this enchantment
        for (String conflict : enchantment.getConflictingEnchantments()) {
            if (conflict.equals(this.getId())) return true;
        }

        return false;
    }
}

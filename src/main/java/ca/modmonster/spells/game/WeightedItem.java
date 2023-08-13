package ca.modmonster.spells.game;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.spell.Spell;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WeightedItem {
    public Material material;
    public Spell spell;
    public CustomEnchantment enchantment;
    public Integer weight;
    public Integer maxStackSize;
    public Rarity rarity;

    public WeightedItem(@Nullable Material material, @Nullable Spell spell, @Nullable CustomEnchantment enchantment, @NotNull Integer weight, @NotNull Integer maxStackSize) {
        if (material == null && spell == null && enchantment == null) throw new IllegalArgumentException("One of the parameters ('item', 'spell', or 'enchantment') must not be null.");

        this.material = material;
        this.spell = spell;
        this.enchantment = enchantment;
        this.weight = weight;
        this.maxStackSize = maxStackSize;
    }

    /**
     * @return Whether the WeightedItem represents a Spell.
     */
    public boolean isSpell() {
        return spell != null;
    }

    /**
     * @return Whether the WeightedItem represents an Enchantment.
     */
    public boolean isEnchantment() {
        return enchantment != null;
    }

    /**
     * @return Whether the WeightedItem can be stacked.
     */
    public boolean isStackable() {
        return maxStackSize != 1;
    }
}

package ca.modmonster.spells.item.enchantment;

import ca.modmonster.spells.item.Rarity;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public abstract class SwordEnchantment extends CustomEnchantment {
    public SwordEnchantment(String id, String name, Rarity rarity, EnchantmentType type, Integer maxLevel, List<NamespacedKey> conflictingEnchantments) {
        super(id, name, rarity, type, maxLevel, conflictingEnchantments);
    }

    public abstract void onHitEntity(EntityDamageByEntityEvent event, Integer level);
}

package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.Random;

public class MoltenEnchantment extends ArmorEnchantment {
    public MoltenEnchantment() {
        super(
            "molten",
            "Molten",
            Rarity.RARE,
            EnchantmentType.ARMOR,
            3,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When hit: &7" + (20 + level * 10) + "% chance to set attacker on fire for " + (level + 1) + " seconds.";
    }

    @Override
    public void onTakeDamageFromEntity(EntityDamageByEntityEvent event, Integer level) {
        if (new Random().nextFloat() < (0.2 + (level / 10.0))) {
            event.getDamager().setFireTicks(20 + (20 * level));
        }
    }
}

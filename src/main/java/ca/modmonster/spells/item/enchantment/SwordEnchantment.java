package ca.modmonster.spells.item.enchantment;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class SwordEnchantment extends CustomEnchantment {
    public abstract void onHitEntity(EntityDamageByEntityEvent event, Integer level);
}

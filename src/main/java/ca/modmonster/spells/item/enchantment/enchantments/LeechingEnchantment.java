package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LeechingEnchantment extends SwordEnchantment {
    @Override
    public String getId() {
        return "leeching";
    }

    @Override
    public String getName() {
        return "Leeching";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public EnchantmentType getType() {
        return EnchantmentType.SWORD;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When attacking enemy: &7Restores your health when dealing damage to enemies.";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (!(event.getDamager() instanceof LivingEntity damager)) return;

        double newHealth = damager.getHealth() + (event.getDamage() / 3);

        // heal
        damager.setHealth(Math.min(newHealth, entity.getAttribute(Attribute.MAX_HEALTH).getValue()));
    }
}

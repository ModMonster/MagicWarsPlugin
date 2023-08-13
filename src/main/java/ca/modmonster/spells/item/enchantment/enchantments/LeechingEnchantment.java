package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class LeechingEnchantment extends SwordEnchantment {
    public LeechingEnchantment() {
        super(
            "leeching",
            "Leeching",
            Rarity.MYTHIC,
            EnchantmentType.SWORD,
            1,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When attacking enemy: &7Restores your health when dealing damage to enemies.";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        LivingEntity damager = (LivingEntity) event.getDamager();

        double newHealth = damager.getHealth() + (event.getDamage() / 3);

        // heal
        damager.setHealth(Math.min(newHealth, entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }
}

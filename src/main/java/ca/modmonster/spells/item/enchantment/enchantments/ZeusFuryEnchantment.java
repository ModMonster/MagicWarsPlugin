package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class ZeusFuryEnchantment extends SwordEnchantment {
    @Override
    public String getId() {
        return "zeus_fury";
    }

    @Override
    public String getName() {
        return "Zeus's Fury";
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
        return "&8When attacking enemy: &7Has a chance to strike enemy with lightning.";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        if (new Random().nextFloat() < 0.3) {
            entity.getWorld().strikeLightningEffect(entity.getLocation());
            entity.damage(5);
            entity.setFireTicks(120);
        }
    }
}

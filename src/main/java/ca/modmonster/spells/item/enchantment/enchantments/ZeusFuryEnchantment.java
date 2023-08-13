package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.Random;

public class ZeusFuryEnchantment extends SwordEnchantment {
    public ZeusFuryEnchantment() {
        super(
            "zeus_fury",
            "Zeus's Fury",
            Rarity.MYTHIC,
            EnchantmentType.SWORD,
            1,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When attacking enemy: &7Has a chance to strike enemy with lightning.";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();

        if (new Random().nextFloat() < 0.3) {
            entity.getWorld().strikeLightningEffect(entity.getLocation());
            entity.damage(5);
            entity.setFireTicks(120);
        }
    }
}

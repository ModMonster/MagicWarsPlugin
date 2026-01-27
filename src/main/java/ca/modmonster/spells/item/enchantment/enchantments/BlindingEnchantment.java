package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class BlindingEnchantment extends SwordEnchantment {
    @Override
    public String getId() {
        return "blinding";
    }

    @Override
    public String getName() {
        return "Blinding";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
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
        return "&8When attacking enemy: &7Blinds enemy for 2 seconds.";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        entity.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(40, 0));
    }
}

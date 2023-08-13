package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class BlindingEnchantment extends SwordEnchantment {
    public BlindingEnchantment() {
        super(
            "blinding",
            "Blinding",
            Rarity.UNCOMMON,
            EnchantmentType.SWORD,
            1,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When attacking enemy: &7Blinds enemy for 2 seconds.";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();

        entity.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(40, 0));
    }
}

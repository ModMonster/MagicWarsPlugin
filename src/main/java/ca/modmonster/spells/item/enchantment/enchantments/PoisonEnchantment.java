package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class PoisonEnchantment extends SwordEnchantment {
    public PoisonEnchantment() {
        super(
            "poison",
            "Poison",
            Rarity.UNCOMMON,
            EnchantmentType.SWORD,
            2,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        String levelAsRoman = Utilities.integerToRoman(level);

        return "&8When attacking enemy: &7Gives your enemy Poison " + levelAsRoman + " for 4 seconds.";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();

        entity.addPotionEffect(PotionEffectType.POISON.createEffect(80, level - 1));
    }
}

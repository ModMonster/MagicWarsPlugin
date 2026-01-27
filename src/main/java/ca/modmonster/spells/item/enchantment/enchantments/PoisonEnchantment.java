package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

// I'm filling up my glass, but it's always hollow
public class PoisonEnchantment extends SwordEnchantment {
    @Override
    public String getId() {
        return "poison";
    }

    @Override
    public String getName() {
        return "Poison";
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
        return 2;
    }

    @Override
    public String getDescription(Integer level) {
        String levelAsRoman = Utilities.integerToRoman(level);

        return "&8When attacking enemy: &7Gives your enemy Poison " + levelAsRoman + " for 4 seconds.";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        entity.addPotionEffect(PotionEffectType.POISON.createEffect(80, level - 1));
    }
}

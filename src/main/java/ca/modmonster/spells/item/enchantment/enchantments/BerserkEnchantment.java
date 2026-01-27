package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BerserkEnchantment extends ArmorEnchantment {
    @Override
    public String getId() {
        return "berserk";
    }

    @Override
    public String getName() {
        return "Berserk";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public EnchantmentType getType() {
        return EnchantmentType.ARMOR;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When low on health: &7Gives you extreme strength.";
    }

    @Override
    public void onTick(Player player, Integer level) {
        int maximumHealthToTrigger = level >= 2? 8 : 6;

        if (!player.hasPotionEffect(PotionEffectType.STRENGTH) && player.getHealth() <= maximumHealthToTrigger) {
            player.addPotionEffect(PotionEffectType.STRENGTH.createEffect(Integer.MAX_VALUE, 1).withParticles(false).withAmbient(true));
        } else if (player.hasPotionEffect(PotionEffectType.STRENGTH) && player.getHealth() > maximumHealthToTrigger) {
            player.removePotionEffect(PotionEffectType.STRENGTH);
        }
    }

    @Override
    public void onTickNotWearingArmor(Player player) {
        if (player.hasPotionEffect(PotionEffectType.STRENGTH)) {
            player.removePotionEffect(PotionEffectType.STRENGTH);
        }
    }
}

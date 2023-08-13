package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class BerserkEnchantment extends ArmorEnchantment {
    public BerserkEnchantment() {
        super(
            "berserk",
            "Berserk",
            Rarity.MYTHIC,
            EnchantmentType.ARMOR,
            2,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When low on health: &7Gives you extreme strength.";
    }

    @Override
    public void onTick(Player player, Integer level) {
        int maximumHealthToTrigger = level >= 2? 8 : 6;

        if (!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) && player.getHealth() <= maximumHealthToTrigger) {
            player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE, 1).withParticles(false).withAmbient(true));
        } else if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) && player.getHealth() > maximumHealthToTrigger) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }

    @Override
    public void onTickNotWearingArmor(Player player) {
        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }
}

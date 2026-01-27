package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

// kachow
public class SpeedEnchantment extends ArmorEnchantment {
    final List<Player> affectedPlayers = new ArrayList<>();

    @Override
    public String getId() {
        return "speed";
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public EnchantmentType getType() {
        return EnchantmentType.BOOTS;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDescription(Integer level) {
        return "&8While wearing: &7Gives you a speed boost.";
    }

    @Override
    public void onTick(Player player, Integer level) {
        if (!affectedPlayers.contains(player) || (affectedPlayers.contains(player) && !player.hasPotionEffect(PotionEffectType.SPEED))) {
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, level - 1).withParticles(false).withAmbient(true));
            affectedPlayers.add(player);
        }
    }

    @Override
    public void onTickNotWearingArmor(Player player) {
        if (affectedPlayers.contains(player)) {
            player.removePotionEffect(PotionEffectType.SPEED);
            affectedPlayers.remove(player);
        }
    }
}

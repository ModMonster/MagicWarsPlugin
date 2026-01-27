package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class NightVisionEnchantment extends ArmorEnchantment {
    final List<Player> affectedPlayers = new ArrayList<>();

    @Override
    public String getId() {
        return "night_vision";
    }

    @Override
    public String getName() {
        return "Night Vision";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public EnchantmentType getType() {
        return EnchantmentType.HELMET;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDescription(Integer level) {
        return "&8While wearing: &7Allows you to see in the dark.";
    }

    @Override
    public void onTick(Player player, Integer level) {
        if (!affectedPlayers.contains(player) || (affectedPlayers.contains(player) && !player.hasPotionEffect(PotionEffectType.NIGHT_VISION))) {
            player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(Integer.MAX_VALUE, 0).withParticles(false).withAmbient(true));
            affectedPlayers.add(player);
        }
    }

    @Override
    public void onTickNotWearingArmor(Player player) {
        if (affectedPlayers.contains(player)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            affectedPlayers.remove(player);
        }
    }
}

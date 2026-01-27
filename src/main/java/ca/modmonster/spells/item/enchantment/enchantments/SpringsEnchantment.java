package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SpringsEnchantment extends ArmorEnchantment {
    final List<Player> affectedPlayers = new ArrayList<>();

    @Override
    public String getId() {
        return "springs";
    }

    @Override
    public String getName() {
        return "Springs";
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
        return 2;
    }

    @Override
    public String getDescription(Integer level) {
        return "&8While wearing: &7Increases your jump height.";
    }

    @Override
    public void onTick(Player player, Integer level) {
        if (!affectedPlayers.contains(player) || (affectedPlayers.contains(player) && !player.hasPotionEffect(PotionEffectType.JUMP_BOOST))) {
            player.addPotionEffect(PotionEffectType.JUMP_BOOST.createEffect(Integer.MAX_VALUE, (level * 2) - 1).withParticles(false).withAmbient(true));
            affectedPlayers.add(player);
        }
    }

    @Override
    public void onTickNotWearingArmor(Player player) {
        if (affectedPlayers.contains(player)) {
            player.removePotionEffect(PotionEffectType.JUMP_BOOST);
            affectedPlayers.remove(player);
        }
    }
}

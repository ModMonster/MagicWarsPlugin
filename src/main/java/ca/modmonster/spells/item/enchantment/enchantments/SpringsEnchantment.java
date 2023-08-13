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

    public SpringsEnchantment() {
        super(
            "springs",
            "Springs",
            Rarity.RARE,
            EnchantmentType.BOOTS,
            2,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        return "&8While wearing: &7Increases your jump height.";
    }

    @Override
    public void onTick(Player player, Integer level) {
        if (!affectedPlayers.contains(player) || (affectedPlayers.contains(player) && !player.hasPotionEffect(PotionEffectType.JUMP))) {
            player.addPotionEffect(PotionEffectType.JUMP.createEffect(Integer.MAX_VALUE, (level * 2) - 1).withParticles(false).withAmbient(true));
            affectedPlayers.add(player);
        }
    }

    @Override
    public void onTickNotWearingArmor(Player player) {
        if (affectedPlayers.contains(player)) {
            player.removePotionEffect(PotionEffectType.JUMP);
            affectedPlayers.remove(player);
        }
    }
}

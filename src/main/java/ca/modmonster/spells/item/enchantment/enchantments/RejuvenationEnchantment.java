package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RejuvenationEnchantment extends ArmorEnchantment {
    public static final Map<Player, Integer> regenerationDelayTicks = new HashMap<>();
    final List<Player> affectedPlayers = new ArrayList<>();

    @Override
    public String getId() {
        return "rejuvenation";
    }

    @Override
    public String getName() {
        return "Rejuvenation";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public EnchantmentType getType() {
        return EnchantmentType.ARMOR;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getDescription(Integer level) {
        return "Regenerates your health quicker when you stand still for more than 3 seconds.";
    }

    @Override
    public void onTick(Player player, Integer level) {
        if (regenerationDelayTicks.getOrDefault(player, 60) < 1) {
            if (!affectedPlayers.contains(player) || (affectedPlayers.contains(player) && !player.hasPotionEffect(PotionEffectType.REGENERATION))) {
                player.addPotionEffect(PotionEffectType.REGENERATION.createEffect(Integer.MAX_VALUE, level - 1));
                affectedPlayers.add(player);
            }
        } else {
            regenerationDelayTicks.put(player, regenerationDelayTicks.getOrDefault(player, 60) - 1);
        }
    }

    @Override
    public void onTickNotWearingArmor(Player player) {
        if (affectedPlayers.contains(player)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
            regenerationDelayTicks.remove(player);
            affectedPlayers.remove(player);
        }
    }

    public static void onMove(PlayerMoveEvent event) {
        regenerationDelayTicks.put(event.getPlayer(), 60);
    }
}

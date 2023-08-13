package ca.modmonster.spells.item.enchantment;

import ca.modmonster.spells.item.Rarity;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public abstract class ArmorEnchantment extends CustomEnchantment {
    public ArmorEnchantment(String id, String name, Rarity rarity, EnchantmentType type, Integer maxLevel, List<NamespacedKey> conflictingEnchantments) {
        super(id, name, rarity, type, maxLevel, conflictingEnchantments);
    }

    public void onTakeDamage(EntityDamageEvent event, Integer level) {}
    public void onTakeDamageFromEntity(EntityDamageByEntityEvent event, Integer level) {}
    public void onDeath(EntityDeathEvent event, Integer level) {}
    public void onTick(Player player, Integer level) {}
    public void onTickNotWearingArmor(Player player) {}
}

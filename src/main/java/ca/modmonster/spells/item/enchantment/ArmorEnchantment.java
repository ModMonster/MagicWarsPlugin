package ca.modmonster.spells.item.enchantment;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public abstract class ArmorEnchantment extends CustomEnchantment {
    public void onTakeDamage(EntityDamageEvent event, Integer level) {}
    public void onTakeDamageFromEntity(EntityDamageByEntityEvent event, Integer level) {}
    public void onDeath(EntityDeathEvent event, Integer level) {}
    public void onTick(Player player, Integer level) {}
    public void onTickNotWearingArmor(Player player) {}
}

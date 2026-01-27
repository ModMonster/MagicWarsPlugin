package ca.modmonster.spells.events;

import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import ca.modmonster.spells.item.spell.spells.Backstabber;
import ca.modmonster.spells.item.spell.spells.minion.Minion;
import ca.modmonster.spells.item.spell.spells.minion.ZombieMinion;
import ca.modmonster.spells.util.InvisibilityManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public class OnEntityDamageByEntity implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        makeMinionsTargetEntity(event);
        handleCustomSwordEnchantments(event);
        handleBackstabberInvisibility(event);
    }

    void handleBackstabberInvisibility(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        if (Backstabber.attackInvisibilityPlayers.contains(player)) {
            Backstabber.attackInvisibilityPlayers.remove(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            InvisibilityManager.removeInvisibility(player);
        }
    }

    void handleCustomSwordEnchantments(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof LivingEntity livingEntity)) return;

        EntityEquipment entityEquipment = livingEntity.getEquipment();
        if (entityEquipment == null) return;
        ItemStack item = entityEquipment.getItemInMainHand();

        if (item.getType().equals(Material.AIR)) return;

        for (CustomEnchantment enchantment : EnchantmentManager.enchantments) {
            if (!(enchantment instanceof SwordEnchantment)) continue;

            int enchantLevel = EnchantmentManager.getEnchantLevel(item, enchantment);
            if (enchantLevel > 0) {
                ((SwordEnchantment) enchantment).onHitEntity(event, enchantLevel);
            }
        }
    }

    void makeMinionsTargetEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {

            for (Map.Entry<Entity, UUID> entry : ZombieMinion.playerMinions.entrySet()) {
                // get entry
                Entity minion = entry.getKey();

                if (event.getDamager().getUniqueId().equals(entry.getValue()) && !(event.getEntity().equals(entry.getKey()))) {
                    if (Minion.isMinion(entity) && Minion.getMinionOwner(entity).equals(event.getDamager())) continue;
                    ((Monster) minion).setTarget(entity);
                }
            }
        }
    }
}

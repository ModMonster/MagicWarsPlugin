package ca.modmonster.spells.events;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class OnEntityDamage implements Listener {
    public static final List<Entity> fallProofEntities = new ArrayList<>();
    public static final List<Entity> invulnerableEntities = new ArrayList<>();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        handleFallProofEntities(event);
        handleCustomArmorEnchantments(event);
        handleInvulnerableEntities(event);
    }

    void handleInvulnerableEntities(EntityDamageEvent event) {
        if (invulnerableEntities.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    void handleCustomArmorEnchantments(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;

        LivingEntity livingEntity = (LivingEntity) entity;
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        if (entityEquipment == null) return;
        ItemStack[] armor = entityEquipment.getArmorContents();

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null || armorPiece.getType().equals(Material.AIR)) continue;

            for (CustomEnchantment enchantment : EnchantmentManager.enchantments) {
                if (!(enchantment instanceof ArmorEnchantment)) continue;

                if (armorPiece.getItemMeta().hasEnchant(enchantment.bukkitEnchantment)) {
                    Integer enchantLevel = armorPiece.getEnchantmentLevel(enchantment.bukkitEnchantment);

                    ((ArmorEnchantment) enchantment).onTakeDamage(event, enchantLevel);
                    if (event instanceof EntityDamageByEntityEvent) ((ArmorEnchantment) enchantment).onTakeDamageFromEntity((EntityDamageByEntityEvent) event, enchantLevel);
                }
            }
        }
    }

    void handleFallProofEntities(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && fallProofEntities.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    public static void addFallProofEntity(LivingEntity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!fallProofEntities.contains(entity)) {
                    fallProofEntities.add(entity);
                }

                if (entity.getVelocity().getY() <= 0 && !entity.hasPotionEffect(PotionEffectType.LEVITATION)) {
                    cancel();
                }
            }
        }.runTaskTimer(Spells.main, 2, 1);
    }
}

package ca.modmonster.spells.events;

import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.spell.spells.minion.ZombieMinion;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class OnTick extends BukkitRunnable {
    @Override
    public void run() {
        handleZombieMinionLifespan();
        handleEnchantmentOnTick();
    }

    void handleEnchantmentOnTick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            EntityEquipment equipment = player.getEquipment();

            Map<CustomEnchantment, Integer> armorEnchantments = new HashMap<>();

            for (ItemStack item : equipment.getArmorContents()) {
                if (item == null || item.getType().isAir()) continue;

                // add all enchantments from this piece
                Map<CustomEnchantment, Integer> enchantments = EnchantmentManager.getEnchantmentsFromItem(item);
                for (Map.Entry<CustomEnchantment, Integer> entry : enchantments.entrySet()) {
                    // enchantment doesn't already exist; add it
                    if (!armorEnchantments.containsKey(entry.getKey())) {
                        armorEnchantments.put(entry.getKey(), entry.getValue());
                        continue;
                    }

                    // check if current enchantment has a higher level
                    if (armorEnchantments.get(entry.getKey()) > entry.getValue()) continue;
                    armorEnchantments.put(entry.getKey(), entry.getValue());
                }
            }

            for (CustomEnchantment enchantment : EnchantmentManager.enchantments) {
                if (!(enchantment instanceof ArmorEnchantment armorEnchantment)) continue;

                if (armorEnchantments.containsKey(enchantment)) {
                    armorEnchantment.onTick(player, armorEnchantments.get(enchantment));
                } else {
                    armorEnchantment.onTickNotWearingArmor(player);
                }
            }
        }
    }

    void handleZombieMinionLifespan() {
        Iterator<Map.Entry<Entity, UUID>> it = ZombieMinion.playerMinions.entrySet().iterator();

        while (it.hasNext())
        {
            // get entry
            Map.Entry<Entity, UUID> entry = it.next();

            Entity entity = entry.getKey();
            Integer lifespan = Utilities.getPersistentEntityTagInteger(entity, "minion_lifespan");

            if (lifespan <= 0) {
                // spawn particles
                entity.getWorld().spawnParticle(Particle.SMOKE, entity.getLocation().add(0, 2, 0), 10, 0.5, 0.5, 0.5, 0);

                // remove minion from world
                entity.remove();

                // remove minion from map
                it.remove();

                return;
            }

            Utilities.setPersistentEntityTag(entity, "minion_lifespan", lifespan - 1);
        }
    }
}

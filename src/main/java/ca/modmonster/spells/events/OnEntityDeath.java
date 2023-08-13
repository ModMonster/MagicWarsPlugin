package ca.modmonster.spells.events;

import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.spell.spells.minion.Minion;
import ca.modmonster.spells.item.spell.spells.minion.ZombieMinion;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class OnEntityDeath implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // stop mobs from dropping items
        event.getDrops().clear();
        event.setDroppedExp(0);

        handleMinionDeath(event);
        handleCustomArmorEnchantments(event);

        if (!event.isCancelled()) {
            handleDeathInGame(event);
        }
    }

    void handleCustomArmorEnchantments(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        EntityEquipment entityEquipment = entity.getEquipment();
        if (entityEquipment == null) return;
        ItemStack[] armor = entityEquipment.getArmorContents();

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null || armorPiece.getType().equals(Material.AIR)) continue;

            for (CustomEnchantment enchantment : EnchantmentManager.enchantments) {
                if (!(enchantment instanceof ArmorEnchantment)) continue;

                if (armorPiece.getItemMeta().hasEnchant(enchantment.bukkitEnchantment)) {
                    Integer enchantLevel = armorPiece.getEnchantmentLevel(enchantment.bukkitEnchantment);

                    ((ArmorEnchantment) enchantment).onDeath(event, enchantLevel);
                }
            }
        }
    }

    void handleDeathInGame(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player killer = victim.getKiller();
        Entity minion = null;

        // check if player was killed by minions
        if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent lastDamageEvent = (EntityDamageByEntityEvent) victim.getLastDamageCause();
            Player minionOwner = Minion.getMinionOwner(lastDamageEvent.getDamager());

            if (minionOwner != null) {
               killer = minionOwner;
               minion = lastDamageEvent.getDamager();
            }
        }

        event.setCancelled(true);

        GameManager.activeGame.kill(victim, killer, minion);
    }

    void handleMinionDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        // check if entity is minion
        if (Utilities.getPersistentEntityTagString(entity, "minion_type") != null) {
            // remove minion from map
            ZombieMinion.playerMinions.entrySet().removeIf(entry -> entry.getKey().equals(entity));
        }
    }
}

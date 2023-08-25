package ca.modmonster.spells.events;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.spell.Power;
import ca.modmonster.spells.item.spell.SpellManager;
import ca.modmonster.spells.item.spell.spells.ReturnPearl;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class OnProjectileHit implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        handleCustomEnderPearls(event);
        handleCustomSnowballs(event);
    }

    void handleCustomSnowballs(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!projectile.getType().equals(EntityType.SNOWBALL)) return;

        // add knockback
        Entity hit = event.getHitEntity();
        if (!(hit instanceof LivingEntity)) return;
        LivingEntity hitLiving = (LivingEntity) event.getHitEntity();

        hitLiving.playHurtAnimation(100);
        hitLiving.getWorld().playSound(hitLiving.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);

        // add knockback
        Vector launchDirection = hitLiving.getLocation().toVector().add(projectile.getLocation().toVector().multiply(-1));
        launchDirection.setY(0.5);
        hitLiving.setVelocity(launchDirection.multiply(0.5));

        // handle slowballs
        String ballType = Utilities.getPersistentEntityTagString(projectile, "projectile_type");
        if (ballType == null) return;
        if (ballType.equals("slowball")) {
            hitLiving.addPotionEffect(PotionEffectType.SLOW.createEffect(40, 1));
        }
    }

    void handleCustomEnderPearls(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!projectile.getType().equals(EntityType.ENDER_PEARL)) return;
        String pearlType = Utilities.getPersistentEntityTagString(projectile, "pearl_type");

        // run code if landing spot is outside world border
        if (!Utilities.isLocationWithinWorldBorder(event.getEntity().getLocation())) {
            ProjectileSource projectileSource = event.getEntity().getShooter();
            Player pearlOwner = projectileSource instanceof Player? (Player) projectileSource : null;

            if (pearlType != null && pearlType.equals("return_pearl")) {
                pearlOwner.setCooldown(Material.ENDER_EYE, 0);

                // give back pearl
                pearlOwner.getInventory().addItem(SpellManager.getSpell(new ReturnPearl(), Power.WEAK));
            } else {
                // give back pearl
                pearlOwner.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
            }

            pearlOwner.sendMessage(Utilities.stringToComponent("&cYou can't teleport outside of the &bworld border&c!"));
            PlaySound.error(pearlOwner);
            projectile.remove();
            event.setCancelled(true);
            return;
        }

        if (pearlType == null) return;

        if (pearlType.equals("return_pearl")) {
            ProjectileSource projectileSource = event.getEntity().getShooter();
            Player pearlOwner = projectileSource instanceof Player? (Player) projectileSource : null;

            if (pearlOwner != null) {
                pearlOwner.setCooldown(Material.ENDER_EYE, 100);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    ReturnPearl.returnPlayer(projectile);
                }
            }.runTaskLater(Spells.main, 100);
        }
    }
}

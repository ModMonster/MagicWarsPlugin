package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.RaycastTarget;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SpearOfFire extends Spell {
    @Override
    public String getId() {
        return "spear_of_fire";
    }

    @Override
    public Material getMaterial() {
        return Material.SPECTRAL_ARROW;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public boolean getStackable() {
        return false;
    }

    @Override
    public boolean getStackableInChests() {
        return false;
    }

    @Override
    public boolean getGlow() {
        return true;
    }

    @Override
    public boolean getConsumeOnUse() {
        return false;
    }

    @Override
    public boolean getHasPower() {
        return true;
    }

    @Override
    public List<Ability> getAbilities() {
        return Collections.singletonList(
            new SpearOfFireClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return Collections.singletonList(Material.ARROW);
    }

    @Override
    public String getName(Power power) {
        return "Spear of Fire";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class SpearOfFireClickAbility extends Ability {
    public SpearOfFireClickAbility() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        int maxParticles;

        switch (power) {
            case WEAK:
                maxParticles = 12;
                break;
            case STRONG:
                maxParticles = 16;
                break;
            case POWERFUL:
                maxParticles = 24;
                break;
            default:
                maxParticles = 0;
        }

        // play shoot sound
        PlaySound.shoot(player);

        // do raycast
        Vector direction = player.getLocation().getDirection().normalize();
        final int[] currentDistance = {0};
        final Location raycastLocation = player.getEyeLocation().subtract(0, 0.5, 0);

        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = raycastLocation.clone().add(direction.clone().multiply(currentDistance[0]));
                currentDistance[0] += 1;

                // spawn particles
                player.getWorld().spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, 0.025);
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.1, 0.1, 0.1, 0, new Particle.DustOptions(Color.ORANGE, 0.75f));

                RaycastTarget raycastTargetAtLocation = Utilities.raycastTargetFromLocation(player, location, 0.5f);

                if (raycastTargetAtLocation != null || currentDistance[0] >= maxParticles) {
                    // damage entity
                    if (raycastTargetAtLocation != null && raycastTargetAtLocation.livingEntity != null) {
                        raycastTargetAtLocation.livingEntity.damage(getDamage(power), player);
                        particleExplosion(location);

                        // possibly set entity on fire
                        if (new Random().nextFloat() < 0.33) {
                            raycastTargetAtLocation.livingEntity.setFireTicks(80);
                        }
                    }

                    cancel();
                }
            }
        }.runTaskTimer(Spells.main, 0, 1);

        return true;
    }

    void particleExplosion(Location location) {
        location.getWorld().spawnParticle(Particle.FLAME, location, 16, 0, 0, 0, 0.15);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1.5f);
    }

    int getDamage(Power power) {
        switch (power) {
            case WEAK:
                return 2;
            case STRONG:
                return 3;
            case POWERFUL:
                return 4;
            default:
                return 0;
        }
    }

    @Override
    public String getDescription(Power power) {
        return "Shoot a beam of fire, dealing " + getDamage(power) + " damage, with a chance of setting the enemy on fire.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 8;
    }
}
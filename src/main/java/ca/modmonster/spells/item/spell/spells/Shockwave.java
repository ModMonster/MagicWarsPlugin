package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.item.spell.spells.minion.Minion;
import ca.modmonster.spells.util.PlaySound;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shockwave extends Spell {
    @Override
    public String getId() {
        return "shockwave";
    }

    @Override
    public Material getMaterial() {
        return Material.NOTE_BLOCK;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
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
            new ShockwaveClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Sonic Shockwave";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class ShockwaveClickAbility extends Ability {
    public ShockwaveClickAbility() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Location center = event.getPlayer().getLocation();

        PlaySound.shoot(event.getPlayer());

        final float[] radius = {0f};
        final float maxRadius;

        switch (power) {
            case WEAK:
                maxRadius = 6;
                break;
            case STRONG:
                maxRadius = 9;
                break;
            case POWERFUL:
                maxRadius = 12;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + power);
        }

        // spawn particles
        new BukkitRunnable() {
            @Override
            public void run() {
                // play sound
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1 - (radius[0] / maxRadius) + 0.2f, 1);

                radius[0] += 1f;
                float angle = 0f;

                List<Entity> yeetEntities = new ArrayList<>();

                // spawn particle circle
                for (int i = 0; i < 63; i++) {
                    double x = (radius[0] * Math.sin(angle));
                    double z = (radius[0] * Math.cos(angle));

                    angle += 0.1;

                    Location spawnLocation = new Location(center.getWorld(), center.getX()+x, center.getY(), center.getZ()+z);
                    center.getWorld().spawnParticle(Particle.REDSTONE, spawnLocation, 0, 0, 0, 0, new Particle.DustOptions(Color.WHITE, 1));

                    for (Entity entity : center.getWorld().getNearbyEntities(spawnLocation, 0.5f, 0.5f, 0.5f)) {
                        if (entity.equals(event.getPlayer())) continue;
                        if (Minion.isMinion(entity) && Minion.getMinionOwner(entity).equals(event.getPlayer())) continue;
                        if (entity instanceof Player && ((Player) entity).getGameMode().equals(GameMode.SPECTATOR)) continue;

                        yeetEntities.add(entity);
                    }
                }

                if (radius[0] > maxRadius) cancel();

                for (Entity entity : yeetEntities) {
                    Vector launchDirection = entity.getLocation().toVector().add(event.getPlayer().getLocation().toVector().multiply(-1));
                    launchDirection.setY(1.5);

                    launchDirection.multiply(0.5);

                    entity.setVelocity(launchDirection);

                    entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.2f, 1);
                }

            }
        }.runTaskTimer(Spells.main, 0, 1);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Create a sonic shockwave which launches all enemies away from the blast.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 40;
    }
}
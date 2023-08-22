package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.item.spell.spells.minion.Minion;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class WitherBall extends Spell {
    @Override
    public String getId() {
        return "wither_ball";
    }

    @Override
    public Material getMaterial() {
        return Material.WITHER_SKELETON_SKULL;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public boolean getStackable() {
        return true;
    }

    @Override
    public boolean getStackableInChests() {
        return false;
    }

    @Override
    public boolean getGlow() {
        return false;
    }

    @Override
    public boolean getConsumeOnUse() {
        return true;
    }

    @Override
    public boolean getHasPower() {
        return false;
    }

    @Override
    public List<Ability> getAbilities() {
        return Collections.singletonList(
            new WitherBallSpellOnClick()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Wither Ball";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class WitherBallSpellOnClick extends Ability {
    public WitherBallSpellOnClick() {
        super(ClickType.CLICK, ClickBlockType.AIR, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 1);

        Fireball skull = player.launchProjectile(WitherSkull.class); // spawn skull
        skull.setIsIncendiary(false); // disable fire
        skull.setYield(0); // disable explosion

        // make skull slower
        double speed = 0.3;
        Vector direction = player.getLocation().getDirection().normalize();

        new BukkitRunnable() {
            @Override
            public void run() {
                // set velocity
                skull.setVelocity(direction.clone().multiply(speed));

                if (skull.isDead()) {
                    cancel();
                }

                // prevent leaving world border
                if (!Utilities.isLocationWithinWorldBorder(skull.getLocation())) {
                    skull.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, skull.getLocation(), 1);
                    skull.getWorld().playSound(skull.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    skull.remove();
                    cancel();
                }

                // wither entities
                for (Entity entity : skull.getNearbyEntities(3, 3, 3)) {
                    if (!(entity instanceof LivingEntity)) continue;
                    if (entity.equals(player)) continue;
                    Player minionOwner = Minion.getMinionOwner(entity);
                    if (minionOwner != null && minionOwner.equals(player)) continue;
                    LivingEntity livingEntity = (LivingEntity) entity;
                    if (livingEntity.hasPotionEffect(PotionEffectType.WITHER)) continue;

                    livingEntity.addPotionEffect(PotionEffectType.WITHER.createEffect(150, 1));
                }

                // wither blocks
                Map<Material, List<Material>> witherMaterials = new HashMap<Material, List<Material>>() {
                    {
                        put(Material.GRASS_BLOCK, Arrays.asList(
                            Material.SOUL_SOIL,
                            Material.SOUL_SAND,
                            Material.COAL_BLOCK,
                            Material.BLACKSTONE
                        ));
                    }
                };

                List<Block> blocks = Utilities.getBlocks(skull.getLocation().getBlock().getLocation(), 2);

                for (Block block : blocks) {
                    // random chance to do nothing
                    if (new Random().nextInt(10) < 9) continue;

                    // spawn wither skeleton minions
                    if (new Random().nextInt(50) >= 49) {
                        Location spawnLocation = block.getLocation();

                        spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 0);
                    }

                    if (!witherMaterials.containsKey(block.getType())) continue;

                    List<Material> availableTypes = witherMaterials.get(block.getType());
                    Material chosenType = availableTypes.get(new Random().nextInt(availableTypes.size()));

                    // set grass above to air
                    Block blockAbove = block.getLocation().clone().add(0, 1, 0).getBlock();
                    if (!blockAbove.getType().isSolid()) {
                        blockAbove.setType(Material.AIR);
                    }

                    block.setType(chosenType);
                }
            }
        }.runTaskTimer(Spells.main, 0, 1);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Shoot a Wither skull in the direction you face, which withers everything within a short distance of it.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return null;
    }
}
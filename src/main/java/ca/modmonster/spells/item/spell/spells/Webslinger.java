package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Webslinger extends Spell {
    @Override
    public String getId() {
        return "webslinger";
    }

    @Override
    public Material getMaterial() {
        return Material.COBWEB;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public boolean getStackable() {
        return true;
    }

    @Override
    public boolean getStackableInChests() {
        return true;
    }

    @Override
    public boolean getGlow() {
        return true;
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
            new WebslingerClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Webslinger";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class WebslingerClickAbility extends Ability {
    public WebslingerClickAbility() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        // spawn cobweb projectile
        FallingBlock block = player.getWorld().spawnFallingBlock(player.getEyeLocation(), Material.COBWEB.createBlockData());
        block.setVelocity(player.getLocation().getDirection().multiply(2));
        block.setDropItem(false);

        // play sound
        player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1, 0.75f);

        new BukkitRunnable() {
            @Override
            public void run() {
                // spawn webs when web projectile lands
                if (block.isOnGround()) {
                    Location webLocation = block.getLocation();

                    // add main web clump
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            for (int z = -1; z < 2; z++) {
                                Location newLocation = webLocation.clone().add(x, y, z);

                                if (!newLocation.getBlock().isSolid()) {
                                    newLocation.getBlock().setType(Material.COBWEB);

                                    final int[] blockLifespan = {new Random().nextInt(10)};
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            blockLifespan[0] -= 1;
                                            if (blockLifespan[0] <= 0) {
                                                newLocation.getBlock().setType(Material.AIR);
                                                cancel();
                                            }
                                        }
                                    }.runTaskTimer(Spells.main, 20, 1);
                                }
                            }
                        }
                    }

                    // add stray webs
                    for (int i = 0; i < new Random().nextInt(20) + 10; i++) {
                        int x = new Random().nextInt(7) - 3;
                        int y = new Random().nextInt(7) - 3;
                        int z = new Random().nextInt(7) - 3;

                        Location newLocation = webLocation.clone().add(x, y, z);

                        if (!newLocation.getBlock().isSolid()) {
                            newLocation.getBlock().setType(Material.COBWEB);
                            final int[] blockLifespan = {new Random().nextInt(10)};
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    blockLifespan[0] -= 1;
                                    if (blockLifespan[0] <= 0) {
                                        newLocation.getBlock().setType(Material.AIR);
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(Spells.main, 20, 1);
                        }
                    }

                    cancel();
                }
            }
        }.runTaskTimer(Spells.main, 1, 1);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Shoot a bundle of cobwebs in the direction you look.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 20;
    }
}

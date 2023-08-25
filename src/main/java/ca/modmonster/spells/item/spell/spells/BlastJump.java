package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.events.OnEntityDamage;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class BlastJump extends Spell {
    @Override
    public String getId() {
        return "blast_jump";
    }

    @Override
    public Material getMaterial() {
        return Material.FIREWORK_ROCKET;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
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
            new BlastJumpRightClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Blast Jump";
    }

    @Override
    public String getDescription(Power power) {
        return "Wheeeee!";
    }
}

class BlastJumpRightClickAbility extends Ability {
    public BlastJumpRightClickAbility() {
        super(ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        // prevent using while flying
        if (player.isGliding()) {
            PlaySound.error(player);
            event.getPlayer().sendMessage(Utilities.stringToComponent("&cYou cannot use this spell while &bflying&c."));
            return false;
        }

        float speed = 2f;

        Vector velocity = new Vector(
            -player.getLocation().getDirection().getX() * speed,
            -player.getLocation().getDirection().getY() * speed,
            -player.getLocation().getDirection().getZ() * speed
        );

        final int[] runCount = {0};

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setVelocity(velocity);

                runCount[0] += 1;
                if (runCount[0] > 4) {
                    cancel();
                }
            }
        }.runTaskTimer(Spells.main, 0, 1);

        // prevent next fall
        OnEntityDamage.addFallProofEntity(player);

        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 10, 1, 1, 1, 0.1f);

        // play sound
        PlaySound.explode(player);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Blast backwards in the opposite direction you were facing.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 30;
    }
}
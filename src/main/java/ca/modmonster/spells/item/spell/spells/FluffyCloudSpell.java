package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class FluffyCloudSpell extends Spell {
    @Override
    public String getId() {
        return "fluffy_cloud";
    }

    @Override
    public Material getMaterial() {
        return Material.WHITE_WOOL;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
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
            new FluffyCloudOnClick()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Fluffy Cloud";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class FluffyCloudOnClick extends Ability {
    public FluffyCloudOnClick() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        // add slow falling to player
        player.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(Integer.MAX_VALUE, 4).withParticles(false).withAmbient(true).withIcon(false));

        // play sound
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);

        final int[] tickCounter = {0};

        // spawn cloud particles
        new BukkitRunnable() {
            @Override
            public void run() {
                tickCounter[0] += 1;
                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().subtract(new Vector(0, 0.5, 0)), 10, 1, 0, 1, 0);

                //noinspection deprecation
                if (tickCounter[0] >= 20 && player.isOnGround() || !GameManager.activeGame.isAlive(player)) {
                    player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    cancel();
                }
            }
        }.runTaskTimer(Spells.main, 1, 1);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Spawn a fluffy cloud underneath your feet which you can float on. The cloud will last until you hit the ground.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 20;
    }
}
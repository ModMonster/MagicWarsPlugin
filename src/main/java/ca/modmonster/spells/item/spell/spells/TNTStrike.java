package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.RaycastTarget;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TNTStrike extends Spell {
    @Override
    public String getId() {
        return "tnt_strike";
    }

    @Override
    public Material getMaterial() {
        return Material.TNT;
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
        return false;
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
            new TNTStrikeClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Airstrike";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class TNTStrikeClickAbility extends Ability {
    public TNTStrikeClickAbility() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        final float strikeDistance = 40;
        final float strikeSize = 20;
        final float strikeYMultiplier = 4;

        Player player = event.getPlayer();
        RaycastTarget raycastTarget = Utilities.raycast(player, 80, 1);
        Location strikeLocation;
        if (raycastTarget == null || raycastTarget.getLocation() == null) {
            strikeLocation = player.getLocation().add(player.getLocation().getDirection().multiply(strikeDistance));
            strikeLocation.setY(player.getLocation().getY() + 15);
        } else {
            strikeLocation = raycastTarget.getLocation();
            strikeLocation.add(0, 15, 0);
        }

        // play warning sound
        player.playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
        strikeLocation.getWorld().playSound(strikeLocation, Sound.ENTITY_TNT_PRIMED, 2, 1);

        for (int i = 0; i < 100; i++) {
            Location randomizedStrikeLocation = strikeLocation.clone().add((new Random().nextFloat() * strikeSize) - (strikeSize / 2), new Random().nextFloat() * strikeSize * strikeYMultiplier, (new Random().nextFloat() * strikeSize) - (strikeSize / 2));

            TNTPrimed tnt = player.getWorld().spawn(randomizedStrikeLocation, TNTPrimed.class);
            tnt.setFuseTicks(Integer.MAX_VALUE);
            tnt.setYield(1.5f);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (tnt.isOnGround()) {
                        tnt.setFuseTicks(0);
                        cancel();
                    }
                }
            }.runTaskTimer(Spells.main, 1, 1);
        }

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Make TNT fall from the sky in the direction you look.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 20;
    }
}
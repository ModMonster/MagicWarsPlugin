package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FangsOfFury extends Spell {
    @Override
    public String getId() {
        return "fangs_of_fury";
    }

    @Override
    public Material getMaterial() {
        return Material.POINTED_DRIPSTONE;
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
        return Arrays.asList(
            new FangsOfFuryLeftClickAbility(),
            new FangsOfFuryRightClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Fangs of Fury";
    }

    @Override
    public String getDescription(Power power) {
        return "*chomp chomp*";
    }

    static void spawnAndTagFang(Player player, Location location) {
        Location groundLocation = Utilities.getHighestBlockYAtLocation(location);

        EvokerFangs fang = (EvokerFangs) location.getWorld().spawnEntity(groundLocation, EntityType.EVOKER_FANGS);
        fang.setOwner(player);
    }
}

class FangsOfFuryLeftClickAbility extends Ability {
    public FangsOfFuryLeftClickAbility() {
        super(ClickType.LEFT_CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();
        int distance = getDistance(power);
        Vector direction = player.getLocation().getDirection().normalize();
        direction.setY(0);

        int currentDistance = 1;

        while (currentDistance < distance) {
            Location location = player.getLocation().add(direction.clone().multiply(currentDistance));
            currentDistance += 1;

            new BukkitRunnable() {
                @Override
                public void run() {
                    FangsOfFury.spawnAndTagFang(player, location);
                }
            }.runTaskLater(Spells.main, currentDistance);
        }

        return true;
    }

    int getDistance(Power power) {
        switch (power) {
            case WEAK:
                return 6;
            case STRONG:
                return 9;
            case POWERFUL:
                return 12;
            default:
                return 0;
        }
    }

    @Override
    public String getDescription(Power power) {
        return "Summon a line of hungry fangs in the direction you look.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 30;
    }
}

class FangsOfFuryRightClickAbility extends Ability {
    public FangsOfFuryRightClickAbility() {
        super(ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();
        int radius = getRadius(power);
        Location center = player.getLocation();

        int totalFangCount = new Random().nextInt(2 * radius) + 2 * radius * 4;
        float angle = 0f;

        for (int i = 0; i < totalFangCount; i++) {
            angle += new Random().nextFloat() * 6.4;
            float newFangRadius = new Random().nextFloat() * radius + 1;

            double x = (newFangRadius * Math.sin(angle));
            double z = (newFangRadius * Math.cos(angle));

            Location spawnLocation = new Location(center.getWorld(), center.getX()+x, center.getY(), center.getZ()+z);

            new BukkitRunnable() {
                @Override
                public void run() {
                    FangsOfFury.spawnAndTagFang(player, spawnLocation);
                }
            }.runTaskLater(Spells.main, new Random().nextInt(20));
        }

        return true;
    }

    int getRadius(Power power) {
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
        return "Summon a sphere of fangs centered around you.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 50;
    }
}
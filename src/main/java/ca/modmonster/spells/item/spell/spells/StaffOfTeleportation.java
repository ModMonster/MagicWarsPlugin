package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class StaffOfTeleportation extends Spell {
    @Override
    public String getId() {
        return "staff_of_teleportation";
    }

    @Override
    public Material getMaterial() {
        return Material.ECHO_SHARD;
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
            new StaffOfTeleportationRightClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Staff of Teleportation";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class StaffOfTeleportationRightClickAbility extends Ability {
    public StaffOfTeleportationRightClickAbility() {
        super(ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        int blocksToTeleport = switch (power) {
            case WEAK -> 5;
            case STRONG -> 7;
            case POWERFUL -> 9;
        };

        Location teleportLocation;
        int i = 0;

        while (true) {
            teleportLocation = player.getLocation();
            Vector dir = teleportLocation.getDirection();
            dir.normalize();
            dir.multiply(blocksToTeleport - i);
            teleportLocation.add(dir);

            if (i >= blocksToTeleport) {
                noSafeSpotFound(player);
                return false;
            }

            if (!teleportLocation.getBlock().isSolid() && Utilities.isLocationWithinWorldBorder(teleportLocation) && teleportLocation.getBlockY() > 0) {
                break;
            }

            i++;
        }

        player.teleport(teleportLocation); // teleport player
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1); // play teleport sound

        return true;
    }

    void noSafeSpotFound(Player player) {
        PlaySound.error(player);
        player.sendMessage(Utilities.stringToComponent("&cNo safe teleport location found!"));
    }

    @Override
    public String getDescription(Power power) {
        int blocksToTeleport = switch (power) {
            case WEAK -> 5;
            case STRONG -> 7;
            case POWERFUL -> 9;
        };

        return "Teleport " + blocksToTeleport + " blocks in the direction you look. Can be used to phase through walls.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return switch (power) {
            case WEAK -> 40;
            case STRONG -> 30;
            case POWERFUL -> 20;
        };
    }
}
package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Snowball;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.List;

public class Slowball extends Spell {
    @Override
    public String getId() {
        return "slowball";
    }

    @Override
    public Material getMaterial() {
        return Material.SNOWBALL;
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
        return true;
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
            new SlowballAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Slowball";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class SlowballAbility extends Ability {
    public SlowballAbility() {
        super(ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        // throw snowball
        Snowball snowball = event.getPlayer().launchProjectile(Snowball.class);
        Utilities.setPersistentEntityTag(snowball, "projectile_type", "slowball");
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_SNOWBALL_THROW, 0.5f, 0.5f);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Throw a snowball which slows the player hit by it.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return null;
    }
}
package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.List;

public class FireballSpell extends Spell {
    @Override
    public String getId() {
        return "fireball";
    }

    @Override
    public Material getMaterial() {
        return Material.FIRE_CHARGE;
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
            new FireballSpellClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Fireball";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class FireballSpellClickAbility extends Ability {
    public FireballSpellClickAbility() {
        super(ClickType.CLICK, ClickBlockType.AIR, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        PlaySound.shoot(player);

        Fireball fireball = player.launchProjectile(Fireball.class); // spawn fireball
        fireball.setIsIncendiary(false); // disable fire
        fireball.setYield(0); // disable explosion

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Shoot a fireball in the direction you look.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return null;
    }
}
package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.InvisibilityManager;
import ca.modmonster.spells.util.PlaySound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Blink extends Spell {
    @Override
    public String getId() {
        return "blink";
    }

    @Override
    public Material getMaterial() {
        return Material.GHAST_TEAR;
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
            new BlinkClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Blink";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class BlinkClickAbility extends Ability {
    public BlinkClickAbility() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        // give potion effects
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(100, 1));
        InvisibilityManager.makeEntityInvisibleForTicks(player, 100);

        // play sound effect
        PlaySound.potion(player);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Temporarily become invisible.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 80;
    }
}
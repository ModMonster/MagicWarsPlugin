package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Quickpaw extends Spell {
    @Override
    public String getId() {
        return "quickpaw";
    }

    @Override
    public Material getMaterial() {
        return Material.RABBIT_FOOT;
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
            new QuickpawClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Quickpaw";
    }

    @Override
    public String getDescription(Power power) {
        return "\"That's a quick nope out of there.\"";
    }
}

class QuickpawClickAbility extends Ability {
    public QuickpawClickAbility() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        // give potion effects
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(100, 1));
        player.addPotionEffect(PotionEffectType.JUMP_BOOST.createEffect(100, 1));

        // play sound effect
        PlaySound.potion(player);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Call upon the god of the Rabbit to grant you a temporary speed and jump boost.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 80;
    }
}

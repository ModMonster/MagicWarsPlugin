package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.events.OnEntityDamage;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class PixieDust extends Spell {
    @Override
    public String getId() {
        return "pixie_dust";
    }

    @Override
    public Material getMaterial() {
        return Material.SUGAR;
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
            new PixieDustRightClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Pixie Dust";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class PixieDustRightClickAbility extends Ability {
    public PixieDustRightClickAbility() {
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

        // play sound
        player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 1, 1);

        // make player float
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 2, false, false, false));

        // prevent next fall
        OnEntityDamage.addFallProofEntity(player);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Levitate into the air for 5 seconds, then safely fall back down.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 80;
    }
}
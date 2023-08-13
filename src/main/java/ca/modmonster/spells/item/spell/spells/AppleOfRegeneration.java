package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.List;

public class AppleOfRegeneration extends Spell {
    @Override
    public String getId() {
        return "apple_of_regeneration";
    }

    @Override
    public Material getMaterial() {
        return Material.APPLE;
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
            new AppleOfRegenerationRightClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Apple of Regeneration";
    }

    @Override
    public String getDescription(Power power) {
        return "An apple a day keeps the doctor away!";
    }
}

class AppleOfRegenerationRightClickAbility extends Ability {
    public AppleOfRegenerationRightClickAbility() {
        super(ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        if (player.getHealth() >= event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            PlaySound.error(player);
            player.sendMessage(Utilities.stringToComponent("&cYour health is already full!"));
            return false;
        }

        // show particles
        player.getWorld().spawnParticle(Particle.HEART, player.getEyeLocation(), 10, 1, 1, 1);

        // play sound
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);

        // heal player
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double newHealth = player.getHealth() + 8;

        player.setHealth(Math.min(newHealth, maxHealth));

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Heal 4 hearts.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return null;
    }
}
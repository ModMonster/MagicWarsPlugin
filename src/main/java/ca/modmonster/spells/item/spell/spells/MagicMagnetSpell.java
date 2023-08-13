package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.RaycastTarget;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class MagicMagnetSpell extends Spell {
    @Override
    public String getId() {
        return "magic_magnet";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_INGOT;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
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
            new MagicMagnetOnClick()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Magic Magnet";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }
}

class MagicMagnetOnClick extends Ability {
    public MagicMagnetOnClick() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();
        RaycastTarget raycastTarget = Utilities.raycast(player, getMaxDistance(power), 0.5f);
        if (raycastTarget == null) return error(event);
        LivingEntity target = raycastTarget.livingEntity;
        if (target == null) return error(event);

        // yeet player towards other player
        Vector launchDirection = target.getEyeLocation().toVector().add(player.getLocation().toVector().multiply(-1)).multiply(-1);
        launchDirection.setY(2);
        launchDirection.multiply(0.2);

        target.setVelocity(launchDirection);

        return true;
    }

    int getMaxDistance(Power power) {
        switch (power) {
            case WEAK:
                return 24;
            case STRONG:
                return 32;
            case POWERFUL:
                return 40;
            default:
                return 0;
        }
    }

    @SuppressWarnings("SameReturnValue")
    boolean error(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Utilities.stringToComponent("&cYou must click on an &benemy &cto pull!"));
        PlaySound.error(player);
        return false;
    }

    @Override
    public String getDescription(Power power) {
        return "Magically pull the clicked enemy towards you.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 80;
    }
}

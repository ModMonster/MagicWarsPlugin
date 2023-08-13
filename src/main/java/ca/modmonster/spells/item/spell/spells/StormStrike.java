package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.RaycastTarget;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.List;

public class StormStrike extends Spell {
    @Override
    public String getId() {
        return "storm_strike";
    }

    @Override
    public Material getMaterial() {
        return Material.PRISMARINE_SHARD;
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
            new StormStrikeClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Storm Strike";
    }

    @Override
    public String getDescription(Power power) {
        return "Thou hast been smitten!";
    }
}

class StormStrikeClickAbility extends Ability {
    public StormStrikeClickAbility() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();
        RaycastTarget hit = Utilities.raycast(player, 100, 1f);

        if (hit != null) {
            if (hit.block != null) {
                hit.block.getWorld().strikeLightning(hit.block.getLocation());
                return true;
            } else if (hit.livingEntity != null) {
                hit.livingEntity.getWorld().strikeLightning(hit.livingEntity.getLocation());
                return true;
            }
        }

        PlaySound.error(player);
        player.sendMessage(Utilities.stringToComponent("&cYou must look at a &bblock &cor &bmob &cto strike with lightning!"));

        return false;
    }

    @Override
    public String getDescription(Power power) {
        return "Send a bolt of lightning to electrocute the player you look at.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 300;
    }
}

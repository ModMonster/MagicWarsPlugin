package ca.modmonster.spells.item.spell;

import org.bukkit.event.player.PlayerInteractEvent;

public abstract class Ability {
    public final ClickType clickType;
    public final ClickBlockType clickBlockType;
    public final ShiftType shiftType;

    public Ability(ClickType abilityType, ClickBlockType clickBlockType, ShiftType shiftType) {
        this.clickType = abilityType;
        this.clickBlockType = clickBlockType;
        this.shiftType = shiftType;
    }

    public abstract boolean onUse(PlayerInteractEvent event, Power power);
    public abstract String getDescription(Power power);
    public abstract Integer getCooldown(Power power);
}
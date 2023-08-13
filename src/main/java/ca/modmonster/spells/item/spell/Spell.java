package ca.modmonster.spells.item.spell;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Spell {
    public abstract String getId();
    public abstract Material getMaterial();
    public abstract Rarity getRarity();
    public abstract boolean getStackable();
    public abstract boolean getStackableInChests();
    public abstract boolean getGlow();
    public abstract boolean getConsumeOnUse();
    public abstract boolean getHasPower();
    public abstract List<Ability> getAbilities();
    public abstract List<Material> getLinkedCooldowns();

    public abstract String getName(Power power);
    public abstract String getDescription(Power power);

    public static Power getPower(ItemStack item) {
        return Power.get(Utilities.getPersistentItemTagInteger(item, "power"));
    }

    public boolean hasAbilityOfType(ClickType clickType) {
        for (Ability ability : getAbilities()) {
            if (ability.clickType.equals(ClickType.CLICK)) return true;
            if (ability.clickType.equals(clickType)) return true;
        }

        return false;
    }

    public boolean hasAbilityOfType(ClickType clickType, ClickBlockType clickBlockType) {
        for (Ability ability : getAbilities()) {
            if (ability.clickType.equals(ClickType.CLICK) && ability.clickBlockType.equals(ClickBlockType.NONE)) return true;
            if (ability.clickType.equals(ClickType.CLICK) && ability.clickBlockType.equals(clickBlockType)) return true;
            if (ability.clickType.equals(clickType) && ability.clickBlockType.equals(ClickBlockType.NONE)) return true;
            if (ability.clickType.equals(clickType) && ability.clickBlockType.equals(clickBlockType)) return true;
        }

        return false;
    }

    public void triggerAbility(PlayerInteractEvent event, ClickType clickType, ClickBlockType clickBlockType, ShiftType shiftType, ItemStack item) {
        Power power = getPower(item);

        for (Ability ability : getAbilities()) {
            if (ability.clickType.equals(clickType) && ability.clickBlockType.equals(clickBlockType) && ability.shiftType.equals(shiftType)) {
                Player player = event.getPlayer();

                // check cooldown
                int cooldownTime = player.getCooldown(item.getType());

                if (cooldownTime != 0) return;

                boolean success = ability.onUse(event, power);

                // if we return false, skip cooldown and consuming item
                if (!success) {
                    return;
                }

                // consume item
                if (getConsumeOnUse() && !player.getGameMode().equals(GameMode.CREATIVE)) {
                    item.setAmount(item.getAmount() - 1);
                }

                // activate cooldown
                if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                    if (ability.getCooldown(power) == null) return;

                    if (ability.getCooldown(power) == -1) {
                        setCooldowns(player, 1000000000);
                    } else {
                        setCooldowns(player, ability.getCooldown(power));
                    }
                }
            }
        }
    }

    void setCooldowns(Player player, int cooldownTime) {
        player.setCooldown(getMaterial(), cooldownTime);

        if (getLinkedCooldowns() != null) {
            for (Material material : getLinkedCooldowns()) {
                player.setCooldown(material, cooldownTime);
            }
        }
    }
}

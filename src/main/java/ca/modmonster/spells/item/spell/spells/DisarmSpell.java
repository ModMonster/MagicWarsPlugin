package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.RaycastTarget;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class DisarmSpell extends Spell {
    @Override
    public String getId() {
        return "disarm";
    }

    @Override
    public Material getMaterial() {
        return Material.RED_DYE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
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
            new DisarmSpellOnClick()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Disarm";
    }

    @Override
    public String getDescription(Power power) {
        return "\"Expelliarmus!\"";
    }
}

class DisarmSpellOnClick extends Ability {
    public DisarmSpellOnClick() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();
        RaycastTarget raycastTarget = Utilities.raycast(player, 40, 0.5f);
        if (raycastTarget == null) return error(event);
        LivingEntity target = raycastTarget.livingEntity;
        if (target == null) return error(event);
        ItemStack heldItem = target.getEquipment().getItemInMainHand();

        // check if target is holding anything
        if (heldItem.getType().isAir()) return notHoldingError(event);

        // send message to target
        if (target instanceof Player) {
            target.sendActionBar(Utilities.stringToComponent("&4&k   &e You have been &bdisarmed &eby &b" + player.getName() + "&e! &4&k   "));
            PlaySound.pop((Player) target);
        }

        // remove item
        target.getEquipment().setItemInMainHand(null);

        // throw item towards player
        Vector launchDirection = target.getEyeLocation().toVector().add(player.getLocation().toVector().multiply(-1)).multiply(-1);
        launchDirection.setY(1.5);
        launchDirection.multiply(0.075);

        Item droppedItem = target.getWorld().dropItem(target.getEyeLocation(), heldItem);
        droppedItem.setVelocity(launchDirection);

        return true;
    }

    @SuppressWarnings("SameReturnValue")
    boolean error(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Utilities.stringToComponent("&cYou must click on an &benemy &cto disarm!"));
        PlaySound.error(player);
        return false;
    }

    @SuppressWarnings("SameReturnValue")
    boolean notHoldingError(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Utilities.stringToComponent("&cThe &benemy &cyou clicked wasn't &bholding anything&c!"));
        PlaySound.error(player);
        return false;
    }

    @Override
    public String getDescription(Power power) {
        return "Disarm a player you look at, causing their held item to be shot towards you.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 40;
    }
}
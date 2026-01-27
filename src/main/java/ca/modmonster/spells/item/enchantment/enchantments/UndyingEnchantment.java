package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

// omg like undyne??? undyne the undying?!?1!?!?/
public class UndyingEnchantment extends ArmorEnchantment {
    @Override
    public String getId() {
        return "undying";
    }

    @Override
    public String getName() {
        return "Undying";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public EnchantmentType getType() {
        return EnchantmentType.CHESTPLATE;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String[] getConflictingEnchantments() {
        return new String[]{"ender_infused"};
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When killed: &7Acts similarly to a Totem of Undying. Enchantment removed after use.";
    }

    @Override
    public void onDeath(EntityDeathEvent event, Integer level) {
        LivingEntity entity = event.getEntity();

        // restore health to half heart
        entity.setHealth(1);

        // remove all potion effects
        for (PotionEffect effect : entity.getActivePotionEffects()) {
            entity.removePotionEffect(effect.getType());
        }

        // add potion effects
        entity.addPotionEffect(PotionEffectType.REGENERATION.createEffect(900, 1));
        entity.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(800, 0));
        entity.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(100, 1));

        // play sound
        entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);

        // show totem animation
        entity.playEffect(EntityEffect.PROTECTED_FROM_DEATH);
        // TODO: check that this doesn't show something stupid

        // remove enchantment
        ItemStack chestplate = Objects.requireNonNull(entity.getEquipment()).getChestplate();
        EnchantmentManager.removeEnchant(chestplate, "undying");

        event.setCancelled(true); // cancel death
    }
}

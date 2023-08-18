package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.C;

import java.util.Collections;

public class UndyingEnchantment extends ArmorEnchantment {
    public UndyingEnchantment() {
        super(
            "undying",
            "Undying",
            Rarity.LEGENDARY,
            EnchantmentType.CHESTPLATE,
            1,
            Collections.singletonList(
                NamespacedKey.minecraft("ender_infused")
            )
        );
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
        entity.playEffect(EntityEffect.TOTEM_RESURRECT);

        // remove enchantment
        ItemMeta chestplateMeta = entity.getEquipment().getChestplate().getItemMeta();
        chestplateMeta.removeEnchant(Enchantment.getByKey(NamespacedKey.minecraft("undying")));
        entity.getEquipment().getChestplate().setItemMeta(chestplateMeta);

        EnchantmentManager.updateEnchantedItemLore(entity.getEquipment().getChestplate());

        event.setCancelled(true); // cancel death
    }
}

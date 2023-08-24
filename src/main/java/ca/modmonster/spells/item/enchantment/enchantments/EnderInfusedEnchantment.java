package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Collections;

public class EnderInfusedEnchantment extends ArmorEnchantment {
    public EnderInfusedEnchantment() {
        super(
            "ender_infused",
            "Ender Infused",
            Rarity.MYTHIC,
            EnchantmentType.CHESTPLATE,
            1,
            Collections.singletonList(
                NamespacedKey.minecraft("undying")
            )
        );
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When killed: &7Teleports you to a random location in world and regenerates your health to 4 hearts. Enchantment removed after use.";
    }

    @Override
    public void onDeath(EntityDeathEvent event, Integer level) {
        LivingEntity entity = event.getEntity();

        // restore health
        entity.setHealth(8);

        // remove all potion effects
        for (PotionEffect effect : entity.getActivePotionEffects()) {
            entity.removePotionEffect(effect.getType());
        }

        // teleport player
        entity.teleport(Utilities.getValidTeleportLocationWithinWorldBorder(event.getEntity().getWorld()));

        // play sound
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        // remove enchantment
        ItemMeta chestplateMeta = entity.getEquipment().getChestplate().getItemMeta();
        chestplateMeta.removeEnchant(Enchantment.getByKey(NamespacedKey.minecraft("ender_infused")));
        entity.getEquipment().getChestplate().setItemMeta(chestplateMeta);

        EnchantmentManager.updateEnchantedItemLore(entity.getEquipment().getChestplate());

        event.setCancelled(true); // cancel death
    }
}

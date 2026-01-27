package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class EnderInfusedEnchantment extends ArmorEnchantment {
    @Override
    public String getId() {
        return "ender_infused";
    }

    @Override
    public String getName() {
        return "Ender Infused";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
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
        return new String[]{"undying"};
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
        ItemStack chestplate = Objects.requireNonNull(entity.getEquipment()).getChestplate();
        EnchantmentManager.removeEnchant(chestplate, "ender_infused");

        event.setCancelled(true); // cancel death
    }
}

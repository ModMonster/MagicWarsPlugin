package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

// I'm pretty good at this one
public class SelfDestructEnchantment extends ArmorEnchantment {
    @Override
    public String getId() {
        return "self_destruct";
    }

    @Override
    public String getName() {
        return "Self Destruct";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public EnchantmentType getType() {
        return EnchantmentType.ARMOR;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When killed: &7Spawns primed TNT at your death location.";
    }

    @Override
    public void onDeath(EntityDeathEvent event, Integer level) {
        Location location = event.getEntity().getLocation();

        for (int i = 0; i < new Random().nextInt(6) + 6; i++) {
            Location spawnLocation = location.clone().add((new Random().nextDouble() * 4) - 2, 0, (new Random().nextDouble() * 4) - 2);
            location.getWorld().spawnEntity(spawnLocation, EntityType.TNT);
        }
    }
}

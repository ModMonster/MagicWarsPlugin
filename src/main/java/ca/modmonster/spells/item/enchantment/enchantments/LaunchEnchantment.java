package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.item.enchantment.SwordEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class LaunchEnchantment extends SwordEnchantment {
    public LaunchEnchantment() {
        super(
            "launch",
            "Launch",
            Rarity.RARE,
            EnchantmentType.SWORD,
            3,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        return "&8When attacking enemy: &7Launches enemy into the air!";
    }

    @Override
    public void onHitEntity(EntityDamageByEntityEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();

        new BukkitRunnable() {
            @Override
            public void run() {
                entity.setVelocity(new Vector(0, (level + 2) / 2.9, 0));
            }
        }.runTaskLater(Spells.main, 1);
    }
}

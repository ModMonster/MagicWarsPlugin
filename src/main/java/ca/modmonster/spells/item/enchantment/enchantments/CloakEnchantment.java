package ca.modmonster.spells.item.enchantment.enchantments;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.enchantment.ArmorEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentType;
import ca.modmonster.spells.util.InvisibilityManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class CloakEnchantment extends ArmorEnchantment {
    public CloakEnchantment() {
        super(
            "cloak",
            "Cloak",
            Rarity.RARE,
            EnchantmentType.ARMOR,
            3,
            new ArrayList<>()
        );
    }

    @Override
    public String getDescription(Integer level) {
        if (level == 1) {
            return "&8When hit: &7Gives you invisibility for 1 second.";
        }
        return "&8When hit: &7Makes you invisible for " + level + " seconds.";
    }

    @Override
    public void onTakeDamage(EntityDamageEvent event, Integer level) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();

        entity.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(level * 20, 0));
        InvisibilityManager.makeEntityInvisibleForTicks(entity, level * 20);
    }
}

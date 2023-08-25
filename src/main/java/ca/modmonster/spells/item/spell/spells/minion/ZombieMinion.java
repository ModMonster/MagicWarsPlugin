package ca.modmonster.spells.item.spell.spells.minion;

import ca.modmonster.spells.item.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class ZombieMinion extends Minion {
    @Override
    public Material getMaterial() {
        return Material.ZOMBIE_SPAWN_EGG;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public String getMinionId() {
        return "zombie";
    }

    @Override
    public String getMinionName() {
        return "Zombie";
    }

    @Override
    public EntityType getMinionEntityType() {
        return EntityType.ZOMBIE;
    }

    @Override
    public int getCooldown() {
        return 80;
    }
}
package ca.modmonster.spells.item.spell.spells.minion;

import ca.modmonster.spells.item.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SkeletonMinion extends Minion {
    @Override
    public Material getMaterial() {
        return Material.SKELETON_SPAWN_EGG;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public String getMinionId() {
        return "skeleton";
    }

    @Override
    public String getMinionName() {
        return "Skeleton";
    }

    @Override
    public EntityType getMinionEntityType() {
        return EntityType.SKELETON;
    }
}
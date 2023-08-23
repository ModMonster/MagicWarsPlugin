package ca.modmonster.spells.item.spell.spells.minion;

import ca.modmonster.spells.item.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class WitherSkeletonMinion extends Minion {
    @Override
    public Material getMaterial() {
        return Material.WITHER_SKELETON_SPAWN_EGG;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public String getMinionId() {
        return "wither_skeleton";
    }

    @Override
    public String getMinionName() {
        return "Wither Skeleton";
    }

    @Override
    public EntityType getMinionEntityType() {
        return EntityType.WITHER_SKELETON;
    }

    @Override
    public int getCooldown() {
        return 160;
    }
}
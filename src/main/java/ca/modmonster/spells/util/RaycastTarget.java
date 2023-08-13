package ca.modmonster.spells.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class RaycastTarget {
    public final Entity entity;
    public final LivingEntity livingEntity;
    public final Block block;

    public RaycastTarget(Entity entity, LivingEntity livingEntity, Block block) {
        this.entity = entity;
        this.livingEntity = livingEntity;
        this.block = block;
    }

    public @Nullable Location getLocation() {
        if (entity != null) return entity.getLocation();
        if (block != null) return block.getLocation();
        return null;
    }
}

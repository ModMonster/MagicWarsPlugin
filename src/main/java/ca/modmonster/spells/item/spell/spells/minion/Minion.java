package ca.modmonster.spells.item.spell.spells.minion;

import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.RaycastTarget;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class Minion extends Spell {
    public static final Map<Entity, UUID> playerMinions = new HashMap<>();
    public abstract String getMinionId();
    public abstract String getMinionName();
    public abstract EntityType getMinionEntityType();
    public abstract int getCooldown();

    @Override
    public String getId() {
        return getMinionId() + "_minion";
    }

    @Override
    public boolean getStackable() {
        return false;
    }

    @Override
    public boolean getStackableInChests() {
        return false;
    }

    @Override
    public boolean getGlow() {
        return false;
    }

    @Override
    public boolean getConsumeOnUse() {
        return false;
    }

    @Override
    public boolean getHasPower() {
        return false;
    }

    @Override
    public List<Ability> getAbilities() {
        return Arrays.asList(
            new MinionClickAbility(getMinionId(), getMinionName(), getMinionEntityType(), getCooldown()),
            new MinionShiftClickAbility(getMinionName())
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return Arrays.asList(
            Material.ZOMBIE_SPAWN_EGG,
            Material.SKELETON_SPAWN_EGG,
            Material.WITHER_SKELETON_SPAWN_EGG
        );
    }

    @Override
    public String getName(Power power) {
        return getMinionName() + " Minion";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }

    public static Player getMinionOwner(Entity minion) {
        String minionOwnerUuidAsString = Utilities.getPersistentEntityTagString(minion, "minion_owner");
        if (minionOwnerUuidAsString == null) return null;

        return Bukkit.getPlayer(UUID.fromString(minionOwnerUuidAsString));
    }

    public static boolean isMinion(Entity entity) {
        return getMinionOwner(entity) != null;
    }

    public static void dismissAllMinions(Player player) {
        Iterator<Map.Entry<Entity, UUID>> it = playerMinions.entrySet().iterator();

        while (it.hasNext())
        {
            // get entry
            Map.Entry<Entity, UUID> entry = it.next();

            if (player.getUniqueId().equals(entry.getValue())) {
                Entity entity = entry.getKey();

                // spawn particles
                entity.getWorld().spawnParticle(Particle.SMOKE, entity.getLocation().add(0, 2, 0), 10, 0.5, 0.5, 0.5, 0);

                // remove minion from world
                entity.remove();

                // remove minion from map
                it.remove();
            }
        }
    }

    public static void spawnMinion(String minionId, String minionName, EntityType minionType, Player player, Location location) {
        LivingEntity spawnedEntity = (LivingEntity) location.getWorld().spawnEntity(location, minionType);

        // add unbreakable helmet
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);

        ItemMeta meta = helmet.getItemMeta();

        meta.setUnbreakable(true);

        helmet.setItemMeta(meta);

        spawnedEntity.getEquipment().setHelmet(helmet);

        // add data tags
        Utilities.setPersistentEntityTag(spawnedEntity, "minion_type", minionId);
        Utilities.setPersistentEntityTag(spawnedEntity, "minion_name", minionName);
        Utilities.setPersistentEntityTag(spawnedEntity, "minion_owner", player.getUniqueId().toString());
        Utilities.setPersistentEntityTag(spawnedEntity, "minion_lifespan", 600);

        // add name tag
        spawnedEntity.customName(
            Component.text(player.getName(), NamedTextColor.AQUA).
                append(Component.text("'s " + minionName + " Minion", NamedTextColor.RED))
        );
        spawnedEntity.setCustomNameVisible(true);

        // add to map
        Minion.playerMinions.put(spawnedEntity, player.getUniqueId());

    }
}

class MinionClickAbility extends Ability {
    final String minionId;
    final String minionName;
    final EntityType minionType;
    final int cooldown;

    public MinionClickAbility(String minionId, String minionName, EntityType minionType, int cooldown) {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
        this.minionId = minionId;
        this.minionType = minionType;
        this.minionName = minionName;
        this.cooldown = cooldown;
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();
        RaycastTarget hit = Utilities.raycast(player, 200, 0.5f);

        // peaceful mode
        if (player.getWorld().getDifficulty().equals(Difficulty.PEACEFUL)) {
            PlaySound.error(player);
            player.sendMessage(
                Component.text("Cannot spawn minions; world is set to ", NamedTextColor.RED).
                    append(Component.text("peaceful", NamedTextColor.AQUA)).
                    append(Component.text(" difficulty."))
            );
            return false;
        }

        // didn't hit anything
        if (hit == null) {
            PlaySound.error(player);
            player.sendMessage(
                Component.text("You must click on a ", NamedTextColor.RED).
                    append(Component.text("block", NamedTextColor.AQUA)).
                    append(Component.text(" or ")).
                    append(Component.text("mob", NamedTextColor.AQUA)).
                    append(Component.text(" to spawn minions!"))
            );
            return false;
        }

        // play sound
        player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1, 1);

        if (hit.entity != null) {
            spawnMinionsAtLocation(player, hit.entity.getLocation());
        } else if (hit.block != null) {
            spawnMinionsAtLocation(player, hit.block.getLocation());
        }

        return true;
    }

    void spawnMinionsAtLocation(Player player, Location location) {
        for (int i = 0; i < new Random().nextInt(2) + 2; i++) {
            Location spawnLocation = location.clone().add(new Random().nextInt(4) - 2, 1, new Random().nextInt(4) - 2);

            Minion.spawnMinion(minionId, minionName, minionType, player, spawnLocation);
        }
    }

    @Override
    public String getDescription(Power power) {
        return "Summons " + minionName.toLowerCase(Locale.ROOT) + "s to fight for you, which disappear after 30 seconds.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return cooldown;
    }
}

class MinionShiftClickAbility extends Ability {
    final String minionName;

    public MinionShiftClickAbility(String minionName) {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
        this.minionName = minionName;
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Iterator<Map.Entry<Entity, UUID>> it = Minion.playerMinions.entrySet().iterator();
        Player player = event.getPlayer();

        if (it.hasNext()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);
        } else {
            player.sendMessage(Component.text("You don't have any minions to dismiss!", NamedTextColor.RED));
            PlaySound.error(player);
            return false;
        }

        Minion.dismissAllMinions(player);

        return false;
    }

    @Override
    public String getDescription(Power power) {
        return "Dismisses all your minions.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return null;
    }
}
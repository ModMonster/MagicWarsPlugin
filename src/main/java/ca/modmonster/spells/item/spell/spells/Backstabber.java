package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.InvisibilityManager;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.RaycastTarget;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Backstabber extends Spell {
    public static final List<Player> attackInvisibilityPlayers = new ArrayList<>();

    @Override
    public String getId() {
        return "backstabber";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_SWORD;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
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
        return true;
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
        return Collections.singletonList(
            new BackstabberClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "The Backstabber";
    }

    @Override
    public String getDescription(Power power) {
        return "*teleports behind you*";
    }
}

class BackstabberClickAbility extends Ability {
    public BackstabberClickAbility() {
        super(ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        RaycastTarget target = Utilities.raycast(event.getPlayer(), 200, 1f);
        if (target == null) return error(event);
        if (target.livingEntity == null) return error(event);

        Player player = event.getPlayer();
        LivingEntity clickedEntity = target.livingEntity;
        Location teleportLocation = clickedEntity.getLocation().add(clickedEntity.getLocation().getDirection().multiply(-1));

        player.teleport(teleportLocation);
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(200, 0));
        InvisibilityManager.makeEntityInvisibleForTicks(player, 200);
        Backstabber.attackInvisibilityPlayers.add(player);

        return true;
    }

    @SuppressWarnings("SameReturnValue")
    boolean error(PlayerInteractEvent event) {
        event.getPlayer().sendMessage(Utilities.stringToComponent("&cYou must click on an &benemy &cto teleport behind!"));
        PlaySound.error(event.getPlayer());
        return false;
    }

    @Override
    public String getDescription(Power power) {
        return "Teleport behind a player you click, and get invisibility for 10 seconds. Invisibility is removed immediately when attacking.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return 400;
    }
}

package ca.modmonster.spells.item.spell.spells;

import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.item.Rarity;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collections;
import java.util.List;

public class ReturnPearl extends Spell {

    @Override
    public String getId() {
        return "return_pearl";
    }

    @Override
    public Material getMaterial() {
        return Material.ENDER_EYE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public boolean getStackable() {
        return true;
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
        return true;
    }

    @Override
    public boolean getHasPower() {
        return false;
    }

    @Override
    public List<Ability> getAbilities() {
        return Collections.singletonList(
            new ReturnPearlRightClickAbility()
        );
    }

    @Override
    public List<Material> getLinkedCooldowns() {
        return null;
    }

    @Override
    public String getName(Power power) {
        return "Return Pearl";
    }

    @Override
    public String getDescription(Power power) {
        return null;
    }

    public static void returnPlayer(Projectile pearl) {
        if (pearl.getOrigin() == null) return;

        ProjectileSource projectileSource = pearl.getShooter();
        Player player = projectileSource instanceof Player? (Player) projectileSource : null;
        if (player == null) return;

        // check if player can be teleported
        if (!GameManager.activeGame.isAlive(player)) return;

        // teleport player
        player.teleport(pearl.getOrigin().setDirection(player.getLocation().getDirection()).subtract(0, 1.5, 0));

        // play sound for player
        player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 1);
    }
}

class ReturnPearlRightClickAbility extends Ability {
    public ReturnPearlRightClickAbility() {
        super(ClickType.RIGHT_CLICK, ClickBlockType.AIR, ShiftType.NONE);
    }

    @Override
    public boolean onUse(PlayerInteractEvent event, Power power) {
        Player player = event.getPlayer();

        // launch pearl
        Projectile pearl = player.launchProjectile(EnderPearl.class);

        // save return pearl data
        Utilities.setPersistentEntityTag(pearl, "pearl_type", "return_pearl");

        // play sound
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 0.5f, 0.5f);

        return true;
    }

    @Override
    public String getDescription(Power power) {
        return "Throw an ender pearl, then teleport back to your previous location after 5 seconds.";
    }

    @Override
    public Integer getCooldown(Power power) {
        return -1;
    }
}
package ca.modmonster.spells.events;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.item.enchantment.enchantments.RejuvenationEnchantment;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        preventLeavingWorldBorder(event);
        triggerRegenerationEnchantment(event);
        handleFallProofEntities(event);
    }

    void handleFallProofEntities(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (((Entity) player).isOnGround() && OnEntityDamage.fallProofEntities.contains(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    OnEntityDamage.fallProofEntities.remove(player);
                }
            }.runTaskLater(Spells.main, 1);
        }
    }

    void triggerRegenerationEnchantment(PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getY() == event.getTo().getY() && event.getFrom().getZ() == event.getTo().getZ()) return;
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft("rejuvenation"));

        if (EnchantmentManager.playerHasArmorEnchantment(event.getPlayer(), enchantment)) {
            RejuvenationEnchantment.onMove(event);
            event.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
        }
    }

    void preventLeavingWorldBorder(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = GameManager.activeGame;

        if (game.isAlive(player)) return;
        if (!player.getGameMode().equals(GameMode.SPECTATOR)) return;

        if (!Utilities.isLocationWithinWorldBounds(event.getTo(), game.world.map)) {
            player.sendActionBar(Component.text("You can't leave the play area!", NamedTextColor.RED));
            event.setCancelled(true);
        }
    }
}

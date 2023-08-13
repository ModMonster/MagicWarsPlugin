package ca.modmonster.spells.game.gameevents;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.LootChest;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

public class RefillChestsGameEvent extends GameEvent {
    public RefillChestsGameEvent(int timeToRun) {
        super(timeToRun);
    }

    @Override
    public String getName() {
        return "Chest Refill";
    }

    @Override
    public void runEvent(Game game) {
        // show title + play sounds
        for (Player player : game.playersInGame) {
            player.showTitle(Title.title(
                Component.empty(),
                Component.text("All chests refilled!", NamedTextColor.DARK_AQUA),
                Title.Times.of(
                    Duration.ofMillis(250),
                    Duration.ofMillis(1250),
                    Duration.ofMillis(500)
                )
            ));

            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
                }
            }.runTaskLater(Spells.main, 30);
        }

        // refill chests
        for (LootChest chest : game.map.chestLocations) {
            if (!Utilities.vectorToBlockLocation(game.world, chest.location).getBlock().getType().isAir()) {
                LootChest.spawnLootChest(game.world, chest, true);
            }
        }
    }
}

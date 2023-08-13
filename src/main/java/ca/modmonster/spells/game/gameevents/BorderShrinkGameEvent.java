package ca.modmonster.spells.game.gameevents;

import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.Duration;

public class BorderShrinkGameEvent extends GameEvent {
    public int shrinkSize;
    public int shrinkTime;

    public BorderShrinkGameEvent(int timeToRun, int shrinkSize, int shrinkTime) {
        super(timeToRun);
        this.shrinkSize = shrinkSize;
        this.shrinkTime = shrinkTime;
    }

    @Override
    public String getName() {
        return "Border Shrink";
    }

    @Override
    public void runEvent(Game game) {
        // show title + play sounds
        for (Player player : game.playersInGame) {
            player.showTitle(Title.title(
                Component.empty(),
                Component.text("The border is shrinking!", NamedTextColor.RED),
                Title.Times.of(
                    Duration.ofMillis(250),
                    Duration.ofMillis(2500),
                    Duration.ofMillis(500)
                )
            ));

            player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 200, 1);
        }

        // shrink world border
        GameManager.activeGame.world.bukkitWorld.getWorldBorder().setSize(shrinkSize, shrinkTime);
    }
}

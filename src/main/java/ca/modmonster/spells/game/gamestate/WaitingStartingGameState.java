package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WaitingStartingGameState extends GameState {
    @Override
    public String getName() {
        return "WAITING_STARTING";
    }

    @Override
    public void setState(Game game) {
        game.startingCountdown = 30;

        // play click sound
        PlaySound.click(game.playersInGame);

        // start countdown
        game.countdown = new BukkitRunnable() {
            @Override
            public void run() {
                game.startingCountdown -= 1;
                game.updateScoreboards();

                if (game.startingCountdown == 11) {
                    game.setState(new StartingGameState());
                }
            }
        };

        // give players return to lobby compass
        for (Player player : game.playersInGame) {
            player.getInventory().setItem(8, GameManager.getLobbyCompassUsableInWorld());
        }

        game.countdown.runTaskTimer(Spells.main, 20, 20);
    }

    @Override
    public void updateScoreboard(FastBoard board, Game game, Player player) {
        board.updateLines(
            Utilities.stringToComponent("&7    ⌚ " + new SimpleDateFormat("MMM d, h:mm a").format(new Date())),
            Component.empty(),
            Utilities.stringToComponent(" &6&lStatus"),
            Utilities.stringToComponent(" &eStarting in " + game.startingCountdown + "s"),
            Component.empty(),
            Utilities.stringToComponent(" &6&lPlayers"),
            Utilities.stringToComponent(" &e" + game.playersInGame.size() + " / " + game.world.map.maxPlayerCount),
            Component.empty(),
            Utilities.stringToComponent(" &6&lMap: &e" + game.world.map.name),
            Component.empty(),
            Utilities.stringToComponent("    &3mc.modmonster.ca ")
        );
    }
}

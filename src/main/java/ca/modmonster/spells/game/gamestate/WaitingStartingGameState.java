package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
    public void updateScoreboard(BetterScoreboard board, Game game, Player player) {
        board.resetLines();
        board.addStaticLine("");
        board.addStaticLine("&6&lStatus");
        board.addStaticLine("&eStarting in " + game.startingCountdown + "s");
        board.addStaticLine(" ");
        board.addStaticLine("&6&lPlayers");
        board.addStaticLine("&e" + game.playersInGame.size() + " / " + game.world.map.maxPlayerCount);
        board.addStaticLine("  ");
        board.addStaticLine("&6&lMap");
        board.addStaticLine("&e" + game.world.map.name);
        board.addStaticLine("   ");
        board.addStaticLine("    &3mc.modmonster.ca ");
    }
}

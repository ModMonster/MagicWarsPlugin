package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class StartingGameState extends GameState {
    @Override
    public String getName() {
        return "STARTING";
    }

    @Override
    public void setState(Game game) {
        game.startingCountdown = 11;

        // start countdown
        game.countdown = new BukkitRunnable() {
            @Override
            public void run() {
            game.startingCountdown -= 1;
            game.updateScoreboards();

            Map<Integer, String> titles = new HashMap<Integer, String>() {
                {
                    put(10, "&6Starting in 10 seconds");
                    put(3, "&c❸");
                    put(2, "&c❷");
                    put(1, "&c❶");
                }
            };

            Map<Integer, String> subtitles = new HashMap<Integer, String>() {
                {
                    put(3, "&eGet ready to fight!");
                    put(2, "&eGood luck!");
                    put(1, "&eHave fun!");
                }
            };

            if (game.startingCountdown == 10) {
                // show countdown title
                if (titles.get(game.startingCountdown) != null) {
                    for (Player player : game.playersInGame) {
                        player.showTitle(Title.title(
                            Utilities.stringToComponent(titles.get(game.startingCountdown)),
                            Component.empty()
                        ));

                        // remove lobby compass
                        player.getInventory().clear();
                    }
                }

                PlaySound.click(game.playersInGame);
            }

            if (game.startingCountdown <= 3 && game.startingCountdown > 0) {
                PlaySound.pling(game.playersInGame, 1);

                // show countdown title
                if (titles.get(game.startingCountdown) != null) {
                    for (Player player : game.playersInGame) {
                        player.showTitle(Title.title(
                            Utilities.stringToComponent(titles.get(game.startingCountdown)),
                            Utilities.stringToComponent(subtitles.get(game.startingCountdown)),
                            Title.Times.of(
                                Duration.ofMillis(game.startingCountdown == 3? 500 : 125),
                                Duration.ofMillis(game.startingCountdown == 3? 375 : 750),
                                Duration.ofMillis(125)
                            )
                        ));
                    }
                }
            }

            if (game.startingCountdown <= 0) {
                PlaySound.pling(game.playersInGame, 2);
                game.startingCountdown = 30;
                game.setState(new ActiveGameState());
                cancel();

                for (Player player : game.playersInGame) {
                    player.showTitle(Title.title(
                        Utilities.stringToComponent("&c&lFIGHT!"),
                        Component.empty(),
                        Title.Times.of(
                            Duration.ofMillis(125),
                            Duration.ofMillis(750),
                            Duration.ofMillis(500)
                        )
                    ));
                }
            }
            }
        };

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

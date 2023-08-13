package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.events.OnEntityDamage;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.game.gameevents.GameEvent;
import ca.modmonster.spells.util.Icons;
import ca.modmonster.spells.util.Utilities;
import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class ActiveGameState extends GameState {
    @Override
    public void setState(Game game) {
        for (Player player : game.playersInGame) {
            // add alive players and make them fall proof
            OnEntityDamage.addFallProofEntity(player);
            game.alivePlayers.add(player);

            // remove compasses (in case we skip starting state)
            player.getInventory().clear();
        }

        // break out of cages
        for (Vector vector : game.world.map.podLocations) {
            for (int x = -game.world.map.podSize.get(0); x < game.world.map.podSize.get(3) + 1; x++) {
                for (int y = -game.world.map.podSize.get(2); y < game.world.map.podSize.get(4) + 1; y++) {
                    for (int z = -game.world.map.podSize.get(3); z < game.world.map.podSize.get(5) + 1; z++) {
                        Location location = Utilities.vectorToBlockLocation(GameManager.activeGame.world.bukkitWorld, vector.clone().add(new Vector(x, y, z)));

                        location.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        for (Player player : game.playersInGame) {
            // give speed
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(200, 1));

            // set blocked slots + mirrored armor slots
            for (int i = 9; i < 35; i++) {
                player.getInventory().setItem(i, Game.blockerStack);
            }

            // set trash slot
            player.getInventory().setItem(35, Game.trashStack);
        }

        // start event timer
        game.eventTimerRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                game.time += 1;
                game.updateScoreboards();

                // see if any events should be run
                for (GameEvent event : GameManager.events) {
                    if (!(event.timeToRun == game.time)) continue;

                    event.runEvent(game);
                    game.nextEventIndex += 1;
                }
            }
        };
        game.eventTimerRunnable.runTaskTimer(Spells.main, 20, 20);
    }

    String timeUntilEvent(Game game, GameEvent event) {
        int seconds = event.timeToRun - game.time;

        if (seconds >= 60) {
            int minutes = (int) Math.floor(seconds / 60f);
            int remainingSeconds = seconds - minutes * 60;

            return minutes + "m " + remainingSeconds + "s";
        }

        return seconds + "s";
    }

    @Override
    public void updateScoreboard(BetterScoreboard board, Game game, Player player) {
        board.resetLines();

        if (game.nextEventIndex < GameManager.events.size()) {
            GameEvent event = GameManager.events.get(game.nextEventIndex);

            board.addStaticLine("");
            board.addStaticLine("&6&lNext Events");
            board.addStaticLine("&e" + event.getName() + " &8" + Icons.DOT + "&e " + timeUntilEvent(game, event));
        }

        if (game.nextEventIndex < GameManager.events.size() - 1) {
            GameEvent event = GameManager.events.get(game.nextEventIndex + 1);

            board.addStaticLine("&e" + event.getName() + " &8" + Icons.DOT + "&e " + timeUntilEvent(game, event));
        }

        board.addStaticLine(" ");
        board.addStaticLine("&6&lAlive");
        board.addStaticLine("&e" + game.alivePlayers.size() + " / " + game.world.map.maxPlayerCount);
        board.addStaticLine("  ");
        board.addStaticLine("&6&lKills");
        board.addStaticLine("&e" + game.kills.getOrDefault(player, 0));
        board.addStaticLine("   ");
        board.addStaticLine("&6&lMap");
        board.addStaticLine("&e" + game.world.map.name);
        board.addStaticLine("    ");
        board.addStaticLine("    &3mc.modmonster.ca ");
    }
}

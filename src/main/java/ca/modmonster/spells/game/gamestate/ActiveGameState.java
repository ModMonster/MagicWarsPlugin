package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.events.OnEntityDamage;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.game.gameevents.BorderShrinkGameEvent;
import ca.modmonster.spells.game.gameevents.GameEvent;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public class ActiveGameState extends GameState {
    @Override
    public String getName() {
        return "ACTIVE";
    }

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

                GameEvent nextEvent = GameManager.events.get(game.nextEventIndex);

                // see if any events should be run
                if (nextEvent.timeToRun == game.time) {
                    nextEvent.runEvent(game);
                    game.nextEventIndex += 1;

                    if (game.nextEventIndex >= GameManager.events.size()) {
                        cancel();
                        game.updateScoreboards();
                    }
                }

                // storm warning
                if (nextEvent instanceof BorderShrinkGameEvent && nextEvent.timeToRun == game.time + 10) {
                    for (Player player : game.playersInGame) {
                        player.sendActionBar(Utilities.stringToComponent("&e&l[!] &cThe border will start to shrink in 10 seconds!"));
                        PlaySound.storm(player);
                    }
                }
            }
        };
        game.eventTimerRunnable.runTaskTimer(Spells.main, 20, 20);
    }

    String timeUntilEvent(Game game, GameEvent event) {
        int seconds = event.timeToRun - game.time;

        if (seconds >= 60) {
            int minutes = Math.round(seconds / 60f);

            return minutes + "m";
        }

        return seconds + "s";
    }

    @Override
    public void updateScoreboard(FastBoard board, Game game, Player player) {
        List<Component> lines = new ArrayList<>();

        if (game.nextEventIndex < GameManager.events.size()) {
            GameEvent event = GameManager.events.get(game.nextEventIndex);

            lines.add(Component.empty());
            lines.add(Utilities.stringToComponent(" &6&lNext Event"));
            lines.add(Utilities.stringToComponent(" &e" + event.getName() + " &8- &e" + timeUntilEvent(game, event)));
        }

        lines.add(Component.empty());
        lines.add(Utilities.stringToComponent(" &6&lAlive"));
        lines.add(Utilities.stringToComponent(" &e" + game.alivePlayers.size() + " / " + game.world.map.maxPlayerCount));
        lines.add(Component.empty());
        lines.add(Utilities.stringToComponent(" &6&lKills: &e" + game.kills.getOrDefault(player, 0)));
        lines.add(Utilities.stringToComponent(" &6&lMap: &e" + game.world.map.name));
        lines.add(Component.empty());
        lines.add(Utilities.stringToComponent("    &3mc.modmonster.ca "));

        board.updateLines(lines);
    }
}

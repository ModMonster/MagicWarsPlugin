package ca.modmonster.spells.util.betterscoreboard;

import java.util.HashMap;
import java.util.Map;

import net.kyori.adventure.text.TextComponent;

public class ScoreboardManager {
    private static final Map<String, BetterScoreboard> betterScoreboards = new HashMap<>();

    public ScoreboardManager() {
    }

    public static BetterScoreboard getBetterScoreboard(String name) {
        return betterScoreboards.getOrDefault(name, null);
    }

    public static BetterScoreboard createNewBetterScoreboard(String name, TextComponent title) {
        BetterScoreboard scoreboard = new BetterScoreboard(name, title);
        betterScoreboards.put(name, scoreboard);
        return scoreboard;
    }

    public static boolean deleteBetterScoreboard(String name) {
        if (betterScoreboards.containsKey(name)) {
            betterScoreboards.remove(name)._unregister();
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteBetterScoreboard(BetterScoreboard scoreboard) {
        if (betterScoreboards.containsKey(scoreboard.getName())) {
            betterScoreboards.remove(scoreboard.getName())._unregister();
            return true;
        } else {
            return false;
        }
    }
}
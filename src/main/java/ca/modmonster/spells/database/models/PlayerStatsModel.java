package ca.modmonster.spells.database.models;

import java.time.Duration;
import java.util.UUID;

public class PlayerStatsModel {
    private UUID player;
    private int kills;
    private int wins;
    private int deaths;
    private Duration timePlayed;

    public PlayerStatsModel(UUID player, int kills, int wins, int deaths, Duration timePlayed) {
        this.player = player;
        this.kills = kills;
        this.wins = wins;
        this.deaths = deaths;
        this.timePlayed = timePlayed;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public Duration getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(Duration timePlayed) {
        this.timePlayed = timePlayed;
    }
}

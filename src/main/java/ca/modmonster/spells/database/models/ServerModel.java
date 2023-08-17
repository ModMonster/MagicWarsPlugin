package ca.modmonster.spells.database.models;

public class ServerModel {
    private int id;
    private String state;
    private String map;
    private int playerCount;
    private int maxPlayers;
    private int aliveCount;
    private int timeRemaining;

    public ServerModel(int id, String state, String map, int playerCount, int maxPlayers, int aliveCount, int timeRemaining) {
        this.id = id;
        this.state = state;
        this.map = map;
        this.playerCount = playerCount;
        this.maxPlayers = maxPlayers;
        this.aliveCount = aliveCount;
        this.timeRemaining = timeRemaining;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = ServerModel.this.maxPlayers;
    }

    public int getAliveCount() {
        return aliveCount;
    }

    public void setAliveCount(int aliveCount) {
        this.aliveCount = aliveCount;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
}

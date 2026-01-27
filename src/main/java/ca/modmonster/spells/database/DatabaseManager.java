package ca.modmonster.spells.database;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.database.models.ServerModel;
import ca.modmonster.spells.database.models.PlayerStatsModel;
import ca.modmonster.spells.game.GameManager;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.Duration;
import java.util.UUID;

public class DatabaseManager {
    private Connection connection;

    public Connection getConnection() throws SQLException {
        if (connection != null) return connection;

        String dbUrl = Spells.main.getConfig().getString("db-url");
        String dbDatabase = Spells.main.getConfig().getString("db-database");
        String dbUser = Spells.main.getConfig().getString("db-user");
        String dbPassword = Spells.main.getConfig().getString("db-password");

        String url = "jdbc:mysql://" + dbUrl + "/" + dbDatabase;
        connection = DriverManager.getConnection(url, dbUser, dbPassword);

        return connection;
    }

    public void initDatabase() throws SQLException {
        runStatement("CREATE TABLE IF NOT EXISTS magic_wars(id int primary key, state varchar(16), map varchar(36), players int, max_players int, alive int, time_remaining int)");
        runStatement("CREATE TABLE IF NOT EXISTS stats.magic_wars(player varchar(36) primary key, kills int, wins int, deaths int, time_played bigint)");
    }

    public void runStatement(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(sql);
        statement.close();
    }

    public ServerModel getServerModel() {
        return new ServerModel(
            Spells.main.getConfig().getInt("id"),
            GameManager.activeGame.state.getName(),
            GameManager.activeGame.world.map.name,
            GameManager.activeGame.playersInGame.size(),
            GameManager.activeGame.world.map.maxPlayerCount,
            GameManager.activeGame.alivePlayers.size(),
            0
        );
    }

    public void initializePlayerStats(PlayerStatsModel model) throws SQLException {
        PreparedStatement statement = getConnection()
            .prepareStatement("INSERT INTO stats.magic_wars(player, kills, wins, deaths, time_played) VALUES(?, ?, ?, ?, ?)");

        statement.setString(1, model.getPlayer().toString());
        statement.setInt(2, model.getKills());
        statement.setInt(3, model.getWins());
        statement.setInt(4, model.getDeaths());
        statement.setLong(5, model.getTimePlayed().getSeconds());

        statement.executeUpdate();
        statement.close();
    }

    public PlayerStatsModel getPlayerStats(Player player) throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "SELECT * FROM stats.magic_wars WHERE player = " + player.getUniqueId();
        ResultSet results = statement.executeQuery(sql);

        if (results.next()) {
            PlayerStatsModel stats = new PlayerStatsModel(
                UUID.fromString(results.getString("player")),
                results.getInt("kills"),
                results.getInt("wins"),
                results.getInt("deaths"),
                Duration.ofSeconds(results.getInt("time_played"))
            );

            statement.close();
            return stats;
        }

        statement.close();
        return null;
    }

    public void updatePlayerStats(PlayerStatsModel model) throws SQLException {
        PreparedStatement statement = getConnection()
            .prepareStatement("UPDATE stats.magic_wars SET kills = ?, wins = ?, deaths = ?, time_played = ? WHERE player = ?");

        statement.setInt(1, model.getKills());
        statement.setInt(2, model.getWins());
        statement.setInt(3, model.getDeaths());
        statement.setLong(4, model.getTimePlayed().getSeconds());
        statement.setString(5, model.getPlayer().toString());

        statement.executeUpdate();
        statement.close();
    }

    public void setServerInDatabase() {
        try {
            ServerModel server = getServerInfoByID(Spells.main.getConfig().getInt("id"));

            if (server == null) {
                // new server that needs to be registered to the database
                sendServerModelToDatabase(getServerModel());
            } else {
                updateServerTable(getServerModel());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateServerTable(ServerModel server) throws SQLException {
        PreparedStatement statement = getConnection()
            .prepareStatement("UPDATE magic_wars SET state = ?, map = ?, players = ?, max_players = ?, alive = ?, time_remaining = ? WHERE id = ?");

        statement.setString(1, server.getState());
        statement.setString(2, server.getMap());
        statement.setInt(3, server.getPlayerCount());
        statement.setInt(4, server.getMaxPlayers());
        statement.setInt(5, server.getAliveCount());
        statement.setInt(6, server.getTimeRemaining());
        statement.setInt(7, server.getId());

        statement.executeUpdate();
        statement.close();
    }

    private void sendServerModelToDatabase(ServerModel server) throws SQLException {
        PreparedStatement statement = getConnection()
            .prepareStatement("INSERT INTO magic_wars(id, state, map, players, max_players, alive, time_remaining) VALUES(?, ?, ?, ?, ?, ?, ?)");

        statement.setInt(1, server.getId());
        statement.setString(2, server.getState());
        statement.setString(3, server.getMap());
        statement.setInt(4, server.getPlayerCount());
        statement.setInt(5, server.getMaxPlayers());
        statement.setInt(6, server.getAliveCount());
        statement.setInt(7, server.getTimeRemaining());

        statement.executeUpdate();
        statement.close();
    }

    public ServerModel getServerInfoByID(int id) throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "SELECT * FROM magic_wars WHERE id = " + id;
        ResultSet results = statement.executeQuery(sql);

        if (results.next()) {
            ServerModel server = new ServerModel(
                results.getInt("id"),
                results.getString("state"),
                results.getString("map"),
                results.getInt("players"),
                results.getInt("max_players"),
                results.getInt("alive"),
                results.getInt("time_remaining")
            );

            statement.close();
            return server;
        }

        statement.close();
        return null;
    }
}

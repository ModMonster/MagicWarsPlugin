package ca.modmonster.spells.database;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.database.models.ServerModel;
import ca.modmonster.spells.game.GameManager;

import java.sql.*;

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
        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS magic_wars(id int primary key, state varchar(16), map varchar(36), players int, max_players int, alive int, time_remaining int)";
        statement.execute(sql);
        statement.close();
    }

    public ServerModel getServerModel() {
        return new ServerModel(
            Spells.main.getConfig().getInt("id"),
            GameManager.activeGame.state.getName(),
            GameManager.activeGame.world.map.name,
            Spells.main.getServer().getOnlinePlayers().size(),
            GameManager.activeGame.world.map.maxPlayerCount,
            GameManager.activeGame.alivePlayers.size(),
            0
        );
    }

    public void setServerInDatabase() {
        try {
            ServerModel server = getServerInfoByID(Spells.main.getConfig().getInt("id"));

            if (server == null) {
                // new server that needs to be registered to the database
                sendModelToDatabase(getServerModel());
            } else {
                updateDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDatabase() {
        try {
            PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE magic_wars SET state = ?, map = ?, players = ?, max_players = ?, alive = ?, time_remaining = ? WHERE id = ?");

            ServerModel server = getServerModel();

            statement.setString(1, server.getState());
            statement.setString(2, server.getMap());
            statement.setInt(3, server.getPlayerCount());
            statement.setInt(4, server.getMaxPlayers());
            statement.setInt(5, server.getAliveCount());
            statement.setInt(6, server.getTimeRemaining());
            statement.setInt(7, server.getId());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            Spells.main.getLogger().severe("Failed to update database!");
            e.printStackTrace();
        }
    }

    public void sendModelToDatabase(ServerModel server) throws SQLException {
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

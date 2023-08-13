package ca.modmonster.spells.events;

import ca.modmonster.spells.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinGame implements Listener {
    @EventHandler
    public void onJoinGame(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        joinLobby(player);

        // remove join message
        event.joinMessage(null);
    }

    public static void joinLobby(Player player) {
        GameManager.activeGame.join(player);
    }
}

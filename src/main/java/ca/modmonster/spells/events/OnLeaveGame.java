package ca.modmonster.spells.events;

import ca.modmonster.spells.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnLeaveGame implements Listener {
    @EventHandler
    public void onLeaveGame(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // leave current game
        GameManager.activeGame.leave(player);

        // remove quit message
        event.quitMessage(null);
    }
}

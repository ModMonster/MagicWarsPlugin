package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
import org.bukkit.entity.Player;

public abstract class GameState {
    public abstract String getName();
    public abstract void setState(Game game);
    public abstract void updateScoreboard(BetterScoreboard board, Game game, Player player);
}

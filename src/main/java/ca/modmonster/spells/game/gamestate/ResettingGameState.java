package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
import org.bukkit.entity.Player;

public class ResettingGameState extends GameState {
    @Override
    public String getName() {
        return "RESETTING";
    }

    @Override
    public void setState(Game game) {

    }

    @Override
    public void updateScoreboard(BetterScoreboard board, Game game, Player player) {

    }
}

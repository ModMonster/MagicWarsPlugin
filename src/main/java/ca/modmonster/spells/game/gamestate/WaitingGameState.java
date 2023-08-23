package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
import org.bukkit.entity.Player;

public class WaitingGameState extends GameState {
    @Override
    public String getName() {
        return "WAITING";
    }

    @Override
    public void setState(Game game) {

    }

    @Override
    public void updateScoreboard(BetterScoreboard board, Game game, Player player) {
        board.resetLines();
        board.addStaticLine("");
        board.addStaticLine(" &6&lStatus");
        board.addStaticLine(" &eWaiting for players");
        board.addStaticLine(" ");
        board.addStaticLine(" &6&lPlayers");
        board.addStaticLine(" &e" + game.playersInGame.size() + " / " + game.world.map.maxPlayerCount);
        board.addStaticLine("  ");
        board.addStaticLine(" &6&lMap: &e" + game.world.map.name);
        board.addStaticLine("   ");
        board.addStaticLine("    &3mc.modmonster.ca    ");
    }
}

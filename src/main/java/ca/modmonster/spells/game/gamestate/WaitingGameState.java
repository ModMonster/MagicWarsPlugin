package ca.modmonster.spells.game.gamestate;

import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.util.Utilities;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
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
    public void updateScoreboard(FastBoard board, Game game, Player player) {
        board.updateLines(
            Component.empty(),
            Utilities.stringToComponent(" &6&lStatus"),
            Utilities.stringToComponent(" &eWaiting for players"),
            Component.empty(),
            Utilities.stringToComponent(" &6&lPlayers"),
            Utilities.stringToComponent(" &e" + game.playersInGame.size() + " / " + game.world.map.maxPlayerCount),
            Component.empty(),
            Utilities.stringToComponent(" &6&lMap: &e" + game.world.map.name),
            Component.empty(),
            Utilities.stringToComponent("    &3mc.modmonster.ca ")
        );
    }
}

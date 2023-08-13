package ca.modmonster.spells.game.gameevents;

import ca.modmonster.spells.game.Game;

public abstract class GameEvent {
    public int timeToRun;

    public GameEvent(int timeToRun) {
        this.timeToRun = timeToRun;
    }

    public abstract String getName();
    public abstract void runEvent(Game game);
}

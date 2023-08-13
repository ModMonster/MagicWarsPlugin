package ca.modmonster.spells.command.magicwars.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResetGameCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Game chosenGame = GameManager.activeGame;

        // join game
        chosenGame.fullReset();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

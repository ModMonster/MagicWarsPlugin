package ca.modmonster.spells.command.magicwars.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.game.gamestate.*;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SetGameStateCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GameState state;

        switch (args[1].toUpperCase(Locale.ROOT)) {
            case "WAITING":
                state = new WaitingGameState();
                break;
            case "WAITING_STARTING":
                state = new WaitingStartingGameState();
                break;
            case "STARTING":
                state = new StartingGameState();
                break;
            case "ACTIVE":
                state = new ActiveGameState();
                break;
            default:
                state = null;
        }

        if (state == null) {
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eInvalid state."));
            return;
        }

        // set state
        GameManager.activeGame.setState(state);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> possibilities = new ArrayList<>();

        // states
        if (args.length == 2) {
            possibilities = Arrays.asList(
                "WAITING",
                "WAITING_STARTING",
                "STARTING",
                "ACTIVE"
            );
        }

        return possibilities;
    }
}

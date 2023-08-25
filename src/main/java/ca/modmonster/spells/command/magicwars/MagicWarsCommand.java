package ca.modmonster.spells.command.magicwars;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.command.magicwars.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicWarsCommand implements CommandExecutor, TabCompleter {
    final static Map<String, Subcommand> subcommands = new HashMap<String, Subcommand>() {
        {
            put("join", new JoinGameCommand());
            put("leave", new LeaveGameCommand());
            put("reset", new ResetGameCommand());
            put("setstate", new SetGameStateCommand());
            put("buildperms", new BuildPermsCommand());
            put("edit", new EditCommand());
            put("solowin", new SoloWinCommand());
        }
    };

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // run subcommand
        if (subcommands.containsKey(args[0])) {
            subcommands.get(args[0]).onCommand(sender, command, label, args);
        } else {
            // invalid subcommand
            sender.sendMessage("Oops, '" + args[0] + "' is not a valid command!");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> possibilities = new ArrayList<>();

        if (args.length == 1) {
            possibilities.addAll(subcommands.keySet());

            return possibilities;
        }

        else if (args.length > 1) {
            if (subcommands.containsKey(args[0])) {
                possibilities = subcommands.get(args[0]).onTabComplete(sender, command, label, args);
            }
        }

        return possibilities;
    }
}

package ca.modmonster.spells.command.spells;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.command.spells.commands.BrowseCommand;
import ca.modmonster.spells.command.spells.commands.GiveCommand;
import ca.modmonster.spells.command.spells.commands.SpawnLootCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellsCommand implements CommandExecutor, TabCompleter {
    final static Map<String, Subcommand> subcommands = new HashMap<String, Subcommand>() {
        {
            put("give", new GiveCommand());
            put("spawnloot", new SpawnLootCommand());
            put("browse", new BrowseCommand());
        }
    };

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) || args.length < 1) {
            return true;
        }

        Player player = (Player) sender;

        // run subcommand
        if (subcommands.containsKey(args[0])) {
            subcommands.get(args[0]).onCommand(sender, command, label, args);
        } else {
            // invalid subcommand
            player.sendMessage("Oops, '" + args[0] + "' is not a valid command!");
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

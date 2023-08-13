package ca.modmonster.spells.command.magicwars.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BuildPermsCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[ERROR]: Only players can use this command");
            return;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            if (GameManager.allowedBuildingPlayers.contains(player)) {
                player.sendMessage(Utilities.stringToComponent("&cBlock breaking disabled!"));
                GameManager.allowedBuildingPlayers.remove(player);
            } else {
                player.sendMessage(Utilities.stringToComponent("&aBlock breaking enabled!"));
                GameManager.allowedBuildingPlayers.add(player);
            }
            return;
        }

        if (args[1].equals("enable")) {
            player.sendMessage(Utilities.stringToComponent("&aBlock breaking enabled!"));
            GameManager.allowedBuildingPlayers.add(player);
        } else if (args[1].equals("disable")) {
            player.sendMessage(Utilities.stringToComponent("&cBlock breaking disabled!"));
            GameManager.allowedBuildingPlayers.remove(player);
        } else {
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &6Incorrect Syntax!")); // prints incorrect syntax
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &e/mw buildperms [enable|disable]")); // prints the correct syntax
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> possibilities = new ArrayList<>();

        possibilities.add("enable");
        possibilities.add("disable");

        return possibilities;
    }
}

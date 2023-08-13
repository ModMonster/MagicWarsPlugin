package ca.modmonster.spells.command.spells.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.game.LootChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpawnLootCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // set chest at player's location
        if (args.length == 1) {
            LootChest.spawnLootChest(((Player) sender).getWorld(), new LootChest(((Player) sender).getTargetBlock(4).getLocation().toVector(), true), true);
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return new ArrayList<>();
    }
}

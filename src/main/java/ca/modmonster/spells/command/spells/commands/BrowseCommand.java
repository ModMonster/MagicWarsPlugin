package ca.modmonster.spells.command.spells.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.gui.BrowseSpellsGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrowseCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        new BrowseSpellsGui().openInventory((HumanEntity) sender);

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

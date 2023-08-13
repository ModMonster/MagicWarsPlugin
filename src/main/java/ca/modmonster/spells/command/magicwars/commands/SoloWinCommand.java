package ca.modmonster.spells.command.magicwars.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.game.Game;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoloWinCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[ERROR]: Only players can use this command");
            return;
        }

        Player player = (Player) sender;

        GameManager.activeGame.win(player);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

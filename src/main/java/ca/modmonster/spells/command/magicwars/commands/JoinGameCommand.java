package ca.modmonster.spells.command.magicwars.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JoinGameCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eOnly players can use this command"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eYou must specify a game to join!"));
            return;
        }

        Player player = (Player) sender;

        // join game
        String result = GameManager.activeGame.join(player);

        if (result != null) {
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &e" + result));
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

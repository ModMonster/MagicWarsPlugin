package ca.modmonster.spells.command.spells.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.item.spell.Power;
import ca.modmonster.spells.item.spell.Spell;
import ca.modmonster.spells.item.spell.SpellManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[ERROR]: Only players can use this command");
            return;
        }

        if (args.length < 2) {
            player.sendMessage("[ERROR]: Incorrect Syntax!"); // prints incorrect syntax
            player.sendMessage("[ERROR]: /spell give <item> [amount] [power]"); // prints the correct syntax
            return;
        }

        // loop through
        for (Spell spell : SpellManager.spells) {
            if (args[1].equals(spell.getId())) {
                Power power;

                if (args.length >= 4) {
                    // get power from command
                    power = Power.get(args[3]);
                } else {
                    // randomize power
                    power = Power.get((int) (Math.random() * 3) + 1);
                }

                if (power == null) {
                    player.sendMessage("[ERROR]: No power level called '" + args[2] + "'.");
                    return;
                }

                player.getInventory().addItem(SpellManager.getSpell(spell, power, args.length >= 3? Integer.parseInt(args[2]) : 1));
                return;
            }
        }

        // incorrect item
        player.sendMessage("[ERROR]: No spell called '" + args[1] + "'.");

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> possibilities = new ArrayList<>();

        // items
        if (args.length == 2) {
            for (Spell spell : SpellManager.spells) {
                possibilities.add(spell.getId());
            }
        }

        // amount
        else if (args.length == 3) {
            possibilities.add("amount");
        }

        // power levels
        else if (args.length == 4) {
            possibilities.add("weak");
            possibilities.add("strong");
            possibilities.add("powerful");
        }

        return possibilities;
    }
}

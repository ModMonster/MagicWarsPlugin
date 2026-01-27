package ca.modmonster.spells.command.enchantments.commands;

import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.item.enchantment.CustomEnchantment;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
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
            player.sendMessage("[ERROR]: /mwen give <enchantment> [level]"); // prints the correct syntax
            return;
        }

        // loop through
        for (CustomEnchantment enchant : EnchantmentManager.enchantments) {
            if (args[1].equals(enchant.getId())) {
                int level;

                if (args.length >= 3) {
                    // get power from command
                    level = Integer.parseInt(args[2]);
                } else {
                    // randomize power
                    level = 1;
                }

                if (level > enchant.getMaxLevel()) {
                    sender.sendMessage("[ERROR]: Maximum level for enchantment '" + enchant.getId() + "' is " + enchant.getMaxLevel());
                    return;
                }

                player.getInventory().addItem(EnchantmentManager.getEnchantedBook(enchant, level));
                return;
            }
        }

        // incorrect item
        player.sendMessage("[ERROR]: No enchantment called '" + args[1] + "'.");

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> possibilities = new ArrayList<>();

        // enchantments
        if (args.length == 2) {
            for (CustomEnchantment enchantment : EnchantmentManager.enchantments) {
                possibilities.add(enchantment.getId());
            }
        }

        // power levels
        else if (args.length == 3) {
            for (CustomEnchantment enchant : EnchantmentManager.enchantments) {
                if (args[1].equals(enchant.getId())) {
                    for (int lvl = 1; lvl < enchant.getMaxLevel() + 1; lvl++) {
                        possibilities.add(String.valueOf(lvl));
                    }
                    break;
                }
            }
        }

        return possibilities;
    }
}

package ca.modmonster.spells.util.animations;

import ca.modmonster.spells.util.AnimationHelper;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.TextComponent;

import java.util.Arrays;
import java.util.List;

public class MagicGamesScoreboardTitleAnimation extends AnimationHelper {
    @Override
    protected List<TextComponent> getLines() {
        return Arrays.asList(
            Utilities.stringToComponent("&3☆&b&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&b&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&b&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&b&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&b&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&b&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&b☆&l MAGIC WARS &b☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC WARS &f☆&r"),

            Utilities.stringToComponent("&f☆&3&l M&b&lAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&b☆&3&l MA&b&lGIC WARS &b☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAG&b&lIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGI&b&lC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGIC &b&lWARS &3☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGIC W&b&lARS &3☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGIC WA&b&lRS &3☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGIC WAR&b&lS &3☆&r"),

            Utilities.stringToComponent("&b☆&3&l MAGIC WARS &b☆&r"),
            Utilities.stringToComponent("&f☆&3&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&3&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&3&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&3&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&3&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&3&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&b☆&3&l MAGIC WARS &b☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&3&l MAGIC WARS &3☆&r"),

            Utilities.stringToComponent("&3☆&b&l M&3&lAGIC WARS &3☆&r"),
            Utilities.stringToComponent("&3☆&b&l MA&3&lGIC WARS &3☆&r"),
            Utilities.stringToComponent("&b☆&l MAG&3&lIC WARS &b☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGI&3&lC WARS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC &3&lWARS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC W&3&lARS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC WA&3&lRS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC WAR&3&lS &f☆&r"),
            Utilities.stringToComponent("&f☆&b&l MAGIC WARS &f☆&r"),
            Utilities.stringToComponent("&b☆&l MAGIC WARS &b☆&r")
        );
    }
}

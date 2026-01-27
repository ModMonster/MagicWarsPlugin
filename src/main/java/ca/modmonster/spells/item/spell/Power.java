package ca.modmonster.spells.item.spell;

import ca.modmonster.spells.util.Icons;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.Locale;

public enum Power {
    WEAK("Weak", 1, NamedTextColor.RED),
    STRONG("Strong", 2, NamedTextColor.GOLD),
    POWERFUL("Powerful", 3, NamedTextColor.GREEN);

    public final String name;
    public final Integer number;
    public final TextColor color;

    Power(String name, Integer number, TextColor color) {
        this.name = name;
        this.number = number;
        this.color = color;
    }

    public static Power get(Integer number) {
        return switch (number) {
            case 1 -> Power.WEAK;
            case 2 -> Power.STRONG;
            case 3 -> Power.POWERFUL;
            default -> null;
        };

    }

    public static Power get(String level) {
        return switch (level.toUpperCase(Locale.ROOT)) {
            case "WEAK", "1" -> Power.WEAK;
            case "STRONG", "2" -> Power.STRONG;
            case "POWERFUL", "3" -> Power.POWERFUL;
            default -> null;
        };

    }

    public static String getLevelMeter(Power power) {
        return switch (power) {
            case WEAK -> Icons.STAR + Icons.STAR_OUTLINE + Icons.STAR_OUTLINE; // ★☆☆
            case STRONG -> Icons.STAR + Icons.STAR + Icons.STAR_OUTLINE; // ★★☆
            case POWERFUL -> Icons.STAR + Icons.STAR + Icons.STAR; // ★★★
        };

    }
}
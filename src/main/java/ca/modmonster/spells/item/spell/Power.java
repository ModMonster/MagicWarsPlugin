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
        switch (number) {
            case 1:
                return Power.WEAK;
            case 2:
                return Power.STRONG;
            case 3:
                return Power.POWERFUL;
        }

        return null;
    }

    public static Power get(String level) {
        switch (level.toUpperCase(Locale.ROOT)) {
            case "WEAK":
            case "1":
                return Power.WEAK;
            case "STRONG":
            case "2":
                return Power.STRONG;
            case "POWERFUL":
            case "3":
                return Power.POWERFUL;
        }

        return null;
    }

    public static String getLevelMeter(Power power) {
        switch (power) {
            case WEAK:
                return Icons.STAR + Icons.STAR_OUTLINE + Icons.STAR_OUTLINE; // ★☆☆
            case STRONG:
                return Icons.STAR + Icons.STAR + Icons.STAR_OUTLINE; // ★★☆
            case POWERFUL:
                return Icons.STAR + Icons.STAR + Icons.STAR; // ★★★
        }

        return null;
    }
}
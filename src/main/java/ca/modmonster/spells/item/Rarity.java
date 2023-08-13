package ca.modmonster.spells.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Rarity {
    COMMON("Common", NamedTextColor.WHITE, 16, 8),
    UNCOMMON("Uncommon", NamedTextColor.GREEN, 8, 6),
    RARE("Rare", NamedTextColor.AQUA, 4, 4),
    MYTHIC("Mythic", NamedTextColor.LIGHT_PURPLE, 2, 2),
    LEGENDARY("Legendary", NamedTextColor.GOLD, 1, 1),
    UNFINISHED("Unfinished", NamedTextColor.RED, 0, 1);

    public final TextColor color;
    public final String name;
    public final Integer rarityWeight;
    public final Integer stackableMaxStackSize;

    Rarity(String name, TextColor color, Integer rarityWeight, Integer stackableMaxStackSize) {
        this.name = name;
        this.color = color;
        this.rarityWeight = rarityWeight;
        this.stackableMaxStackSize = stackableMaxStackSize;
    }

    /**
     * Create a bold TextComponent with the name and color of the rarity.
     * @return generated TextComponent
     */
    public @NotNull TextComponent getDecoratedComponent() {
        return Component.text(name.toUpperCase(Locale.ROOT), color, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
    }
}
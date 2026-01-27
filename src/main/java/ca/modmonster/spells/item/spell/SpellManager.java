package ca.modmonster.spells.item.spell;

import ca.modmonster.spells.item.spell.spells.*;
import ca.modmonster.spells.item.spell.spells.minion.SkeletonMinion;
import ca.modmonster.spells.item.spell.spells.minion.WitherSkeletonMinion;
import ca.modmonster.spells.item.spell.spells.minion.ZombieMinion;
import ca.modmonster.spells.util.Icons;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SpellManager {
    public static final List<Spell> spells = Arrays.asList(
        new SpearOfLight(),
        new PixieDust(),
        new StormStrike(),
        new BlastJump(),
        new ZombieMinion(),
        new WitherSkeletonMinion(),
        new SkeletonMinion(),
        new FireballSpell(),
        new AppleOfRegeneration(),
        new Shockwave(),
        new ReturnPearl(),
        new WitherBall(),
        new StaffOfTeleportation(),
        new Backstabber(),
        new ArrowStrike(),
        new TNTStrike(),
        new Quickpaw(),
        new Blink(),
        new Webslinger(),
        new FangsOfFury(),
        new DisarmSpell(),
        new MagicMagnetSpell(),
        new FluffyCloudSpell(),
        new SpearOfFire(),
        new Slowball()
    );

    public static Spell getSpellFromItem(ItemStack item) {
        String spellType = Utilities.getPersistentItemTagString(item, "spell_type");

        for (Spell spell : spells) {
            if (spell.getId().equals(spellType)) {
                return spell;
            }
        }

        return null;
    }

    public static boolean isSpell(ItemStack item) {
        return Utilities.getPersistentItemTagString(item, "spell_type") != null;
    }

    /**
     * Creates a new ItemStack with a new power level from another ItemStack representing a spell.
     * @param spellStack stack representing spell
     * @param power power of new ItemStack
     * @return original ItemStack with specified power
     */
    public static @NotNull ItemStack asSpellWithPower(@NotNull ItemStack spellStack, @NotNull Power power) {
        Spell spell = getSpellFromItem(spellStack);
        return new ItemStack(getSpell(spell, power));
    }

    public static ItemStack getSpell(Spell spell, Power power) {
        return getSpellInternal(spell, power, 1);
    }

    public static ItemStack getSpell(Spell spell, Power power, Integer count) {
        return getSpellInternal(spell, power, count);
    }

    private static ItemStack getSpellInternal(Spell spell, Power power, Integer count) {
        // make itemstack with spell material
        ItemStack itemStack = new ItemStack(spell.getMaterial(), count);

        // set item type
        Utilities.setPersistentItemTag(itemStack, "spell_type", spell.getId());

        // set power
        if (spell.getHasPower()) {
            Utilities.setPersistentItemTag(itemStack, "power", power.number);
        } else {
            Utilities.setPersistentItemTag(itemStack, "power", 1);
        }

        // add uuid to make unstackable
        if (!spell.getStackable()) {
            Random random = ThreadLocalRandom.current();
            byte[] randomBytes = new byte[32];
            random.nextBytes(randomBytes);
            String encoded = Base64.getEncoder().encodeToString(randomBytes);

            Utilities.setPersistentItemTag(itemStack, "uuid", encoded);
        }

        // set glowing
        Utilities.setGlowing(itemStack, spell.getGlow());

        // get item metadata
        ItemMeta meta = itemStack.getItemMeta();

        // set display name & color
        if (spell.getHasPower()) {
            meta.displayName(
                Component.text(power.name + " ", spell.getRarity().color).
                decoration(TextDecoration.ITALIC, false).
                append(Component.text(spell.getName(power)))
            );
        } else {
            meta.displayName(Component.text(spell.getName(power), spell.getRarity().color).decoration(TextDecoration.ITALIC, false));
        }

        // set lore
        List<Component> lore = new ArrayList<>();

        // set description
        if (spell.getDescription(power) != null) {
            for (String line : Utilities.addNewlinesToStringForLore(spell.getDescription(power))) {
                lore.add(Component.text(line, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            }
        }

        lore.add(Component.space());

        // set power
        if (spell.getHasPower()) {
            lore.add(
                    Utilities.stringToComponentWithoutItalic("&8"+ Icons.POWER + " &7Power: ").
                    append(Component.text(Power.getLevelMeter(power) + " ", power.color)).
                    append(Component.text("(" + power.name.toUpperCase(Locale.ROOT) + ")", power.color, TextDecoration.BOLD))
            );

            lore.add(Component.space());
        }

        // show abilities
        for (Ability ability : spell.getAbilities()) {
            String triggerButton = ((ability.shiftType.equals(ShiftType.SHIFT)? "(SHIFT + " : "(") + ability.clickType.name.toUpperCase(Locale.ROOT) + ")");

            // name and trigger button
            lore.add(Utilities.stringToComponentWithoutItalic("&8" + Icons.RIGHT_ARROW + " &9Ability: &b&l" + triggerButton));

            // description
            if (ability.getDescription(power) != null) {
                for (String line : Utilities.addNewlinesToStringForLore(ability.getDescription(power))) {
                    lore.add(Component.text(line, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
                }
            }

            // cooldown
            if (ability.getCooldown(power) != null && ability.getCooldown(power) != -1) {
                lore.add(Component.text(Utilities.ticksToSeconds(ability.getCooldown(power)) + " second cooldown", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            }

            lore.add(Component.space());
        }

        // add consume on use lore
        if (spell.getConsumeOnUse()) {
            lore.add(Utilities.stringToComponentWithoutItalic("&e&l! &8This item will be consumed when used!"));
            lore.add(Component.space());
        }

        // add rarity lore
        lore.add(spell.getRarity().getDecoratedComponent().append(Component.text(" SPELL")));

        meta.lore(lore);

        // make item unbreakable
        if (itemStack.getType().getMaxDurability() != 0) {
            meta.setUnbreakable(true);
        }

        // set new metadata
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}

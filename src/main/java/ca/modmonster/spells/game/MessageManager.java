package ca.modmonster.spells.game;

import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.Random;

public class MessageManager {
    public static List<String> adjectives;
    public static List<String> verbs;
    public static String deathMessageFormatByPlayer;
    public static List<String> deathMessageFormats;
    public static List<String> deathMessageFormatsFire;
    public static List<String> deathMessageFormatsFall;
    public static List<String> deathMessageFormatsDrown;
    public static List<String> deathMessageFormatsExplode;
    public static List<String> deathMessageFormatsLightning;
    public static List<String> deathMessageFormatsWither;
    public static String deathMessageFormatByMinion;
    public static List<String> leaveMessageFormats;

    public static TextComponent getDeathMessage(Player victim, Player killer, Entity minion) {
        if (killer == null) {
            // victim died on their own
            EntityDamageEvent.DamageCause lastDamageCause = victim.getLastDamageCause().getCause();

            String deathMessage = switch (lastDamageCause) {
                case FIRE, FIRE_TICK ->
                        Utilities.getRandomEntryInArray(deathMessageFormatsFire).replace("%player%", victim.getName());
                case FALL ->
                        Utilities.getRandomEntryInArray(deathMessageFormatsFall).replace("%player%", victim.getName());
                case DROWNING ->
                        Utilities.getRandomEntryInArray(deathMessageFormatsDrown).replace("%player%", victim.getName());
                case ENTITY_EXPLOSION, BLOCK_EXPLOSION ->
                        Utilities.getRandomEntryInArray(deathMessageFormatsExplode).replace("%player%", victim.getName());
                case LIGHTNING ->
                        Utilities.getRandomEntryInArray(deathMessageFormatsLightning).replace("%player%", victim.getName());
                case WITHER ->
                        Utilities.getRandomEntryInArray(deathMessageFormatsWither).replace("%player%", victim.getName());
                default -> Utilities.getRandomEntryInArray(deathMessageFormats).replace("%player%", victim.getName());
            };

            return Utilities.getStatusMessage(Utilities.StatusMessageType.DEATH, Utilities.stringToComponent(deathMessage));
        }

        String chosenAdjective = adjectives.get(new Random().nextInt(adjectives.size()));
        String chosenVerb = verbs.get(new Random().nextInt(verbs.size()));

        if (minion != null) {
            // victim was killed by minion
            String deathMessage = deathMessageFormatByMinion
                .replace("%victim%", victim.getName())
                .replace("%killer%", killer.getName())
                .replace("%minion%", Utilities.getPersistentEntityTagString(minion, "minion_name"))
                .replace("%adjective%", chosenAdjective)
                .replace("%verb%", chosenVerb);

            return Utilities.getStatusMessage(Utilities.StatusMessageType.DEATH, Utilities.stringToComponent(deathMessage));
        }

        // victim was killed
        String deathMessage = deathMessageFormatByPlayer
            .replace("%victim%", victim.getName())
            .replace("%killer%", killer.getName())
            .replace("%adjective%", chosenAdjective)
            .replace("%verb%", chosenVerb);

        return Utilities.getStatusMessage(Utilities.StatusMessageType.DEATH, Utilities.stringToComponent(deathMessage));
    }
}

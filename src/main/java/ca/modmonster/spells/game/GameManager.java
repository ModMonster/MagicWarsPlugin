package ca.modmonster.spells.game;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.gameevents.BorderShrinkGameEvent;
import ca.modmonster.spells.game.gameevents.GameEvent;
import ca.modmonster.spells.game.gameevents.RefillChestsGameEvent;
import ca.modmonster.spells.game.gamestate.WaitingGameState;
import ca.modmonster.spells.util.Icons;
import ca.modmonster.spells.util.Utilities;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameManager {
    public static final List<WorldMap> maps = new ArrayList<>();
    public static Game activeGame;
    public static final List<Material> blockBreakWhitelist = new ArrayList<>();
    public static final List<Material> blockInteractWhitelist = new ArrayList<>();
    public static final List<Player> allowedBuildingPlayers = new ArrayList<>();
    public static final List<GameEvent> events = new ArrayList<>();

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

    public static ItemStack getLobbyCompass() {
        ItemStack compass = Utilities.getCustomItem(Material.COMPASS, Utilities.stringToComponentWithoutItalic("&8" + Icons.RIGHT_ARROW + " &3Return to Lobby &b&l(CLICK)"));
        Utilities.setGlowing(compass, true);
        return compass;
    }

    public static ItemStack getLobbyCompassUsableInWorld() {
        ItemStack compass = Utilities.getCustomItem(Material.COMPASS, Utilities.stringToComponentWithoutItalic("&3Return to Lobby &b&l(RIGHT CLICK)"));
        Utilities.setGlowing(compass, true);
        return compass;
    }

    public static TextComponent getDeathMessage(Player victim, Player killer, Entity minion) {
        if (killer == null) {
            // victim died on their own
            EntityDamageEvent.DamageCause lastDamageCause = victim.getLastDamageCause().getCause();

            String deathMessage;
            switch (lastDamageCause) {
                case FIRE:
                case FIRE_TICK:
                    deathMessage = Utilities.getRandomEntryInArray(deathMessageFormatsFire).replace("%player%", victim.getName());
                    break;
                case FALL:
                    deathMessage = Utilities.getRandomEntryInArray(deathMessageFormatsFall).replace("%player%", victim.getName());
                    break;
                case DROWNING:
                    deathMessage = Utilities.getRandomEntryInArray(deathMessageFormatsDrown).replace("%player%", victim.getName());
                    break;
                case ENTITY_EXPLOSION:
                case BLOCK_EXPLOSION:
                    deathMessage = Utilities.getRandomEntryInArray(deathMessageFormatsExplode).replace("%player%", victim.getName());
                    break;
                case LIGHTNING:
                    deathMessage = Utilities.getRandomEntryInArray(deathMessageFormatsLightning).replace("%player%", victim.getName());
                    break;
                case WITHER:
                    deathMessage = Utilities.getRandomEntryInArray(deathMessageFormatsWither).replace("%player%", victim.getName());
                    break;
                default:
                    deathMessage = Utilities.getRandomEntryInArray(deathMessageFormats).replace("%player%", victim.getName());
            }

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

    public static void init() {
        initializeMaps();
        initMapConfig();
        initEventConfig();
        createGame(Utilities.getRandomEntryInArray(maps));
    }

    static void initEventConfig() {
        List<List> configEvents = (List<List>) Spells.mainConfig.getList("game-events");

        for (List configEvent : configEvents) {
            switch ((String) configEvent.get(0)) {
                case "REFILL_CHESTS":
                    events.add(new RefillChestsGameEvent((int) configEvent.get(1)));
                    break;
                case "BORDER_SHRINK":
                    events.add(new BorderShrinkGameEvent((int) configEvent.get(1), (int) configEvent.get(2), (int) configEvent.get(3)));
            }
        }
    }

    static void initMapConfig() {
        // block break whitelist
        List<String> blockBreakMaterialStrings = Spells.mapConfig.getStringList("block-break-whitelist");
        for (String block : blockBreakMaterialStrings) {
            blockBreakWhitelist.add(Material.valueOf(block));
        }

        // block interact whitelist
        List<String> blockInteractMaterialStrings = Spells.mapConfig.getStringList("block-interact-whitelist");
        for (String block : blockInteractMaterialStrings) {
            blockInteractWhitelist.add(Material.valueOf(block));
        }
    }

    static void initializeMaps() {
        List<Map<?, ?>> configMaps = Spells.mapConfig.getMapList("maps");

        for (Map map : configMaps) {

            for (Map.Entry<String, Map> entry : (Iterable<Map.Entry<String, Map>>) map.entrySet()) {
                File sourceWorldFolder = new File(new File(Spells.main.getDataFolder(), "maps"), (String) entry.getValue().get("world"));

                // get chest locations
                List<LootChest> chestLocations = new ArrayList<>();

                for (List list : (List<List>) entry.getValue().get("chest-locations")) {
                    Vector location = new Vector((int) list.get(0), (int) list.get(1), (int) list.get(2));

                    chestLocations.add(new LootChest(location, (Boolean) list.get(3)));
                }

                List<Integer> spawnFacingList = (List<Integer>) entry.getValue().get("spawn-facing");
                Vector spawnFacing = new Vector(spawnFacingList.get(0), spawnFacingList.get(1), spawnFacingList.get(2));

                // get spawn locations
                List<Vector> spawnLocations = new ArrayList<>();

                for (List list : (List<List>) entry.getValue().get("spawn-locations")) {
                    Vector location = new Vector((int) list.get(0), (int) list.get(1), (int) list.get(2));

                    spawnLocations.add(location);
                }

                WorldMap worldMap = new WorldMap(
                    entry.getKey(),
                    (String) entry.getValue().get("name"),
                    (List<Integer>) entry.getValue().get("pod-size"),
                    sourceWorldFolder,
                    (int) entry.getValue().get("map-size"),
                    spawnFacing,
                    spawnLocations,
                    chestLocations,
                    (int) entry.getValue().get("min-players"),
                    (int) entry.getValue().get("max-players"),
                    (int) entry.getValue().get("max-build-height")
                );

                maps.add(worldMap);
            }
        }
    }

    static void createGame(WorldMap map) {
        ActiveWorld world = new ActiveWorld(map);
        Game game = new Game(world);
        activeGame = game;

        game.world.load();
        game.setState(new WaitingGameState());
    }
}
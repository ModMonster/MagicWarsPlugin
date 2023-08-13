//package ca.modmonster.spells.game;
//
//import ca.modmonster.spells.Spells;
//import ca.modmonster.spells.game.gameevents.BorderShrinkGameEvent;
//import ca.modmonster.spells.game.gameevents.GameEvent;
//import ca.modmonster.spells.game.gameevents.RefillChestsGameEvent;
//import ca.modmonster.spells.game.gamestate.GameState;
//import ca.modmonster.spells.game.gamestate.WaitingGameState;
//import ca.modmonster.spells.util.FileUtil;
//import ca.modmonster.spells.util.Icons;
//import ca.modmonster.spells.util.Utilities;
//import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.WorldCreator;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Vector;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class GameManager {
//    public static List<WorldMap> maps = new ArrayList<>();
//    public static final List<Material> blockBreakWhitelist = new ArrayList<>();
//    public static final List<Player> allowedBuildingPlayers = new ArrayList<>();
//    public static final List<GameEvent> events = new ArrayList<>();
//
//    public static GameState state = null;
//    public static WorldMap activeMap;
//    public static final Map<Player, Location> filledCages = new HashMap<>();
//    public static Integer startingCountdown = 30;
//
//    public static final List<Player> alivePlayers = new ArrayList<>();
//    public static BukkitRunnable countdown = null;
//    public static final Map<Player, Integer> kills = new HashMap<>();
//    public static final Map<BukkitRunnable, Player> runningPlayerEvents = new HashMap<>();
//    static final Map<Player, BetterScoreboard> boards = new HashMap<>();
//
//    public static int nextEventIndex = 0;
//    public static int time = 0;
//    public static BukkitRunnable eventTimerRunnable = null;
//
//    public static ItemStack blockerStack;
//    public static ItemStack trashStack;
//
//    static Player firstPlace = null;
//    static Player secondPlace = null;
//    static Player thirdPlace = null;
//
//    public static ItemStack getLobbyCompass() {
//        ItemStack compass = Utilities.getCustomItem(Material.COMPASS, Utilities.stringToComponentWithoutItalic("&8" + Icons.RIGHT_ARROW + " &3Return to Lobby &b&l(CLICK)"));
//        Utilities.setGlowing(compass, true);
//        return compass;
//    }
//
//    public static ItemStack getLobbyCompassUsableInWorld() {
//        ItemStack compass = Utilities.getCustomItem(Material.COMPASS, Utilities.stringToComponentWithoutItalic("&3Return to Lobby &b&l(RIGHT CLICK)"));
//        Utilities.setGlowing(compass, true);
//        return compass;
//    }
//
//    public static void init() {
//        initMaps();
//        initBreakWhitelist();
//        initEvents();
//        initGame();
//    }
//
//    static void initGame() {
//        // Choose a map
//        activeMap = Utilities.getRandomEntryInArray(maps);
//
//        File activeWorldFolder = new File(Bukkit.getWorldContainer().getParentFile(), "game");
//
//        if (activeWorldFolder.exists()) {
//            Spells.main.getLogger().warning("Folder exists still, deleting");
//            FileUtil.delete(activeWorldFolder);
//        }
//
//        Spells.main.getLogger().info("Copying world folder for map " + activeMap.id);
//
//        try {
//            FileUtil.copy(activeMap.sourceWorldFolder, activeWorldFolder);
//        } catch (IOException e) {
//            Spells.main.getLogger().severe("Failed to load map from source folder " + activeMap.sourceWorldFolder.getAbsolutePath());
//            e.printStackTrace();
//            return;
//        }
//
//        Spells.main.getLogger().info("Creating / loading game world from provided map " + activeMap.id);
//
//        // create world
//        Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));
//        Bukkit.getWorld("game").setAutoSave(false);
//
//        setState(new WaitingGameState());
//    }
//
//    static void initBreakWhitelist() {
//        List<String> blockBreakMaterialStrings = (List<String>) Spells.mapConfig.getList("block-break-whitelist");
//        for (String block : blockBreakMaterialStrings) {
//            blockBreakWhitelist.add(Material.valueOf(block));
//        }
//    }
//
//    static void initEvents() {
//        List<List> configEvents = (List<List>) Spells.  mainConfig.getList("game-events");
//
//        for (List configEvent : configEvents) {
//            switch ((String) configEvent.get(0)) {
//                case "REFILL_CHESTS":
//                    events.add(new RefillChestsGameEvent((int) configEvent.get(1)));
//                    break;
//                case "BORDER_SHRINK":
//                    events.add(new BorderShrinkGameEvent((int) configEvent.get(1), (int) configEvent.get(2), (int) configEvent.get(3)));
//            }
//        }
//    }
//
//    static void initMaps() {
//        List<Map<?, ?>> configMaps = Spells.mapConfig.getMapList("maps");
//
//        for (Map map : configMaps) {
//
//            for (Map.Entry<String, Map> entry : (Iterable<Map.Entry<String, Map>>) map.entrySet()) {
//                File sourceWorldFolder = new File(new File(Spells.main.getDataFolder(), "maps"), (String) entry.getValue().get("world"));
//
//                // get chest locations
//                List<LootChest> chestLocations = new ArrayList<>();
//
//                for (List list : (List<List>) entry.getValue().get("chest-locations")) {
//                    Vector location = new Vector((int) list.get(0), (int) list.get(1), (int) list.get(2));
//
//                    chestLocations.add(new LootChest(location, (Boolean) list.get(3)));
//                }
//
//                List<Integer> spawnFacingList = (List<Integer>) entry.getValue().get("spawn-facing");
//                Vector spawnFacing = new Vector(spawnFacingList.get(0), spawnFacingList.get(1), spawnFacingList.get(2));
//
//                // get spawn locations
//                List<Vector> spawnLocations = new ArrayList<>();
//
//                for (List list : (List<List>) entry.getValue().get("spawn-locations")) {
//                    Vector location = new Vector((int) list.get(0), (int) list.get(1), (int) list.get(2));
//
//                    spawnLocations.add(location);
//                }
//
//                WorldMap worldMap = new WorldMap(
//                    entry.getKey(),
//                    (String) entry.getValue().get("name"),
//                    (List<Integer>) entry.getValue().get("pod-size"),
//                    sourceWorldFolder,
//                    (int) entry.getValue().get("map-size"),
//                    spawnFacing,
//                    spawnLocations,
//                    chestLocations,
//                    (int) entry.getValue().get("min-players"),
//                    (int) entry.getValue().get("max-players")
//                );
//
//                maps.add(worldMap);
//            }
//        }
//    }
//}

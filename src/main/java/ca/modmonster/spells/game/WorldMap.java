package ca.modmonster.spells.game;

import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;

public class WorldMap {
    public final String id;
    public final String name;
    public final List<Integer> podSize;
    public final File sourceWorldFolder;
    public final int worldBorderSize;
    public final Vector spawnFacingLocation;
    public final List<Vector> podLocations;
    public final List<LootChest> chestLocations;
    public final Integer minPlayerCount;
    public final Integer maxPlayerCount;
    public final Integer maxBuildHeight;

    public WorldMap(String id, String name, List<Integer> podSize, File sourceWorldFolder, int worldBorderSize, Vector spawnFacingLocation, List<Vector> podLocations, List<LootChest> chestLocations, Integer minPlayerCount, Integer maxPlayerCount, Integer maxBuildHeight) {
        this.id = id;
        this.name = name;
        this.podSize = podSize;
        this.sourceWorldFolder = sourceWorldFolder;
        this.worldBorderSize = worldBorderSize;
        this.spawnFacingLocation = spawnFacingLocation;
        this.podLocations = podLocations;
        this.chestLocations = chestLocations;
        this.minPlayerCount = minPlayerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.maxBuildHeight = maxBuildHeight;
    }
}
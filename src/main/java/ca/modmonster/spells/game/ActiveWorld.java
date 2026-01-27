package ca.modmonster.spells.game;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.util.FileUtil;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ActiveWorld {
    public WorldMap map;
    public World bukkitWorld;
    public File activeWorldFolder;

    public ActiveWorld(WorldMap worldMap) {
        this.map = worldMap;
    }

    public boolean load() {
        if (isLoaded()) return true;

        Spells.main.getLogger().info("Copying world folder for map " + map.id);
        activeWorldFolder = new File(
            Bukkit.getWorldContainer().getParentFile(),
            map.sourceWorldFolder.getName() + "_active_" + System.currentTimeMillis()
        );

        try {
            FileUtil.copy(map.sourceWorldFolder, activeWorldFolder);
        } catch (IOException e) {
            Spells.main.getLogger().severe("Failed to load game world from source folder " + map.sourceWorldFolder.getAbsolutePath());
            e.printStackTrace();
            return false;
        }

        Spells.main.getLogger().info("Creating / loading game world from provided map " + map.id);
        bukkitWorld = Bukkit.createWorld(
            new WorldCreator(activeWorldFolder.getName())
        );

        if (bukkitWorld != null) {
            bukkitWorld.setAutoSave(false);
            fillChests();
        }
        return isLoaded();
    }

    public void fillChests() {
        Spells.main.getLogger().info("Filling chests...");

        // fill chests
        for (LootChest chest : map.chestLocations) {
            LootChest.spawnLootChest(bukkitWorld, chest, false);
        }
    }

    public void unload() {
        if (bukkitWorld != null) {
            Spells.main.getLogger().info("Unloading game world");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kick(Utilities.stringToComponent("&cForcibly transferring you to lobby."));
                Spells.main.getLogger().info("Byebye " + player.getName());
            }
            if (!Bukkit.unloadWorld(bukkitWorld, false)) Spells.main.getLogger().severe("FAILED TO UNLOAD GAME WORLD!!");
        }

        if (activeWorldFolder != null) {
            FileUtil.delete(activeWorldFolder);
            Spells.main.getLogger().info("Deleted game world");
        }

        bukkitWorld = null;
        activeWorldFolder = null;
    }

    public boolean reset() {
        Spells.main.getLogger().info("Resetting game world");
        unload();
        return load();
    }

    public boolean isLoaded() {
        return bukkitWorld != null;
    }
}

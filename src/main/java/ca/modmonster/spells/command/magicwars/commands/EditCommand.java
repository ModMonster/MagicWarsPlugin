package ca.modmonster.spells.command.magicwars.commands;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.command.Subcommand;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditCommand extends Subcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eOnly players can use this command"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eYou must specify an edit parameter!"));
            return;
        }

        if (args.length < 3 && args[1].equals("addchest")) {
            sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eYou must specify whether or not the chest can contain enchanted books!"));
            return;
        }

        if (args[1].equals("findchests")) {
            sender.sendMessage("Finding chests...");
            List<Vector> chests = new ArrayList<>();

            Location worldCenter = player.getWorld().getWorldBorder().getCenter();
            double worldSize = (player.getWorld().getWorldBorder().getSize() / 2) + 1;

            for (double x = worldCenter.getX() - worldSize; x <= worldCenter.getX() + worldSize; x++) {
                for (double z = worldCenter.getZ() - worldSize; z <= worldCenter.getZ() + worldSize; z++) {
                    for (int y = 0; y < 256; y++) {
                        Location location = new Location(player.getWorld(), x, y, z);
                        if (location.getBlock().getType().equals(Material.CHEST)) {
                            chests.add(location.toVector());
                        }
                    }
                }
            }

            sender.sendMessage("Found " + chests.size() + " chests!");
            sender.sendMessage("Searched between:");
            sender.sendMessage("x=" + (worldCenter.getX() - worldSize) + ", " + (worldCenter.getX() + worldSize));
            sender.sendMessage("z=" + (worldCenter.getZ() - worldSize) + ", " + (worldCenter.getZ() + worldSize));

            File outputFile = new File(Spells.main.getDataFolder(), "output-chests.txt");

            if (!outputFile.exists()) {
                try {
                    outputFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                for (Vector vector : chests) {
                    List<Integer> location = Arrays.asList(
                        (int) Math.floor(vector.getX()),
                        (int) Math.floor(vector.getY()),
                        (int) Math.floor(vector.getZ())
                    );

                    Files.writeString(outputFile.toPath(), location + "\n", StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if (args[1].equals("pos1")) {
//            pos1 = player.getLocation();
//            sender.sendMessage(Utilities.stringToComponent("&aSet position 1"));
//            return;
//        }
//
//        if (args[1].equals("pos2")) {
//            pos2 = player.getLocation();
//            sender.sendMessage(Utilities.stringToComponent("&aSet position 2"));
//            return;
//        }
//
//        if (args[1].equals("tpchest")) {
//            if (pos1 == null || pos2 == null) {
//                sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eYou must set pos1 and pos2 first!"));
//                return;
//            }
//
//            List<Chunk> scanChunks = new ArrayList<>();
//
//            // get coordinates
//            int xMin = Math.min(pos1.getChunk().getX(), pos2.getChunk().getX());
//            int xMax = Math.max(pos1.getChunk().getX(), pos2.getChunk().getX());
//            int zMin = Math.min(pos1.getChunk().getZ(), pos2.getChunk().getZ());
//            int zMax = Math.max(pos1.getChunk().getZ(), pos2.getChunk().getZ());
//
//            // get chunks to scan
//            for (int x = xMin; x < xMax; x++) {
//                for (int z = zMin; z < zMax; z++) {
//                    scanChunks.add(player.getWorld().getChunkAt(x, z));
//                }
//            }
//
//            // scan chunks
//            Block nearestBlock = null;
//            Double nearestDistance = null;
//
//            for (Chunk chunk : scanChunks) {
//                for (BlockState state : chunk.getTileEntities()) {
//                    Double distance = state.getLocation().distanceSquared(player.getLocation());
//
//                    if (nearestBlock == null) {
//                        if (state.getBlock().getType().equals(Material.CHEST) && ((Chest) state).getBlockInventory().isEmpty()) nearestBlock = state.getBlock();
//                        nearestDistance = distance;
//                    } else if (nearestDistance > distance && state.getBlock().getType().equals(Material.CHEST) && ((Chest) state).getBlockInventory().isEmpty()) {
//                        nearestBlock = state.getBlock();
//                        nearestDistance = distance;
//                    }
//                }
//            }
//
//            if (nearestBlock == null) {
//                sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eNo chests in this area! You're done!"));
//                return;
//            }
//
//            player.teleport(nearestBlock.getLocation());
//        }
//
//        if (args[1].equals("addchest")) {
//            Location location = player.getTargetBlock(4).getLocation();
//            boolean canContainEnchantedBooks = args[2].equals("true");
//
//            if (!location.getBlock().getType().equals(Material.CHEST)) {
//                sender.sendMessage(Utilities.stringToComponent("&7[&c&lERROR&7]: &eBlock is not a chest!"));
//                return;
//            }
//
//            location.getBlock().setType(Material.AIR);
//
//            // add chest
//            List<?> newChestLocation = Arrays.asList(
//                location.getBlockX(),
//                location.getBlockY(),
//                location.getBlockZ(),
//                canContainEnchantedBooks
//            );
//
//            File outputFile = new File(Spells.main.getDataFolder(), "output-chests.txt");
//
//            if (!outputFile.exists()) {
//                try {
//                    outputFile.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            try {
//                Files.write(outputFile.toPath(), (newChestLocation + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return;
//        }

        if (args[1].equals("addspawnpod")) {
            Location location = player.getLocation();

            // add spawn pod
            List<?> spawnPodLocation = Arrays.asList(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
            );

            File outputFile = new File(Spells.main.getDataFolder(), "output-spawnpods.txt");

            if (!outputFile.exists()) {
                try {
                    outputFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Files.writeString(outputFile.toPath(), spawnPodLocation + "\n", StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> possibilities = new ArrayList<>();

        possibilities.add("addspawnpod");
        possibilities.add("findchests");

        return possibilities;
    }
}

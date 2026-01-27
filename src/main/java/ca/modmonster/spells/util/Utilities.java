package ca.modmonster.spells.util;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.WorldMap;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Utilities {
    public static ItemStack setPersistentItemTag(ItemStack item, String tag, String value) {
        // get item metadata
        ItemMeta meta = item.getItemMeta();

        // store value
        meta.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.STRING, value);

        // set item metadata
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setPersistentItemTag(ItemStack item, String tag, Integer value) {
        // get item metadata
        ItemMeta meta = item.getItemMeta();

        // store value
        meta.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.INTEGER, value);

        // set item metadata
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setPersistentItemTag(ItemStack item, String tag, Float value) {
        // get item metadata
        ItemMeta meta = item.getItemMeta();

        // store value
        meta.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.FLOAT, value);

        // set item metadata
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setPersistentItemTag(ItemStack item, String tag, boolean value) {
        // get item metadata
        ItemMeta meta = item.getItemMeta();

        // store value
        meta.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.BOOLEAN, value);

        // set item metadata
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setPersistentItemTag(ItemStack item, String tag, int[] value) {
        // get item metadata
        ItemMeta meta = item.getItemMeta();

        // store value
        meta.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.INTEGER_ARRAY, value);

        // set item metadata
        item.setItemMeta(meta);

        return item;
    }

    public static String getPersistentItemTagString(ItemStack item, String tag) {
        // get value
        if (item.getItemMeta() != null) {
            return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.STRING);
        }
        return null;
    }

    public static Integer getPersistentItemTagInteger(ItemStack item, String tag) {
        // get value
        if (item.getItemMeta() != null) {
            return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.INTEGER);
        }
        return null;
    }

    public static Float getPersistentItemTagFloat(ItemStack item, String tag) {
        // get value
        if (item.getItemMeta() != null) {
            return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.FLOAT);
        }
        return null;
    }

    public static Boolean getPersistentItemTagBoolean(ItemStack item, String tag) {
        // get value
        if (item.getItemMeta() != null) {
            return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.BOOLEAN);
        }
        return null;
    }

    public static int[] getPersistentItemTagIntArray(ItemStack item, String tag) {
        // get value
        if (item.getItemMeta() != null) {
            return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.INTEGER_ARRAY);
        }
        return null;
    }

    public static void removePersistantItemTag(ItemStack item, String tag) {
        // remove value
        item.getItemMeta().getPersistentDataContainer().remove(new NamespacedKey(Spells.main, tag));
    }


    public static void setPersistentEntityTag(Entity entity, String tag, String value) {
        // set entity data
        entity.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.STRING, value);

    }

    public static void setPersistentEntityTag(Entity entity, String tag, Integer value) {
        // set entity data
        entity.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.INTEGER, value);

    }

    public static Entity setPersistentEntityTag(Entity entity, String tag, Float value) {
        // set entity data
        entity.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.FLOAT, value);

        return entity;
    }

    public static Entity setPersistentEntityTag(Entity entity, String tag, boolean value) {
        // set entity data
        entity.getPersistentDataContainer().set(new NamespacedKey(Spells.main, tag), PersistentDataType.INTEGER, value? 1 : 0);

        return entity;
    }

    public static String getPersistentEntityTagString(Entity entity, String tag) {
        // get value
        return entity.getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.STRING);
    }

    public static Integer getPersistentEntityTagInteger(Entity entity, String tag) {
        // get value
        return entity.getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.INTEGER);
    }

    public static Float getPersistentEntityTagFloat(Entity entity, String tag) {
        // get value
        return entity.getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.FLOAT);
    }

    public static boolean getPersistentEntityTagBoolean(Entity entity, String tag) {
        // get value
        Integer value = entity.getPersistentDataContainer().get(new NamespacedKey(Spells.main, tag), PersistentDataType.INTEGER);

        return value != null && value == 1;
    }

    public static void removePersistentEntityTag(Entity entity, String tag) {
        // remove value
        entity.getPersistentDataContainer().remove(new NamespacedKey(Spells.main, tag));
    }

    public static ItemStack setGlowing(ItemStack itemStack, boolean glow) {
        if (glow) {
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.addUnsafeEnchantment(itemStack.getType().equals(Material.BOW)? Enchantment.PROTECTION : Enchantment.POWER, 1);
        } else {
            itemStack.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.removeEnchantment(itemStack.getType().equals(Material.BOW)? Enchantment.PROTECTION : Enchantment.POWER);
        }

        return itemStack;
    }

    public static String removeDoubleTrailingZeroToString(Double num) {
        return num.toString().replaceFirst("\\.0*$|(\\.\\d*?)0+$", "$1");
    }

    public static String ticksToSeconds(Integer ticks) {
        if (ticks == null) {
            return null;
        }

        Double cooldownSeconds = ticks / 20.0;

        return removeDoubleTrailingZeroToString(cooldownSeconds);
    }

    public static Integer ticksToSecondsInt(Integer ticks) {
        return Math.round(ticks / 20f);
    }

    public static List<String> addNewlinesToStringForLore(String string) {
        String[] lines = ChatPaginator.wordWrap(string, 40);
        return Arrays.asList(lines);
    }

    public static RaycastTarget raycastTargetFromLocation(Player player, Location location, float padding) {
        // check for block
        Block block = location.getBlock();
        Material blockType = block.getType();

        if (blockType.isSolid()) {
            // hit a block
            return new RaycastTarget(
                null,
                null,
                block
            );
        }

        // check for entities
        Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, padding, padding, padding);

        if (!nearbyEntities.isEmpty()) {
            boolean skip = false;
            Entity entity = nearbyEntities.iterator().next();

            if (entity instanceof Player && entity.equals(player)) {
                skip = true;
            }

            if (!skip) {
                if (entity instanceof LivingEntity livingEntity) {

                    return new RaycastTarget(
                        entity,
                        livingEntity,
                        null
                    );
                } else {
                    return new RaycastTarget(
                        entity,
                        null,
                        null
                    );
                }
            }
        }

        return null;
    }

    public static RaycastTarget raycast(Player player, Integer maxDistance, float padding) {
        Vector direction = player.getLocation().getDirection().normalize();

        int currentDistance = 0;

        while (currentDistance < maxDistance) {
            Location location = player.getEyeLocation().subtract(0, 0.5, 0).add(direction.clone().multiply(currentDistance / 2f));
            currentDistance += 1;

            RaycastTarget raycastTargetAtLocation = raycastTargetFromLocation(player, location, padding);
            if (raycastTargetAtLocation != null) return raycastTargetAtLocation;
        }

        return null;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static Location vectorToLocation(World world, Vector vector) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    public static Location vectorToBlockLocation(World world, Vector vector) {
        return new Location(world, vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public static Location centerLocationOnBlock(Location location) {
        return location.add(0.5, 0, 0.5);
    }

    public static void teleportPlayerWithFacing(Player player, Location location, float pitch, float yaw) {
        player.teleport(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), yaw, pitch));
    }

    public static TextComponent stringToComponent(String string) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
    }

    public static TextComponent stringToComponentWithoutItalic(String string) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(string).decoration(TextDecoration.ITALIC, false);
    }

    public static String componentToString(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    public static String integerToRoman(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanLetters = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num = num - values[i];
                roman.append(romanLetters[i]);
            }
        }

        return roman.toString();
    }

    public static Integer getValidShiftClickSlot(Inventory playerInventory) {
        int chosenShiftClickSpot = -1;

        for (int i = 9; i < 36; i++) {
            ItemStack itemInSlot = playerInventory.getItem(i);
            if (itemInSlot != null && !itemInSlot.getType().isAir()) continue;

            chosenShiftClickSpot = i;
        }

        for (int i = 0; i < 9; i++) {
            ItemStack itemInSlot = playerInventory.getItem(i);
            if (itemInSlot != null && !itemInSlot.getType().isAir()) continue;

            chosenShiftClickSpot = i;
        }

        if (chosenShiftClickSpot == -1) return null;
        return chosenShiftClickSpot;
    }

    /**
     * Gets the highest block at location (best spreadplayers teleport spot)
     * @param location location to get block at
     * @return new Location representing the highest block
     */
    public static @Nullable Location getHighestBlockYAtLocation(@NotNull Location location) {
        int height = location.getWorld().getMaxHeight();

        while (height > location.getWorld().getMinHeight()) {
            height--;
            Location newLocation = location.clone();
            newLocation.setY(height);
            Block block = newLocation.getBlock();

            if (block.isSolid()) {
                block = newLocation.add(0, 1, 0).getBlock();
                if (!block.isSolid()) {
                    return newLocation;
                }
            }
        }

        return null;
    }

    /**
     * @param location location to check
     * @return whether the given location is within the set world border
     */
    public static boolean isLocationWithinWorldBorder(@NotNull Location location) {
        double worldBorderSize = location.getWorld().getWorldBorder().getSize() / 2;
        Location worldBorderCenter = location.getWorld().getWorldBorder().getCenter();

        Location lowestCorner = new Location(location.getWorld(), worldBorderCenter.getX() - worldBorderSize, 0, worldBorderCenter.getZ() - worldBorderSize);
        Location highestCorner = new Location(location.getWorld(), worldBorderCenter.getX() + worldBorderSize, 0, worldBorderCenter.getZ() + worldBorderSize);

        return location.getX() >= lowestCorner.getX() && location.getX() <= highestCorner.getX() && location.getZ() >= lowestCorner.getZ() && location.getZ() <= highestCorner.getZ();
    }

    /**
     * @param location location to check
     * @return whether the given location is within the boundaries of the world (as specified in maps.yml config)
     */
    public static boolean isLocationWithinWorldBounds(@NotNull Location location, @NotNull WorldMap map) {
        double worldBorderSize = map.worldBorderSize / 2.0;
        Location worldBorderCenter = location.getWorld().getWorldBorder().getCenter();

        Location lowestCorner = new Location(location.getWorld(), worldBorderCenter.getX() - worldBorderSize, 0, worldBorderCenter.getZ() - worldBorderSize);
        Location highestCorner = new Location(location.getWorld(), worldBorderCenter.getX() + worldBorderSize, 0, worldBorderCenter.getZ() + worldBorderSize);

        if (location.getY() < 0) {
            return false;
        }

        return location.getX() >= lowestCorner.getX() && location.getX() <= highestCorner.getX() && location.getZ() >= lowestCorner.getZ() && location.getZ() <= highestCorner.getZ();
    }

    /**
     * Gets a safe teleport place within the world border in a given world.
     * @param world world to get teleport location within
     * @return a random location within world borders to teleport to
     */
    public static @NotNull Location getValidTeleportLocationWithinWorldBorder(@NotNull World world) {
        Location worldBorderCenter = world.getWorldBorder().getCenter();
        double worldBorderSize = world.getWorldBorder().getSize();

        Location teleportLocationNoY = new Location(world, new Random().nextDouble() * worldBorderSize - (worldBorderSize / 2), 0.0, new Random().nextDouble() * worldBorderSize - (worldBorderSize / 2));
        teleportLocationNoY.add(worldBorderCenter);

        teleportLocationNoY.setX(Math.round(teleportLocationNoY.getX()));
        teleportLocationNoY.setZ(Math.round(teleportLocationNoY.getZ()));

        Location teleportLocation = getHighestBlockYAtLocation(teleportLocationNoY);
        if (teleportLocation == null) {
            teleportLocation = teleportLocationNoY;
            teleportLocation.setY(180);
        }
        centerLocationOnBlock(teleportLocation);

        return teleportLocation;
    }

    /**
     * More reliable function compared to the Spigot API function {@link Material#isInteractable()}.
     * @param material the material to check.
     * @return true if the material is interactable, false otherwise.
     */
    public static boolean isInteractable(Material material)
    {
        boolean interactable = material.isInteractable();
        if (!interactable)
            return false;

        return switch (material) {
            case ACACIA_STAIRS, ANDESITE_STAIRS, BIRCH_STAIRS, BLACKSTONE_STAIRS, BRICK_STAIRS, COBBLESTONE_STAIRS,
                 CRIMSON_STAIRS, DARK_OAK_STAIRS, DARK_PRISMARINE_STAIRS, DIORITE_STAIRS, END_STONE_BRICK_STAIRS,
                 GRANITE_STAIRS, JUNGLE_STAIRS, MOSSY_COBBLESTONE_STAIRS, MOSSY_STONE_BRICK_STAIRS, NETHER_BRICK_STAIRS,
                 OAK_STAIRS, POLISHED_ANDESITE_STAIRS, POLISHED_BLACKSTONE_BRICK_STAIRS, POLISHED_BLACKSTONE_STAIRS,
                 POLISHED_DIORITE_STAIRS, POLISHED_GRANITE_STAIRS, PRISMARINE_BRICK_STAIRS, PRISMARINE_STAIRS,
                 PURPUR_STAIRS, QUARTZ_STAIRS, RED_NETHER_BRICK_STAIRS, RED_SANDSTONE_STAIRS, SANDSTONE_STAIRS,
                 SMOOTH_QUARTZ_STAIRS, SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_SANDSTONE_STAIRS, SPRUCE_STAIRS,
                 STONE_BRICK_STAIRS, STONE_STAIRS, WARPED_STAIRS, ACACIA_FENCE, BIRCH_FENCE, CRIMSON_FENCE,
                 DARK_OAK_FENCE, JUNGLE_FENCE, MOVING_PISTON, NETHER_BRICK_FENCE, OAK_FENCE, PUMPKIN, REDSTONE_ORE,
                 REDSTONE_WIRE, SPRUCE_FENCE, WARPED_FENCE -> false;
            default -> true;
        };
    }

    public static List<Block> getBlocks(Location start, int radius){
        List<Block> blocks = new ArrayList<>();
        for(double x = start.getX() - radius; x <= start.getX() + radius; x++){
            for(double y = start.getY() - radius; y <= start.getY() + radius; y++){
                for(double z = start.getZ() - radius; z <= start.getZ() + radius; z++){
                    Location loc = new Location(start.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }
            }
        }
        return blocks;
    }

    public static <T> T getRandomEntryInArray(T[] array) {
        return array[new Random().nextInt(array.length)];
    }

    public static <T> T getRandomEntryInArray(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    /**
     * @param material the material to check
     * @return the EquipmentSlot that the material can be equipped in
     */
    public static @Nullable EquipmentSlot getArmorEquipmentSlotOfMaterial(@NotNull Material material) {
        return switch (material) {
            case LEATHER_HELMET, CHAINMAIL_HELMET, GOLDEN_HELMET, IRON_HELMET, DIAMOND_HELMET, NETHERITE_HELMET,
                 TURTLE_HELMET
                    -> EquipmentSlot.HEAD;
            case LEATHER_CHESTPLATE, CHAINMAIL_CHESTPLATE, GOLDEN_CHESTPLATE, IRON_CHESTPLATE, DIAMOND_CHESTPLATE,
                 NETHERITE_CHESTPLATE, ELYTRA
                    -> EquipmentSlot.CHEST;
            case LEATHER_LEGGINGS, CHAINMAIL_LEGGINGS, GOLDEN_LEGGINGS, IRON_LEGGINGS, DIAMOND_LEGGINGS,
                 NETHERITE_LEGGINGS
                    -> EquipmentSlot.LEGS;
            case LEATHER_BOOTS, CHAINMAIL_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS
                    -> EquipmentSlot.FEET;
            default -> null;
        };
    }

    /**
     * @param material the material to check
     * @return whether the Material represents a piece of armor
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isMaterialArmor(@NotNull Material material) {
        return getArmorEquipmentSlotOfMaterial(material) != null;
    }

    public static ItemStack getCustomItem(Material material, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getCustomItem(Material material, Component name, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public enum StatusMessageType {
        TELEPORT("WARP"),
        JOIN("JOIN"),
        LEAVE("QUIT"),
        INFO("INFO"),
        DEATH("DEATH");

        final String name;

        StatusMessageType(String name) {
            this.name = name;
        }
    }

    public static TextComponent getStatusMessage(StatusMessageType status, String message) {
        return Utilities.stringToComponent("&3&l" + status.name + "&8 » &b" + message);
    }

    public static TextComponent getStatusMessage(StatusMessageType status, Component message) {
        return Utilities.stringToComponent("&3&l" + status.name + "&8 » &b").append(message);
    }

    public static Component getComponentWithDefaultColor(Component component, TextColor defaultColor) {
        if (component.color() == null || component.color().equals(NamedTextColor.WHITE)) {
            return component.color(defaultColor);
        }
        return component;
    }

    public static Component getComponentWithDefaultColorAqua(Component component) {
        return getComponentWithDefaultColor(component, NamedTextColor.AQUA);
    }

    public static void bungeeServerSend(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(Spells.main, "BungeeCord", out.toByteArray());
    }
}
package ca.modmonster.spells.util;

import ca.modmonster.spells.Spells;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InvisibilityManager {
    static Map<BukkitRunnable, LivingEntity> unvanishRunnables = new HashMap<>();

    public static void init() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            new PacketAdapter(Spells.main, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_EQUIPMENT) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    PacketContainer packet = event.getPacket();
                    Player receivingPlayer = event.getPlayer();
                    int entityId = packet.getIntegers().getValues().getFirst();
                    for (Player potentiallyInvisiblePlayer : receivingPlayer.getWorld().getPlayers()) {
                        if (potentiallyInvisiblePlayer.getEntityId() == entityId && isInvisible(potentiallyInvisiblePlayer)) {
                            packet.getSlotStackPairLists().write(0, Arrays.asList(
                                new Pair(EnumWrappers.ItemSlot.HEAD, null),
                                new Pair(EnumWrappers.ItemSlot.CHEST, null),
                                new Pair(EnumWrappers.ItemSlot.LEGS, null),
                                new Pair(EnumWrappers.ItemSlot.FEET, null),
                                new Pair(EnumWrappers.ItemSlot.MAINHAND, getItemInSlot(potentiallyInvisiblePlayer, EnumWrappers.ItemSlot.MAINHAND)),
                                new Pair(EnumWrappers.ItemSlot.OFFHAND, getItemInSlot(potentiallyInvisiblePlayer, EnumWrappers.ItemSlot.OFFHAND))
                            ));
                        }
                    }
                }
            }
        );
    }

    public static boolean isInvisible(Player player) {
        return unvanishRunnables.containsValue(player);
    }

    public static void makeEntityInvisibleForTicks(@NotNull LivingEntity entity, int ticks) {
        sendHiddenArmorPacket(entity);

        BukkitRunnable unvanishRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                unvanishRunnables.remove(this);

                if (unvanishRunnables.containsValue(entity)) return;

                sendVisibleArmorPacket(entity);
            }
        };
        unvanishRunnable.runTaskLater(Spells.main, ticks);

        unvanishRunnables.put(unvanishRunnable, entity);
    }

    public static void removeInvisibility(@NotNull Player player) {
        Map<BukkitRunnable, LivingEntity> newMap = new HashMap<>(unvanishRunnables);

        for (Map.Entry<BukkitRunnable, LivingEntity> runnable : unvanishRunnables.entrySet()) {
            if (runnable.getValue().equals(player)) {
                newMap.remove(runnable.getKey());
            }
        }

        unvanishRunnables = new HashMap<>(newMap);

        sendVisibleArmorPacket(player);
    }
    
    static void sendHiddenArmorPacket(@NotNull LivingEntity entity) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packetContainer.getIntegers().write(0, entity.getEntityId());
        packetContainer.getSlotStackPairLists().write(0, Arrays.asList(
            new Pair(EnumWrappers.ItemSlot.HEAD, null),
            new Pair(EnumWrappers.ItemSlot.CHEST, null),
            new Pair(EnumWrappers.ItemSlot.LEGS, null),
            new Pair(EnumWrappers.ItemSlot.FEET, null)
        ));

        for (Player entity1 : entity.getWorld().getPlayers()) {
            if (entity1.equals(entity)) continue;

            ProtocolLibrary.getProtocolManager().sendServerPacket(entity1, packetContainer);
        }
    }

    static void sendVisibleArmorPacket(@NotNull LivingEntity entity) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packetContainer.getIntegers().write(0, entity.getEntityId());
        packetContainer.getSlotStackPairLists().write(0, Arrays.asList(
            new Pair(EnumWrappers.ItemSlot.HEAD, getItemInSlot(entity, EnumWrappers.ItemSlot.HEAD)),
            new Pair(EnumWrappers.ItemSlot.CHEST, getItemInSlot(entity, EnumWrappers.ItemSlot.CHEST)),
            new Pair(EnumWrappers.ItemSlot.LEGS, getItemInSlot(entity, EnumWrappers.ItemSlot.LEGS)),
            new Pair(EnumWrappers.ItemSlot.FEET, getItemInSlot(entity,EnumWrappers.ItemSlot.FEET ))
        ));

        for (Player entity1 : entity.getWorld().getPlayers()) {
            if (entity1.equals(entity)) continue;

            ProtocolLibrary.getProtocolManager().sendServerPacket(entity1, packetContainer, false);
        }
    }

    static ItemStack getItemInSlot(LivingEntity entity, EnumWrappers.ItemSlot slot)
    {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) return null;
        ItemStack item = switch (slot) {
            case MAINHAND -> equipment.getItemInMainHand();
            case OFFHAND -> equipment.getItemInOffHand();
            case FEET -> equipment.getBoots();
            case LEGS -> equipment.getLeggings();
            case CHEST -> equipment.getChestplate();
            case HEAD -> equipment.getHelmet();
            default -> null;
        };

        //Sending equipment packet with AIR crashes the client and prints a Netty NPE.
        return item == null || item.getType() == Material.AIR ? null : item;
    }
}

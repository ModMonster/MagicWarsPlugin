package ca.modmonster.spells.events;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.gui.EnchanterGui;
import ca.modmonster.spells.item.spell.*;
import ca.modmonster.spells.util.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnPlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        useSpells(event);
        handleCustomEnchantingTable(event);
        preventOpeningGrindstone(event);
        doLobbyCompass(event);
    }

    void doLobbyCompass(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null || event.getItem().getItemMeta() == null) return;
        ItemMeta itemMeta = event.getItem().getItemMeta();
        if (!itemMeta.equals(GameManager.getLobbyCompassUsableInWorld().getItemMeta())) return;

        player.sendMessage(Utilities.getStatusMessage(Utilities.StatusMessageType.TELEPORT, "Sending you to &lLobby"));
        Utilities.bungeeServerSend(player, Spells.mainConfig.getString("lobby-server"));
    }

    void preventOpeningGrindstone(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!event.getClickedBlock().getType().equals(Material.GRINDSTONE)) return;

        event.setCancelled(true);
    }

    void handleCustomEnchantingTable(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)) return;
        if (!event.getAction().isRightClick()) return;
        if (event.getPlayer().isSneaking() && event.getItem() != null && !event.getItem().getType().isAir()) return;

        // open gui
        new EnchanterGui().openInventory(event.getPlayer());

        event.setCancelled(true);
    }

    void useSpells(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
                if (event.getPlayer().isSneaking()) {
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.AIR, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.AIR, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
                } else {
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.AIR, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.AIR, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
                }

                triggerAbility(event, ClickType.CLICK, ClickBlockType.AIR, ShiftType.NONE);
                triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.AIR, ShiftType.NONE);
                triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
                triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.NONE, ShiftType.NONE);
                break;
            case LEFT_CLICK_BLOCK:
                if (event.getPlayer().isSneaking()) {
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.BLOCK, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.BLOCK, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
                } else {
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.BLOCK, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.BLOCK, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
                }

                triggerAbility(event, ClickType.CLICK, ClickBlockType.BLOCK, ShiftType.NONE);
                triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.BLOCK, ShiftType.NONE);
                triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
                triggerAbility(event, ClickType.LEFT_CLICK, ClickBlockType.NONE, ShiftType.NONE);
                break;
            case RIGHT_CLICK_AIR:
                if (event.getPlayer().isSneaking()) {
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.AIR, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.AIR, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
                } else {
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.AIR, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.AIR, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
                }

                triggerAbility(event, ClickType.CLICK, ClickBlockType.AIR, ShiftType.NONE);
                triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.AIR, ShiftType.NONE);
                triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
                triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NONE);
                break;
            case RIGHT_CLICK_BLOCK:
                if (event.getPlayer().isSneaking()) {
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.BLOCK, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.BLOCK, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
                    triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.SHIFT);
                } else {
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.BLOCK, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.BLOCK, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
                    triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NOSHIFT);
                }

                triggerAbility(event, ClickType.CLICK, ClickBlockType.BLOCK, ShiftType.NONE);
                triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.BLOCK, ShiftType.NONE);
                triggerAbility(event, ClickType.CLICK, ClickBlockType.NONE, ShiftType.NONE);
                triggerAbility(event, ClickType.RIGHT_CLICK, ClickBlockType.NONE, ShiftType.NONE);
        }
    }

    public static void triggerAbility(PlayerInteractEvent event, ClickType clickType, ClickBlockType clickBlockType, ShiftType shiftType) {
        ItemStack item = event.getItem();

        // air or something lol
        if (item == null) {
            return;
        }

        String spellType = Utilities.getPersistentItemTagString(item, "spell_type");

        // regular item, not a spell
        if (spellType == null) {
            return;
        }

        Spell spell = SpellManager.getSpellFromItem(item);
        assert spell != null;

        // allow players to open chests with spells
        if (!event.getPlayer().isSneaking() && event.getClickedBlock() != null && Utilities.isInteractable(event.getClickedBlock().getType()) && spell.hasAbilityOfType(ClickType.RIGHT_CLICK)) {
            return;
        }

        spell.triggerAbility(event, clickType, clickBlockType, shiftType, item);

        // cancel right click event
        if (!event.getAction().isLeftClick() || event.getClickedBlock() == null || Utilities.isInteractable(event.getClickedBlock().getType())) {
            event.setCancelled(true);
        }
    }
}

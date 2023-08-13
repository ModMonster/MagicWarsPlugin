package ca.modmonster.spells.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaySound {
    public static void error(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 0.5f, 1);
    }

    public static void error(List<Player> players) {
        for (Player player : players) {
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 1, 1);
        }
    }

    public static void shoot(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1, 1);
    }

    public static void ding(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }

    public static void ding(List<Player> players) {
        for (Player player : players) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }

    public static void death(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_BAT_DEATH, 1, 1);
    }

    public static void death(List<Player> players) {
        for (Player player : players) {
            player.playSound(player.getLocation(), Sound.ENTITY_BAT_DEATH, 1, 1);
        }
    }

    public static void highPitchDing(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
    }

    public static void highPitchDing(List<Player> players) {
        for (Player player : players) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
        }
    }

    public static void click(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
    }

    public static void click(List<Player> players) {
        for (Player player : players) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }

    public static void explode(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
    }

    public static void pling(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    public static void pling(Player player, Integer pitch) {
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, pitch);
    }

    public static void pling(List<Player> players) {
        for (Player player : players) {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }
    }

    public static void pling(List<Player> players, Integer pitch) {
        for (Player player : players) {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, pitch);
        }
    }

    public static void potion(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
    }

    public static void potion(List<Player> players) {
        for (Player player : players) {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
        }
    }

    public static void pop(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
    }

    public static void pop(List<Player> players) {
        for (Player player : players) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
        }
    }

    public static void teleport(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    public static void teleport(List<Player> players) {
        for (Player player : players) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }
    }

    public static void forPlayersInList(List<Player> players, Sound sound) {
        for (Player player : players) {
            player.playSound(player.getLocation(), sound, 1, 1);
        }
    }
}

package ca.modmonster.spells.game;

import ca.modmonster.spells.Spells;
import ca.modmonster.spells.events.OnEntityDamage;
import ca.modmonster.spells.game.gamestate.*;
import ca.modmonster.spells.item.spell.spells.minion.Minion;
import ca.modmonster.spells.util.AnimationHelper;
import ca.modmonster.spells.util.Icons;
import ca.modmonster.spells.util.betterscoreboard.BetterScoreboard;
import ca.modmonster.spells.util.betterscoreboard.ScoreboardManager;
import ca.modmonster.spells.util.PlaySound;
import ca.modmonster.spells.util.Utilities;
import ca.modmonster.spells.util.betterscoreboard.animations.MagicGamesScoreboardTitleAnimation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Game {
    public GameState state = null;
    public ActiveWorld world;
    public final List<Player> playersInGame = new ArrayList<>();
    public final Map<Player, Location> filledCages = new HashMap<>();

    public Integer startingCountdown = 30;

    public final List<Player> alivePlayers = new ArrayList<>();
    public BukkitRunnable countdown = null;
    public final Map<Player, Integer> kills = new HashMap<>();
    public final Map<BukkitRunnable, Player> runningPlayerEvents = new HashMap<>();
    final Map<Player, BetterScoreboard> boards = new HashMap<>();

    public int nextEventIndex = 0;
    public int time = 0;
    public BukkitRunnable eventTimerRunnable = null;

    public static ItemStack blockerStack;
    public static ItemStack trashStack;

    Player firstPlace = null;
    Player secondPlace = null;
    Player thirdPlace = null;

    public Game(ActiveWorld world) {
        this.world = world;

        startAnimatedScoreboardTitle();

        // initialize blocked slot item stack
        blockerStack = Utilities.getCustomItem(
            Material.BLACK_STAINED_GLASS_PANE,
            Utilities.stringToComponentWithoutItalic("&cYou can't use this slot!")
        );

        // initialize trash slot item stack
        trashStack = Utilities.getCustomItem(
            Material.BARRIER,
            Utilities.stringToComponentWithoutItalic("&c&lTrash"),
            Arrays.asList(
                Utilities.stringToComponentWithoutItalic("&8" + Icons.RIGHT_ARROW + " &cDrop items here &4or &cright click"),
                Utilities.stringToComponentWithoutItalic("&4items to delete them.")
            )
        );
    }

    BetterScoreboard createScoreboard() {
        TextComponent title = Component.text("   ", NamedTextColor.AQUA, TextDecoration.BOLD).
                              append(Component.text("☆", NamedTextColor.DARK_AQUA)).
                              append(Component.text(" MAGIC WARS ")).
                              append(Component.text("☆", NamedTextColor.DARK_AQUA)).
                              append(Component.text("   "));
        BetterScoreboard board = ScoreboardManager.createNewBetterScoreboard(getId(), title);
        board.addStaticLine("Error, you shouldn't be seeing this.");

        return board;
    }

    void startAnimatedScoreboardTitle() {
        final Integer[] index = {0};
        final AnimationHelper animation = new MagicGamesScoreboardTitleAnimation();

        new BukkitRunnable() {
            @Override
            public void run() {
                index[0] += 1;

                if (index[0] >= animation.getLength()) {
                    index[0] = 0;
                }

                TextComponent component = animation.getLine(index[0]);

                for (BetterScoreboard board : boards.values()) {
                    board.setTitle(component);
                }
            }
        }.runTaskTimer(Spells.main, 0, 5);
    }

    public String getId() {
        return this.world.activeWorldFolder.getName();
    }

    public void setState(GameState newState) {
        if (countdown != null) countdown.cancel();

        newState.setState(this);
        state = newState;
        updateScoreboards();

        // refresh guis
//        CustomGui.reloadAll();
    }

    public void updateScoreboards() {
        for (Player player : boards.keySet()) {
            BetterScoreboard board = boards.get(player);
            state.updateScoreboard(board, this, player);
        }
    }

    public void mapReset() {
        world.map = Utilities.getRandomEntryInArray(GameManager.maps);
        world.reset();
        setState(new WaitingGameState());
    }

    public void fullReset() {
        // reset things to normal
        kills.clear();
        filledCages.clear();
        nextEventIndex = 0;
        time = 0;

        // stop event timer
        if (eventTimerRunnable != null) {
            eventTimerRunnable.cancel();
        }

        // reset game
        for (Player player : Bukkit.getOnlinePlayers()) {
            Utilities.bungeeServerSend(player, Spells.mainConfig.getString("lobby-server"));
        }

        final int[] tick = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                Spells.main.getLogger().info("Trying to reset map, attempted " + tick[0] + " times.");
                tick[0] += 1;
                if (!Bukkit.getOnlinePlayers().isEmpty() && tick[0] < 10) return;

                cancel();
                mapReset();
            }
        }.runTaskTimer(Spells.main, 0, 10);
    }

    /**
     * Run when killing a player.
     * Sets them into spectator mode and removes them from the game.
     * @param victim player being killed
     * @param killer player who killed the victim (if minion is specified, is the owner of the minion)
     * @param minion minion who killed the victim
     */
    public void kill(@NotNull Player victim, @Nullable Player killer, @Nullable Entity minion) {
        alivePlayers.remove(victim);
        kills.put(killer, kills.getOrDefault(killer, 0) + 1);
        updateScoreboards();

        victim.setGameMode(GameMode.SPECTATOR); // set victim to spectator
        Bukkit.getServer().sendMessage(GameManager.getDeathMessage(victim, killer, minion)); // send death message

        // play sounds
        if (killer != null) PlaySound.highPitchDing(killer);
        PlaySound.death(victim);

        // spawn lightning at death location
        victim.getWorld().strikeLightningEffect(victim.getLocation());
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 200, 1);
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 200, 1);

        // remove victim minions
        Minion.dismissAllMinions(victim);

        // remove victim cooldowns
        for (Material material : Material.values()) {
            if (!material.isItem()) continue;
            if (!victim.hasCooldown(material)) continue;
            victim.setCooldown(material, 0);
        }

        // drop victim items
        for (ItemStack item : victim.getInventory().getContents()) {
            if (item == null) continue;
            if (item.getItemMeta().equals(trashStack.getItemMeta())) continue;
            if (item.getItemMeta().equals(blockerStack.getItemMeta())) continue;

            victim.getWorld().dropItemNaturally(victim.getLocation(), item);
        }

        // clear victim inventory
        victim.getInventory().clear();

        // remove victim potion effects
        for (PotionEffect effect : victim.getActivePotionEffects()) {
            victim.removePotionEffect(effect.getType());
        }

        // show title for victim
        victim.showTitle(Title.title(
            Component.text("YOU DIED!", NamedTextColor.RED, TextDecoration.BOLD),
            Component.text("You are now a spectator.")
        ));

        // send open inventory actionbar
        BukkitRunnable actionBar = new BukkitRunnable() {
            @Override
            public void run() {
                victim.sendActionBar(Utilities.stringToComponent("&bOpen your inventory &3and &bclick the compass &3to return to the lobby."));
            }
        };
        actionBar.runTaskTimer(Spells.main, 0, 20);

        runningPlayerEvents.put(actionBar, victim);

        // add lobby compass to inventory
        victim.getInventory().setItem(8, GameManager.getLobbyCompass());

        // make player win / 2nd / 3rd
        if (alivePlayers.size() == 1) {
            secondPlace = victim;
            win(alivePlayers.get(0));
        } else if (alivePlayers.size() == 2) {
            thirdPlace = victim;
        }

        // refresh guis
//        CustomGui.reloadAll();
    }

    public String join(Player player) {
        if (state == null) return "Still loading game, try again in a few seconds!";
        if (!(state instanceof WaitingGameState) && !(state instanceof WaitingStartingGameState)) return "Game is currently in an unjoinable state (" + state.getClass().getSimpleName() + ")!";
        if (playersInGame.size() >= world.map.maxPlayerCount) return "All player slots in this game are taken!";

        // add player to list
        playersInGame.add(player);

        // get free cages
        List<Location> validCageLocations = new ArrayList<>();

        for (Vector vector : world.map.podLocations) {
            Location location = Utilities.centerLocationOnBlock(Utilities.vectorToBlockLocation(GameManager.activeGame.world.bukkitWorld, vector));

            if (!filledCages.containsValue(location)) {
                validCageLocations.add(location);
            }
        }

        // choose cage randomly
        Location chosenCage = validCageLocations.get(new Random().nextInt(validCageLocations.size()));

        // teleport player to chosen cage
        player.teleport(chosenCage);
        filledCages.put(player, chosenCage);

        // clear player inventory
        player.getInventory().clear();

        // give player return to lobby compass
        player.getInventory().setItem(8, GameManager.getLobbyCompassUsableInWorld());

        // send join message
        Bukkit.getServer().sendMessage(Utilities.getStatusMessage(Utilities.StatusMessageType.JOIN, Utilities.getComponentWithDefaultColorAqua(player.displayName())));

        // set to survival
        player.setGameMode(GameMode.SURVIVAL);

        // set look direction
        Vector dir = world.map.spawnFacingLocation.clone().subtract(player.getEyeLocation().toVector());
        Location loc = player.getLocation().setDirection(dir);
        player.teleport(loc);

        // show player title
        player.showTitle(Title.title(
            Utilities.stringToComponent("&3" + Icons.STAR_OUTLINE + " &b&lMAGIC WARS&3 " + Icons.STAR_OUTLINE),
            Component.empty()
        ));

        // set waiting starting
        if (playersInGame.size() >= world.map.minPlayerCount) {
            setState(new WaitingStartingGameState());
        }

        // set starting
        if (playersInGame.size() >= world.map.maxPlayerCount) {
            setState(new StartingGameState());
        }

        // create scoreboard
        BetterScoreboard board = createScoreboard();
        boards.put(player, board);
        board.applyToPlayer(player);

        // set player count on scoreboard
        updateScoreboards();

        // refresh guis
//        CustomGui.reloadAll();

        return null;
    }

    public void kickPlayer(Player player) {
        Utilities.bungeeServerSend(player, Spells.mainConfig.getString("lobby-server"));
        leave(player);
    }

    public void leave(Player player) {
        // remove player invulnerability
        OnEntityDamage.invulnerableEntities.remove(player);

        // remove from scoreboard
        if (boards.get(player) != null) {
            boards.get(player)._unregister();
            boards.remove(player);
        }

        // cancel all player events
        Map<BukkitRunnable, Player> eventMap = new HashMap<>(runningPlayerEvents);

        for (Map.Entry<BukkitRunnable, Player> entry : eventMap.entrySet()) {
            if (entry.getValue().equals(player)) {
                entry.getKey().cancel();
                runningPlayerEvents.remove(entry.getKey());
            }
        }

        // remove titles and actionbars
        player.clearTitle();
        player.sendActionBar(Component.empty());

        // send leave message
        if (firstPlace == null && isAlive(player)) {
            if (state instanceof ActiveGameState) {
                Bukkit.getServer().sendMessage(Utilities.getStatusMessage(Utilities.StatusMessageType.LEAVE, getLeaveMessage(player)));
            } else {
                Bukkit.getServer().sendMessage(Utilities.getStatusMessage(Utilities.StatusMessageType.LEAVE, Utilities.getComponentWithDefaultColorAqua(player.displayName())));
            }
        }

        // remove player from lists
        playersInGame.remove(player);
        filledCages.remove(player);
        alivePlayers.remove(player);

        // refresh guis
//        CustomGui.reloadAll();

        if (state instanceof ActiveGameState) {
            // set state so last player wins
            if (firstPlace == null) {
                if (playersInGame.size() == 1) {
                    secondPlace = player;
                    win(alivePlayers.get(0));
                } else if (playersInGame.size() == 2) {
                    thirdPlace = player;
                }
            }

            // set state if everyone leaves
//            if (playersInGame.size() == 0) {
//                mapReset();
//            }
        }

        if (state instanceof WaitingStartingGameState || state instanceof StartingGameState) {
            // set waiting if too many people leave
            if (playersInGame.size() < world.map.minPlayerCount) {
                setState(new WaitingGameState());
            }
        }

        // set player count on scoreboard
        updateScoreboards();
    }

    TextComponent getLeaveMessage(Player player) {
        String format = Utilities.getRandomEntryInArray(GameManager.leaveMessageFormats);
        format = format.replace("%player%", Utilities.componentToString(Utilities.getComponentWithDefaultColorAqua(player.displayName())));

        return Utilities.stringToComponent(format);
    }

    public void win(Player player) {
        firstPlace = player;

        // cancel event timer
        eventTimerRunnable.cancel();

        // clear player inventory and add compass
        player.getInventory().clear();
        player.getInventory().setItem(8, GameManager.getLobbyCompassUsableInWorld());

        // make player invincible
        OnEntityDamage.invulnerableEntities.add(player);

        // prevent player from picking up items
        for (Entity entity : GameManager.activeGame.world.bukkitWorld.getEntities()) {
            if (!(entity instanceof Item)) continue;
            ((Item) entity).setPickupDelay(Integer.MAX_VALUE);
        }

        // send chat leaderboard message
        Bukkit.getServer().sendMessage(Component.empty());
        Bukkit.getServer().sendMessage(Utilities.stringToComponent("&8-=-=-=-=- &b&lGAME OVER!&8 -=-=-=-=-"));
        Bukkit.getServer().sendMessage(Component.empty());
        Bukkit.getServer().sendMessage(Utilities.stringToComponent("&eWinner &8- &e").append(Utilities.getComponentWithDefaultColorAqua(player.displayName())));

        if (secondPlace != null) Bukkit.getServer().sendMessage(Utilities.stringToComponent("&62nd Place &8- &6").append(Utilities.getComponentWithDefaultColorAqua(secondPlace.displayName())));
        if (thirdPlace != null) Bukkit.getServer().sendMessage(Utilities.stringToComponent("&c3rd Place &8- &c").append(Utilities.getComponentWithDefaultColorAqua(thirdPlace.displayName())));

        Bukkit.getServer().sendMessage(Component.empty());
        Bukkit.getServer().sendMessage(Utilities.stringToComponent("&8-=-=-=-=--=-=-=-=--=-=-=-=--=-"));

        // send player title
        player.showTitle(Title.title(
            Utilities.stringToComponent("&3&lVICTORY!"),
            Utilities.stringToComponent("&7You were the last one standing!")
        ));

        // fireworks
        final int totalFireworks = new Random().nextInt(4) + 8;
        final int[] tickCounter = {0};

        BukkitRunnable victoryFireworksRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                Location spawnLocation = player.getLocation().clone().add(new Random().nextDouble() * 4 - 2, new Random().nextDouble() * 4 - 2, new Random().nextDouble() * 4 - 2);
                Firework firework = (Firework) player.getWorld().spawnEntity(spawnLocation, EntityType.FIREWORK);

                FireworkMeta meta = firework.getFireworkMeta();

                meta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(getColor(new Random().nextInt(7) + 1)).withFade(getColor(new Random().nextInt(7) + 1)).build());

                firework.setFireworkMeta(meta);

                tickCounter[0] += 1;

                if (tickCounter[0] > totalFireworks) {
                    cancel();
                }
            }
        };
        victoryFireworksRunnable.runTaskTimer(Spells.main, 0, 8);

        runningPlayerEvents.put(victoryFireworksRunnable, player);

        // reset
        new BukkitRunnable() {
            @Override
            public void run() {
                fullReset();
            }
        }.runTaskLater(Spells.main, 200);
    }

    private static Color getColor(final int i) {
        switch (i) {
            case 1:
                return Color.RED;
            case 2:
                return Color.ORANGE;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.AQUA;
            case 6:
                return Color.BLUE;
            case 7:
                return Color.PURPLE;
        }
        return Color.WHITE;
    }

    public boolean isAlive(Player player) {
        return alivePlayers.contains(player);
    }
    public boolean isJoinable() {
        return playersInGame.size() < world.map.maxPlayerCount && (state instanceof WaitingGameState || state instanceof WaitingStartingGameState || state instanceof StartingGameState);
    }
}

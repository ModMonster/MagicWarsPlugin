package ca.modmonster.spells;

import ca.modmonster.spells.command.enchantments.EnchantmentsCommand;
import ca.modmonster.spells.command.magicwars.MagicWarsCommand;
import ca.modmonster.spells.command.spells.SpellsCommand;
import ca.modmonster.spells.events.*;
import ca.modmonster.spells.game.GameManager;
import ca.modmonster.spells.game.LootChest;
import ca.modmonster.spells.item.enchantment.EnchantmentManager;
import ca.modmonster.spells.util.InvisibilityManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

// TODO: make system to record who killed who when killed with spells.
// TODO: prevent players from sleeping in beds
// TODO: add clickable message in chat when killed to play again
// TODO: minions see you when invisible
// TODO: name above head not show rank
// TODO: you can loot chests while in victory state
// TODO: nerf minions
// TODO: disable pushing in lobby
// TODO: disable going to nether + re add portal in lobby
// TODO: add command to manually end game
// TODO: arrows dont remove from body when dying
// TODO: body arrows arent invisible
// TODO: only cages with player in are disappearing
// TODO: you can send wither ball and change blocks outside of border
// TODO: game end event
// TODO: scoreboard flicker
// TODO: show player alive status in tab list
// TODO: prevent teleporting to dead players in spectator menu
// TODO: dont show dead players in world when spectating

// TO TEST

public final class Spells extends JavaPlugin {
    public static YamlConfiguration mapConfig;
    public static YamlConfiguration mainConfig;
    public static Spells main;

    @Override
    public void onEnable() {
        main = this;

        generateConfig();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // register tick loop
        new OnTick().runTaskTimer(this, 0, 1);

        // register events
        getServer().getPluginManager().registerEvents(new OnPlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamage(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDeath(), this);
        getServer().getPluginManager().registerEvents(new OnEntityTarget(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamageByEntity(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnLeaveGame(), this);
        getServer().getPluginManager().registerEvents(new OnMobSpawn(), this);
        getServer().getPluginManager().registerEvents(new OnHungerChange(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClose(), this);
        getServer().getPluginManager().registerEvents(new OnEntityExplode(), this);
        getServer().getPluginManager().registerEvents(new OnProjectileHit(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerInteractWithEntity(), this);
        getServer().getPluginManager().registerEvents(new OnBlockFade(), this);
        getServer().getPluginManager().registerEvents(new OnJoinGame(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerCraft(), this);
        getServer().getPluginManager().registerEvents(new OnRecipeDiscover(), this);
        getServer().getPluginManager().registerEvents(new OnAdvancementGet(), this);
        getServer().getPluginManager().registerEvents(new OnXpGain(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        getServer().getPluginManager().registerEvents(new OnDropItem(), this);

        // register commands
        this.getCommand("spell").setExecutor(new SpellsCommand());

        this.getCommand("magicwarsenchants").setExecutor(new EnchantmentsCommand());
        this.getCommand("mwen").setExecutor(new EnchantmentsCommand());

        this.getCommand("magicwars").setExecutor(new MagicWarsCommand());
        this.getCommand("mw").setExecutor(new MagicWarsCommand());

        // register enchantments
        EnchantmentManager.init();

        // register invisibility listeners
        InvisibilityManager.init();
    }

    void generateConfig() {
        getDataFolder().mkdirs();

        // create folder for magic wars maps
        File gameMapsFolder = new File(getDataFolder(), "maps");
        if (!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs();
        }

        // create map config file
        File mapConfigFile = new File(getDataFolder(), "maps.yml");
        if (!mapConfigFile.exists()) {
            mapConfigFile.getParentFile().mkdirs();
            saveResource("maps.yml", false);
        }

        // create config file
        File mainConfigFile = new File(getDataFolder(), "config.yml");
        if (!mainConfigFile.exists()) {
            mainConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        // load map config
        mapConfig = new YamlConfiguration();
        try {
            mapConfig.load(mapConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // load death config
        mainConfig = new YamlConfiguration();
        try {
            mainConfig.load(mainConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        ConfigurationSection deathSection = mainConfig.getConfigurationSection("death-messages");

        GameManager.deathMessageFormatByPlayer = deathSection.getString("killed-template");
        GameManager.deathMessageFormats = deathSection.getStringList("death-templates");
        GameManager.deathMessageFormatsFire = deathSection.getStringList("death-templates-fire");
        GameManager.deathMessageFormatsFall = deathSection.getStringList("death-templates-fall");
        GameManager.deathMessageFormatsDrown = deathSection.getStringList("death-templates-drown");
        GameManager.deathMessageFormatsExplode = deathSection.getStringList("death-templates-explode");
        GameManager.deathMessageFormatsLightning = deathSection.getStringList("death-templates-lightning");
        GameManager.deathMessageFormatsWither = deathSection.getStringList("death-templates-wither");
        GameManager.deathMessageFormatByMinion = deathSection.getString("killed-template-minion");
        GameManager.leaveMessageFormats = deathSection.getStringList("leave-templates");
        GameManager.adjectives = deathSection.getStringList("adjectives");
        GameManager.verbs = deathSection.getStringList("verbs");

        GameManager.init();
        LootChest.init();
    }

    @Override
    public void onDisable() {
        GameManager.activeGame.world.unload();
        super.onDisable();
    }
}

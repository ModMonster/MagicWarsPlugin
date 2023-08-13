package ca.modmonster.spells.util.betterscoreboard;

import java.util.*;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class BetterScoreboard {
    final Stack<String> avTeamNames;
    final Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
    final Objective obj;
    final String name;
    TextComponent title;
    final Map<String, Team> scoreboardTeams = new HashMap<>();
    int lineIndex = 15;
    final List<String> lines = new ArrayList<>();

    public Scoreboard getBukkitScoreboard() {
        return this.scoreboard;
    }

    public BetterScoreboard(String name, TextComponent title) {
        this.obj = this.scoreboard.registerNewObjective(name, Criteria.DUMMY, title);
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.name = name;
        this.title = title;
        this.avTeamNames = new Stack<String>() {
            {
                this.push(ChatColor.BLACK.toString() + ChatColor.WHITE);
                this.push(ChatColor.BLACK.toString() + ChatColor.YELLOW);
                this.push(ChatColor.BLACK.toString() + ChatColor.GREEN);
                this.push(ChatColor.BLACK.toString() + ChatColor.RED);
                this.push(ChatColor.BLACK.toString() + ChatColor.BLUE);
                this.push(ChatColor.BLACK.toString() + ChatColor.AQUA);
                this.push(ChatColor.BLACK.toString() + ChatColor.LIGHT_PURPLE);
                this.push(ChatColor.BLACK.toString() + ChatColor.GOLD);
                this.push(ChatColor.YELLOW.toString() + ChatColor.WHITE);
                this.push(ChatColor.GREEN.toString() + ChatColor.WHITE);
                this.push(ChatColor.RED.toString() + ChatColor.WHITE);
                this.push(ChatColor.BLUE.toString() + ChatColor.WHITE);
                this.push(ChatColor.AQUA.toString() + ChatColor.WHITE);
                this.push(ChatColor.LIGHT_PURPLE.toString() + ChatColor.WHITE);
                this.push(ChatColor.GOLD.toString() + ChatColor.WHITE);
            }
        };
    }

    public void setTitle(TextComponent title) {
        this.title = title;
        this.obj.displayName(title);
    }

    public void applyToPlayer(Player p) {
        p.setScoreboard(this.scoreboard);
    }

    public void addStaticLine(String content) {
        if (this.lineIndex != -1) {
            String line = ChatColor.translateAlternateColorCodes('&', content);

            this.obj.getScore(line).setScore(this.lineIndex--);
            this.lines.add(line);
        }
    }

    public void resetLines() {
        for (String line : lines) {
            this.obj.getScore(line).resetScore();
        }

        lines.clear();
        lineIndex = 15;
    }

    public void _unregister() {
        this.obj.unregister();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getScoreboard() == this.scoreboard) {
                p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
        }

    }

    public String getName() {
        return this.name;
    }

    public TextComponent getTitle() {
        return this.title;
    }
}

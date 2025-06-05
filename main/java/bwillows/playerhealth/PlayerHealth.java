package bwillows.playerhealth;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public final class PlayerHealth extends JavaPlugin implements Listener {

    Plugin plugin;
    ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        plugin = this;
        scoreboardManager = Bukkit.getScoreboardManager();
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void startUpdatingScoreboard(Player player) {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Scoreboard board = player.getScoreboard();

            Objective healthDisplay = board.getObjective("healthDisplay");
            if(healthDisplay == null) {
                healthDisplay = board.registerNewObjective("healthDisplay", "placeholder");
                healthDisplay.setDisplaySlot(DisplaySlot.BELOW_NAME);
                healthDisplay.setDisplayName(ChatColor.RED + "❤");
            }
            Score score = healthDisplay.getScore(player.getName());
            score.setScore((int) player.getHealth());

        }, 0L, 20L); // 20L = 1 second
    }

    public void createScoreboard(Player player) {
        Scoreboard board = scoreboardManager.getNewScoreboard();
        player.setScoreboard(board);
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createScoreboard(player);
        startUpdatingScoreboard(player);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

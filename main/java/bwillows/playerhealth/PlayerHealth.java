package bwillows.playerhealth;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public final class PlayerHealth extends JavaPlugin implements Listener {

    private Plugin plugin;
    private ScoreboardManager scoreboardManager;
    private Scoreboard globalBoard; // Global scoreboard for all players

    @Override
    public void onEnable() {
        plugin = this;
        scoreboardManager = Bukkit.getScoreboardManager();
        // Create a global scoreboard to be used for all players
        globalBoard = scoreboardManager.getNewScoreboard();
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, plugin);

        Bukkit.getScheduler().runTaskTimer(this, this::updateAllPlayersHealth, 0L, 20L);
    }

    // Method to update health for a specific player
    public void updateHealth(Player player) {
        Scoreboard board = player.getScoreboard();

        // Always ensure the player has the global scoreboard
        if (board != globalBoard) {
            player.setScoreboard(globalBoard);
        }

        Objective healthDisplay = globalBoard.getObjective("healthDisplay");
        if (healthDisplay == null) {
            healthDisplay = globalBoard.registerNewObjective("healthDisplay", "health");
            healthDisplay.setDisplaySlot(DisplaySlot.BELOW_NAME);
            healthDisplay.setDisplayName(ChatColor.RED + "❤");
        }
        // Make sure the score is updated for the player
        Score score = healthDisplay.getScore(player.getName());
        score.setScore((int) player.getHealth());
    }

    // Method to update health for all players
    private void updateAllPlayersHealth() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateHealth(player);
        }
    }

    // Create a scoreboard for a player (but we always set them to the global scoreboard)
    public void createScoreboard(Player player) {
        player.setScoreboard(globalBoard); // Set all players to use the global scoreboard
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createScoreboard(player);
        // Update health immediately when the player joins
        updateHealth(player);
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            updateHealth(player);  // Update health when the player takes damage
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

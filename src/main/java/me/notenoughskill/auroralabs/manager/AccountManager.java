package me.notenoughskill.auroralabs.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.netty.util.concurrent.CompleteFuture;
import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import me.notenoughskill.auroralabs.database.PlayerVials;
import me.notenoughskill.auroralabs.database.SQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AccountManager {
    private Map<String, PlayerVials> playerVials = new HashMap<>();

    private final AuroraLabsPlugin plugin;

    public AccountManager(final AuroraLabsPlugin plugin) {
        this.playerVials = new HashMap<String, PlayerVials>();
        this.plugin = plugin;
    }

    @Nullable
    public PlayerVials getPlayerData(String uuid) {
        return this.playerVials.get(uuid);
    }

    public void createPlayerData(@NotNull final String uuid, final int vialAmount) {
        final PlayerVials vials = new PlayerVials(uuid);
        vials.setVials(vialAmount);
        final PlayerVials[] playerVials = new PlayerVials[1];
        CompletableFuture.runAsync(() -> this.savePlayerData(vials)).thenAccept(result -> Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> playerVials[0] = this.playerVials.put(uuid, vials)));
    }

    public void savePlayerData(PlayerVials vials) {
        SQL sql = (SQL) this.plugin.getDatabase();
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, () -> {
            try {
                Connection connection = sql.getNewConnection();
                String command = "SELECT Vials FROM auroralabs WHERE UUID='" + vials.getUUID() + "'";
                PreparedStatement statement = connection.prepareStatement(command);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    sql.update(connection, vials.getUUID(), String.valueOf(vials.getVials()));
                } else {
                    sql.insert(connection, vials.getUUID(), String.valueOf(vials.getVials()));
                }
                rs.close();
                statement.close();
                connection.close();
            } catch (SQLException exception) {
                System.out.println("[Aurora Labs] Error saving player account data.");
                exception.printStackTrace();
            }
        });
    }

    public void loadAllPlayerData() {
        SQL sql = (SQL) this.plugin.getDatabase();
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, () -> {
            try (Connection connection = sql.getNewConnection()) {
                String command = "SELECT * FROM auroralabs";
                try (PreparedStatement statement = connection.prepareStatement(command); ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String uuid = rs.getString("UUID");
                        String vials = rs.getString("Vials");
                        PlayerVials vials = new PlayerVials(uuid, Integer.parseInt(vials));
                        this.playerVials.put(uuid, vials);
                    }
                    System.out.println("[Aurora Labs] Successfully loaded all saved accounts!");
                }
            } catch (SQLException e) {
                System.out.println("[Aurora Labs] Error loading database player accounts!");
                Bukkit.getPluginManager().disablePlugin((Plugin)this.plugin);
                e.printStackTrace();
            }
        });
    }
}

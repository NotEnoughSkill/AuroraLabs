package me.notenoughskill.auroralabs;

import com.earth2me.essentials.Essentials;
import me.notenoughskill.auroralabs.commands.CommandAdmin;
import me.notenoughskill.auroralabs.commands.CommandMain;
import me.notenoughskill.auroralabs.database.SQL;
import me.notenoughskill.auroralabs.listeners.LabsSanityListener;
import me.notenoughskill.auroralabs.tasks.DateWatcherTask;
import me.notenoughskill.auroralabs.tasks.LabsEventTask;
import me.notenoughskill.auroralabs.tasks.LabsSanityTask;
import me.notenoughskill.auroralabs.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public final class AuroraLabsPlugin extends JavaPlugin {
    private static AuroraLabsPlugin instance;

    private World world;

    private Location spawn;

    private LabsEventTask labs;

    private Essentials ess3;

    private SQL database;

    @Override
    public void onEnable() {
        createDatabaseFile();
        instance = this;
        this.ess3 = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
        registerCommands();
        registerListeners();
        registerTasks();
        loadConfig();
        this.database = new SQL(this);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private void registerCommands() {
        getCommand("labs").setExecutor((CommandExecutor)new CommandMain());
        getCommand("alabs").setExecutor((CommandExecutor)new CommandAdmin());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents((Listener)new LabsSanityListener(), (Plugin)this);
    }

    private void registerTasks() {
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, (Runnable)new DateWatcherTask(), 1200L, 600L);
        Bukkit.getScheduler().runTaskTimer((Plugin)this, (Runnable)new LabsSanityTask(), 20L, 20L);
    }

    private void loadConfig()
    {
        registerConfigData();
    }

    public void registerConfigData() {
        this.world = Bukkit.getWorld(instance.getConfig().getString("world"));
        if (instance.getConfig().getString("playerSpawn") != null) {
            this.spawn = LocationUtils.convertStringToLocation(instance.getConfig().getString("playerSpawn"));
        }
    }

    public void createDatabaseFile() {
        File pluginFolder = new File("plugins/Auroralabs");
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
        File dataFolder = new File(getDataFolder(), "database.db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
                System.out.println("[Aurora Labs] Successfully creating database file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static AuroraLabsPlugin getInstance() {
        return instance;
    }

    public LabsEventTask getLabs() {
        return this.labs;
    }

    public World getWorld() {
        return this.world;
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public void setLabs(LabsEventTask labs) {
        this.labs = labs;
    }
}

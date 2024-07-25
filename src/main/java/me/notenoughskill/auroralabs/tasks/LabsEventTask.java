package me.notenoughskill.auroralabs.tasks;

import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class LabsEventTask implements Runnable{

    private int runtime;

    private boolean running;

    private int taskId;

    @Override
    public void run() {
        if (this.running) {
            this.runtime++;
        }
    }

    public List<Player> getLabsPlayers() {
        return AuroraLabsPlugin.getInstance().getWorld().getPlayers();
    }

    public int getRuntime() {
        return this.runtime;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return this.taskId;
    }
}

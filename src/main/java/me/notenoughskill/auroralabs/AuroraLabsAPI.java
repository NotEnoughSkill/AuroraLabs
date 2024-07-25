package me.notenoughskill.auroralabs;

import com.google.common.collect.Maps;
import me.notenoughskill.auroralabs.tasks.LabsEventTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class AuroraLabsAPI {
    public static Map<UUID, Integer> playerLivesUsed = Maps.newHashMap();

    public static boolean isLabsActive() {
        return AuroraLabsPlugin.getInstance().getLabs() != null;
    }

    public static boolean canTeleportToLabs() {
        return (isLabsActive());
    }

    public static void resetLabs() {
        playerLivesUsed.clear();
        if (AuroraLabsPlugin.getInstance().getLabs() != null) {
            int oldTaskId = AuroraLabsPlugin.getInstance().getLabs().getTaskId();
            if (oldTaskId != -1) {
                Bukkit.getLogger().info("Resetting Labs, stopping task ID #" + oldTaskId);
                Bukkit.getScheduler().cancelTask(oldTaskId);
            }
        }
        LabsEventTask LET = new LabsEventTask();
        LET.setTaskId(Bukkit.getScheduler().runTaskTimer((Plugin)AuroraLabsPlugin.getInstance(), (Runnable)LET, 20L, 20L).getTaskId());
        AuroraLabsPlugin.getInstance().setLabs(LET);
        Bukkit.getLogger().info("Started new Labs with task ID #" + LET.getTaskId());
    }
}

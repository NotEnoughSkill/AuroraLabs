package me.notenoughskill.auroralabs.tasks;

import me.notenoughskill.auroralabs.AuroraLabsAPI;
import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class LabsSanityTask implements Runnable{
    @Override
    public void run() {
        if (AuroraLabsPlugin.getInstance().getWorld() != null && !AuroraLabsAPI.isLabsActive()) {
            for (Player p : AuroraLabsPlugin.getInstance().getWorld().getPlayers()) {
                if ( !p.isOp() && p.getGameMode() != GameMode.CREATIVE) {
                    p.teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                }
            }
        }
    }
}

package me.notenoughskill.auroralabs.commands;

import me.notenoughskill.auroralabs.AuroraLabsAPI;
import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import me.notenoughskill.auroralabs.database.PlayerVials;
import me.notenoughskill.auroralabs.gui.LabsGui;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CommandMain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            (new LabsGui()).getInventory().open((Player)sender);
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                if (args[0].equalsIgnoreCase("warp")) {
                    if (AuroraLabsAPI.isLabsActive()) {
                        if (AuroraLabsAPI.canTeleportToLabs()) {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                if (AuroraLabsPlugin.getInstance().getSpawn() != null) {
                                    p.teleport(AuroraLabsPlugin.getInstance().getSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                                } else {
                                    p.sendMessage(ChatColor.RED + "An internal server error has occured while attempting to locate Labs spawn.");
                                }
                            }
                        } else {
                            sender.sendMessage(ChatColor.GRAY + "You can no longer teleport in to this Labs event.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "No Labs events are currently running.");
                        sender.sendMessage(ChatColor.GRAY + "Use /labs to find the next scheduled Labs.");
                    }
                }
                if (args[0].equalsIgnoreCase("balance")) {
                    Player p = (Player)sender;
                    PlayerVials vials = AuroraLabsPlugin.getInstance().
                }
            }
        }
        return true;
    }
}

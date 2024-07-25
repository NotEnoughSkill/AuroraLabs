package me.notenoughskill.auroralabs.commands;

import com.decimatemc.decimatecommons.util.LocationUtil;
import me.notenoughskill.auroralabs.AuroraLabsAPI;
import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import me.notenoughskill.auroralabs.items.LabsKey;
import me.notenoughskill.auroralabs.utils.ItemUtils;
import me.notenoughskill.auroralabs.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdmin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "");
            sender.sendMessage(ChatColor.RED + "Aurora Labs Help");
            sender.sendMessage(ChatColor.RED + "/alabs start - Start a Labs event");
            sender.sendMessage(ChatColor.RED + "/alabs stop - Stop a Labs event");
            sender.sendMessage(ChatColor.RED + "/alabs setspawn - Set the spawnpoint for the Labs event");
            sender.sendMessage(ChatColor.RED + "/alabs givekey {player}- Give a player a key");
            sender.sendMessage(ChatColor.RED + "");
        }
        String param1 = args[0];
        String param2 = args[1];
        if (param1.equalsIgnoreCase("start")) {
            AuroraLabsAPI.resetLabs();
            sender.sendMessage("Labs has been force started.");
        }
        if (param1.equalsIgnoreCase("stop")) {
            if (AuroraLabsPlugin.getInstance().getLabs() == null) {
                sender.sendMessage(ChatColor.RED + "No Labs events are currently running!");
                return true;
            }
        }
        Player p2 = Bukkit.getPlayer(args[1]);
        if (param1.equalsIgnoreCase("givekey")) {
            if (p2 == null) {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Unable to find player: " + args[1]);
                return true;
            }
            if (param2.equals(p2.getName())) {
                ItemUtils.giveItemToPlayer(p2, LabsKey.getLabsKeyItem());
                sender.sendMessage("Added a Labs key to " + p2.getName() + "'s Inventory!");
            } else {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Unable to find player: " + args[1]);
                return true;
            }
        }
        if (param1.equalsIgnoreCase("setspawn")) {
            Location newSpawn = ((Player)sender).getLocation();
            AuroraLabsPlugin.getInstance().setSpawn(newSpawn);
            AuroraLabsPlugin.getInstance().getConfig().set("playerSpawn", LocationUtils.convertLocationToString(newSpawn));
            AuroraLabsPlugin.getInstance().saveConfig();
            sender.sendMessage(ChatColor.RED + "Labs Spawnpoint set to your location!");
            return true;
        }
        return true;
    }
}

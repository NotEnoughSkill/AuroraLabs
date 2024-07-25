package me.notenoughskill.auroralabs.utils;

import java.text.DecimalFormat;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LocationUtils {
    public static String convertLocationToString(Location l) {
        DecimalFormat df = new DecimalFormat("#.####");
        return l.getWorld().getName() + "," + df.format(l.getX()) + "," + df.format(l.getY()) + "," + df.format(l.getZ()) + ":" + df.format(l.getYaw()) + "$" + df.format(l.getPitch());
    }

    public static Location convertStringToLocation(String location_string) {
        return convertStringToLocation(location_string, false);
    }

    public static Location convertStringToLocation(String location_string, boolean letWorldBeNull) {
        if (location_string == null)
            return null;
        if ((location_string.split(",")).length == 4) {
            Location loc;
            String world_name = location_string.split(",")[0];
            double x = Double.parseDouble(location_string.split(",")[1]);
            double y = Double.parseDouble(location_string.split(",")[2]);
            double z = Double.parseDouble(location_string.split(",")[3].split(":")[0]);
            float yaw = Float.parseFloat(location_string.substring(location_string.indexOf(":") + 1, location_string.indexOf("$")));
            float pitch = Float.parseFloat(location_string.substring(location_string.indexOf("$") + 1, location_string.length()));
            if (letWorldBeNull) {
                loc = new Location(Bukkit.getWorld(world_name), x, y, z, yaw, pitch);
            } else if (Bukkit.getWorld(world_name) != null) {
                loc = new Location(Bukkit.getWorld(world_name), x, y, z, yaw, pitch);
            } else {
                Location l_spawn = ((World)Bukkit.getServer().getWorlds().get(0)).getSpawnLocation();
                l_spawn.setPitch(2.0F);
                l_spawn.setYaw(-179.0F);
                loc = l_spawn;
            }
            return loc;
        }
        return null;
    }

    public static Location getNearbyLocation(Location l, int radius, int minDistance, int y_boost) {
        Random rand = new Random();
        Location rand_loc = l.clone();
        rand_loc.add(((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + minDistance)), 0.0D, ((rand.nextBoolean() ? 1 : -1) * (rand.nextInt(radius) + minDistance)));
        rand_loc.add(0.5D, y_boost, 0.5D);
        Block b = rand_loc.getWorld().getHighestBlockAt(rand_loc).getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock();
        boolean badMaterial = (b.isLiquid() || b.getType() == Material.STATIONARY_WATER || b.getType() == Material.WATER || b.getType() == Material.SNOW);
        if (badMaterial)
            return getNearbyLocation(l, radius, minDistance, y_boost);
        while ((rand_loc.getBlock().getType() != Material.AIR || rand_loc.getBlock().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR) && rand_loc.getY() < 255.0D)
            rand_loc = rand_loc.add(0.0D, 1.0D, 0.0D);
        return rand_loc;
    }
}

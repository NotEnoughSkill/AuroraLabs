package me.notenoughskill.auroralabs.utils;

import org.bukkit.entity.Player;

public class ExpUtils {
    private static int getTotalExperience(int level) {
        int xp = 0;
        if (level >= 0 && level <= 15) {
            xp = (int)Math.round(Math.pow(level, 2.0D) + (6 * level));
        } else if (level > 15 && level <= 30) {
            xp = (int)Math.round(2.5D * Math.pow(level, 2.0D) - 40.5D * level + 360.0D);
        } else if (level > 30) {
            xp = (int)Math.round(4.5D * Math.pow(level, 2.0D) - 162.5D * level + 2220.0D);
        }
        return xp;
    }

    public static int getTotalExperience(Player player) {
        return Math.round(player.getExp() * player.getExpToLevel()) + getTotalExperience(player.getLevel());
    }

    public static void setTotalExperience(Player player, int amount) {
        float a = 0.0F;
        float b = 0.0F;
        float c = -amount;
        if (amount > getTotalExperience(0) && amount <= getTotalExperience(15)) {
            a = 1.0F;
            b = 6.0F;
        } else if (amount > getTotalExperience(15) && amount <= getTotalExperience(30)) {
            a = 2.5F;
            b = -40.5F;
            c += 360.0F;
        } else if (amount > getTotalExperience(30)) {
            a = 4.5F;
            b = -162.5F;
            c += 2220.0F;
        }
        int level = (int)Math.floor((-b + Math.sqrt(Math.pow(b, 2.0D) - (4.0F * a * c))) / (2.0F * a));
        int xp = amount - getTotalExperience(level);
        player.setLevel(level);
        player.setExp(0.0F);
        player.giveExp(xp);
    }
}

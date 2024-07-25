package me.notenoughskill.auroralabs.items;

import me.notenoughskill.auroralabs.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LabsKey {
    public static LabsKey instance;

    public static ItemStack getLabsKeyItem()
    {
        ItemStack i = ItemUtils.buildItem(Material.PRISMARINE_SHARD, (short)0,
                ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + "Labs Key", new String[] { ChatColor.GRAY + "Place in yourinventory",
                ChatColor.GRAY + "and do /labs to access Labs!"});
        return i;
    }

    public static boolean isLabsKeyItem(ItemStack i) {
        return (i != null) && i.getType() == Material.PRISMARINE_SHARD && i.hasItemMeta() && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).equals(ChatColor.GRAY + "Right-Click this key to unlock");
    }
}

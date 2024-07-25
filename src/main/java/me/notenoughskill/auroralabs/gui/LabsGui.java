package me.notenoughskill.auroralabs.gui;

import com.decimatemc.decimatecommons.DecimateCommons;
import com.decimatemc.decimatecommons.inventory.ClickableItem;
import com.decimatemc.decimatecommons.inventory.SmartInventory;
import com.decimatemc.decimatecommons.inventory.content.InventoryContents;
import com.decimatemc.decimatecommons.inventory.content.InventoryProvider;
import com.decimatemc.decimatecommons.util.CommonsUtil;
import me.notenoughskill.auroralabs.AuroraLabsAPI;
import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import me.notenoughskill.auroralabs.items.LabsKey;
import me.notenoughskill.auroralabs.tasks.DateWatcherTask;
import me.notenoughskill.auroralabs.tasks.LabsEventTask;
import me.notenoughskill.auroralabs.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LabsGui implements InventoryProvider {
    private final SmartInventory inventory = SmartInventory.builder().title("Aurora Labs").size(1, 9).type(InventoryType.CHEST).provider(this).manager(DecimateCommons.getInstance().getInventoryManager()).id(UUID.randomUUID().toString()).build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&7", 1, 7, new String[0])));
        contents.set(8, ClickableItem.empty(CommonsUtil.createItem(Material.BOOK, "&3&l&nAurora Labs", new String[] {
                "&7Aurora Labs is a end game", "&7grindy yet high rewarding PvE & PvP activity.", "", "&7Explore the uncharted lands of Labs and try to", "&7defeat &3&lcustom enemies and bosses and gather vials", "&7which can be used to purchase high end exclusive gear and rewards.",
                "&7Aurora Lab's doors will unlock ever &3&n8 hours&7. While", "&7unlocked you will have &a1 hour &7to kill as many", "&7enemies as possible and gather all of the viles your pockets can hold.", "",
                "&7Upon dying in the Aurora Labs, you", "&7will &4&lnot &7be able to get back inside", "&7until the next event, so GoodLuck!"
        })));
        LabsEventTask LET = AuroraLabsPlugin.getInstance().getLabs();
        List<String> lore = new ArrayList<>();
        lore.add("");
        if (player.getInventory().contains(LabsKey.getLabsKeyItem())) {
            lore.add(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.GRAY + "Use " + ChatColor.DARK_AQUA + ChatColor.UNDERLINE + "/warp labs" + ChatColor.GRAY + " to enter labs!");
        } else {
            lore.add(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.GRAY + "You need a key to access Labs!");
        }
        lore.add("");
        //lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Players: " + ChatColor.AQUA + LET.getLabsPlayers().size());
        if (player.getInventory().contains(LabsKey.getLabsKeyItem())) {
            contents.set(4, ClickableItem.of(CommonsUtil.createItem(Material.PRISMARINE_SHARD, "&3&lLabs Information", lore), event -> Bukkit.dispatchCommand((CommandSender)player, "warp lab")));
        } else {
            contents.set(4, ClickableItem.empty(CommonsUtil.createItem(Material.PRISMARINE_CRYSTALS, "&3&lLabs Information", lore)));
        }
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

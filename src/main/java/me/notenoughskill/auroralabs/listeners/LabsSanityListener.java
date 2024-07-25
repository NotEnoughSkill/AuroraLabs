package me.notenoughskill.auroralabs.listeners;

import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.mysql.jdbc.TimeUtil;
import me.notenoughskill.auroralabs.AuroraLabsAPI;
import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import me.notenoughskill.auroralabs.items.LabsKey;
import me.notenoughskill.auroralabs.utils.ExpUtils;
import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.craftbukkit.libs.com.google.common.cache.Cache;
import org.bukkit.craftbukkit.libs.com.google.common.cache.CacheBuilder;
import org.bukkit.craftbukkit.libs.com.google.common.cache.CacheLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LabsSanityListener implements Listener {
    private Map<UUID, Integer> storeEXP = new HashMap<>();

    private Cache<UUID, Long> recentLabsDeaths;

    public LabsSanityListener() {
        this.recentLabsDeaths = CacheBuilder.newBuilder().expireAfterWrite(8L, TimeUnit.MINUTES).build(new CacheLoader<UUID, Long>() {
            @Override
            public Long load(UUID uuid) {
                return Long.valueOf(System.currentTimeMillis());
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (AuroraLabsAPI.canTeleportToLabs()) {
            final Player p = e.getPlayer();
            Bukkit.getScheduler().runTaskLater((Plugin) AuroraLabsPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.sendMessage("");
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l*&b&l*&3&l* &b&lYOU HAVE 1 HOUR LEFT! &3&l*&b&l*&3&l*"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fType &3/labs &fto view more event information!"));
                    p.sendMessage("");
                }
            }, 25L);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("labsWorld")) {
            if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                e.setUseItemInHand(Event.Result.DENY);
                e.setUseInteractedBlock(Event.Result.DENY);
                e.getPlayer().setItemInHand((ItemStack)null);
                e.getPlayer().sendMessage("" + ChatColor.RED + ChatColor.BOLD + "(!) " + ChatColor.RED + "This is not allowed here!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/warp lab")) {
            e.setMessage("/labs warp");
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase("labsWorld")) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLabsRunningCheck(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player)e.getWhoClicked();
        if (e.getInventory() != null && e.getInventory().getTitle().equalsIgnoreCase("Aurora Labs") && item != null && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3&lLabs Information")) && !AuroraLabsAPI.canTeleportToLabs()) {
            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You need a Labs Key to access this area!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPreDeathEvent(PrePlayerDeathEvent e) {
        Player player = e.getPlayer();
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("labsWorld") && this.storeEXP != null && !this.storeEXP.containsKey(e.getPlayer().getUniqueId())) {
            this.storeEXP.put(e.getPlayer().getUniqueId(), Integer.valueOf(ExpUtils.getTotalExperience(player)));
        }
    }

    @EventHandler
    public void onRespawnFromLabsDeath(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("labsWorld") && this.storeEXP != null && this.storeEXP.containsKey(e.getPlayer().getUniqueId())) {
            ExpUtils.setTotalExperience(player, (Integer)this.storeEXP.get(player.getUniqueId()).intValue());
            this.storeEXP.remove(e.getPlayer().getUniqueId());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l(!) &aYou have died in Labs, all of your inventory has been restored!"));
        }
        if (e.getPlayer().getWorld().getName().contains("labs")) {
            this.recentLabsDeaths.apply(e.getPlayer().getUniqueId());
            AuroraLabsAPI.playerLivesUsed.put(e.getPlayer().getUniqueId(), Integer.valueOf(((Integer)AuroraLabsAPI.playerLivesUsed.getOrDefault(e.getPlayer().getUniqueId(), Integer.valueOf(0))).intValue() + 1));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void OnPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getPlayer().getInventory().contains(LabsKey.getLabsKeyItem())) {
            e.getPlayer().getInventory().removeItem(LabsKey.getLabsKeyItem());
        }
        if (e.getFrom() != null && e.getTo() != null && e.getTo().getWorld().getName().contains("labs") && !e.getTo().getWorld().getName().equals(e.getFrom().getWorld().getName())) {
            if (AuroraLabsAPI.playerLivesUsed.containsKey(e.getPlayer().getUniqueId()) && (Integer)AuroraLabsAPI.playerLivesUsed.get(e.getPlayer().getUniqueId()).intValue() == 1) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &cYou have already died therefore you cannot return to Labs!"));
                e.setCancelled(true);
                if (!e.getPlayer().isOp()) {
                    return;
                }
            }
            Long lastDeath = (Long)this.recentLabsDeaths.asMap().get(e.getPlayer().getUniqueId());
            if (lastDeath != null) {
                long timeToLookFor = getTimeTillReturn(e.getPlayer());
                long timeSinceDeath = System.currentTimeMillis() - lastDeath.longValue();
                if (timeSinceDeath < timeToLookFor) {
                    int secondsLeft = (int)((timeToLookFor - timeSinceDeath) / 1000L);
                    if (secondsLeft > 0) {
                        if (!e.getPlayer().isOp()) {
                            e.setCancelled(true);
                        }
                        e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot enter Labs for another " + ChatColor.RED.toString() + ChatColor.UNDERLINE.toString() + TimeUtils.formatSeconds(secondsLeft) + ChatColor.RED + " due to your recent death!");
                    }
                }
            }
        }
    }

    private long getTimeTillReturn(Player player) {
        return player.hasPermission("aurorapvp.aurora") ? TimeUnit.MINUTES.toMillis(5L) : TimeUnit.MINUTES.toMillis(8L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleportMonitor(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (e.getTo() != null && e.getFrom() != null && e.getTo().getWorld().getName().contains("labs") && !e.getFrom().getWorld().getName().equals(e.getTo().getWorld().getName())) {
            long minutesToReturn = getTimeTillReturn(e.getPlayer()) / 1000L;
            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Once you die, you will not be able to return to Labs for " + ChatColor.RED.toString() + ChatColor.BOLD.toString() + (minutesToReturn / 60L) + ChatColor.RED + " minutes!");
        }
    }

}

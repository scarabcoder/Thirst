package com.gamerking195.dev.thirst.listener;

import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstManager;
import com.gamerking195.dev.thirst.util.UtilUpdater;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PlayerJoinLeaveListener 
implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
        Player player = event.getPlayer();

		ThirstManager.getThirst().playerJoin(player);

		if (player.isOp() || player.hasPermission("thirst.command.update") || player.hasPermission("thirst.*")) {
		    if (Thirst.getInstance().getYAMLConfig().enableUpdater && UtilUpdater.getInstance().isUpdateAvailable()) {
		        new BukkitRunnable() {
                    @Override
                    public void run() {
                        String currentVersion = Thirst.getInstance().getDescription().getVersion();
                        String newVersion = UtilUpdater.getInstance().getLatestVersion();
                        List<String> testedVersions = UtilUpdater.getInstance().getTestedVersions();
                        String mcVersion = Bukkit.getServer().getClass().getPackage().getName();
                        mcVersion = mcVersion.substring(mcVersion.lastIndexOf(".") + 1);
                        mcVersion = mcVersion.substring(1, mcVersion.length()-3);
                        mcVersion = mcVersion.replace("_", ".");

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m------------------------------"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&lThirst &fV" + currentVersion + " &bby &f" + Thirst.getInstance().getDescription().getAuthors()));
                        player.sendMessage("");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bThere is a Thirst update available!"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bVersion: &f" + newVersion));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bUpdates: \n" + UtilUpdater.getInstance().getUpdateInfo()));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bSupported MC Versions: &f" + StringUtils.join(testedVersions, ", ")));
                        if (!testedVersions.contains(mcVersion))
                            player.sendMessage(ChatColor.RED+"Warning your current version, "+mcVersion+", is not supported by this update, there may be unexpected bugs!");
                        player.sendMessage("");

                        TextComponent accept = new TextComponent("[CLICK TO UPDATE]");
                        accept.setColor(ChatColor.DARK_AQUA);
                        accept.setBold(true);
                        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/thirst update"));
                        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&1&lTHIRST &bV" + currentVersion + " &a&l» &bV" + newVersion+"\n&b\n&b    CLICK TO UPDATE")).create()));

                        player.spigot().sendMessage(accept);

                        player.sendMessage("");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m------------------------------"));
                    }
                }.runTaskLater(Thirst.getInstance(), 2L);
            }
        }
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		ThirstManager.getThirst().playerLeave(event.getPlayer());
	}
}

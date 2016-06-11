package com.gamerking195.dev.thirst.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstEffectsHandler;

public class ThirstCommand 
implements CommandExecutor
{

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{ 
		if (label.equalsIgnoreCase("thirst"))
		{
			if (args.length == 0)
			{
				if (sender instanceof Player)
				{
					Player p = (Player) sender;
					if (!p.hasPermission("thirst.command") && !p.hasPermission("thirst.*"))
					{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().NoPermissionMesage));
						return true;
					}
				}

				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m--------------------"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&lThirst &fV"+Main.getInstance().getDescription().getVersion()+" &bby &f"+Main.getInstance().getDescription().getAuthors()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bSpigot: &fhttps://www.spigotmc.org/resources/thirst.24610/"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bGithub: &fhttps://github.com/GamerKing195/Thirst"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHelp: &f/thirst help"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m--------------------"));
				return true;
			}
			else
			{
				if (args[0].equalsIgnoreCase("view"))
				{	
					if (args.length >= 2)
					{
						if (sender instanceof Player)
						{
							Player p = (Player) sender;
							if (!p.hasPermission("thirst.command.view.other") && !p.hasPermission("thirst.*"))
							{
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().NoPermissionMesage));
								return true;
							}
						}

						OfflinePlayer oP = Bukkit.getOfflinePlayer(args[1]);
						if (oP == null || !oP.hasPlayedBefore() || !Thirst.getThirst().getThirstConfig().contains(oP.getUniqueId().toString()))
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().InvalidCommandMessage));
							return true;
						}
						else if (oP != null && oP.hasPlayedBefore() && Thirst.getThirst().getThirstConfig().contains(oP.getUniqueId().toString()))
						{
							//if they run /thirst view %player%
							String thirstViewPlayerMessage = Main.getInstance().getYAMLConfig().ThirstViewPlayerMessage
									.replace("%player%", oP.getName())
									.replace("%bar%", Thirst.getThirst().getThirstBar(oP)
									.replace("%percent%", Thirst.getThirst().getThirstPercent(oP, true)
									.replace("%thirstmessage%", Thirst.getThirst().getThirstString(oP))));

							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', thirstViewPlayerMessage.replace("%thirstmessage%", Thirst.getThirst().getThirstString(oP))));
							return true;
						}
					}
					else
					{
						if (sender instanceof Player)
						{
							Player p = (Player) sender;
							if (!p.hasPermission("thirst.command.view") && !p.hasPermission("thirst.*"))
							{
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().NoPermissionMesage));
								return true;
							}

							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().ThirstViewMessage.replace("%player%", p.getName())+Thirst.getThirst().getThirstString(p)));
							return true;
						}
						else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bYou must be a player to execute that command!"));
							return true;
						}
					}
				}
				else if (args[0].equalsIgnoreCase("help"))
				{
					if (sender instanceof Player)
					{
						Player p = (Player) sender;
						if (!p.hasPermission("thirst.command.help") && !p.hasPermission("thirst.*"))
						{
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().NoPermissionMesage));
							return true;
						}
					}

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m--------------------"));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1&lThirst &fV"+Main.getInstance().getDescription().getVersion()+" &bby &f"+Main.getInstance().getDescription().getAuthors()));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst | &fDisplays basic plugin information."));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst help | &fDisplays this help message."));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst view | &fDisplays your thirst."));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/thirst view [PLAYER] | &fDisplays the thirst of the specified player."));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&m--------------------"));
				}
				else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))
				{
					try 
					{	
						Main.getInstance().getYAMLConfig().reload();
						
						Main.getInstance().getYAMLConfig().load();
						
						Main.getInstance().getYAMLConfig().save();

						for (Player p : Bukkit.getServer().getOnlinePlayers())
						{
							p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
							
							Thirst.getThirst().getThirstConfig().set(p.getUniqueId().toString(), Thirst.getThirst().getPlayerThirst(p));

							ThirstEffectsHandler.getThirstEffects().removePlayer(p);
						}
						
						Thirst.getThirst().saveThirstFile();
						
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bconfig.yml & thirst_data.yml sucsesfully reloaded!"));
					} 
					catch (InvalidConfigurationException ex)
					{
						Logger log = Main.getInstance().getLogger();
						PluginDescriptionFile pdf = Main.getInstance().getDescription();

						log.log(Level.SEVERE, "=============================");
						log.log(Level.SEVERE, "Error while reloading the config for "+pdf.getName()+" V"+pdf.getVersion());
						log.log(Level.SEVERE, "WARNING, Thirst is now disabled!");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "Printing StackTrace:");
						ex.printStackTrace();
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "Printing Message:");
						log.log(Level.SEVERE, ex.getMessage());
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "");
						log.log(Level.SEVERE, "END OF ERROR");
						log.log(Level.SEVERE, "=============================");

						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&1Thirst&8] &bconfig.yml reloading failed! Check the console for more info."));
						
						Main.getInstance().disable();
						return true;
					}
					for (Player p : Bukkit.getServer().getOnlinePlayers())
					{
						Thirst.getThirst().displayThirst(p);
					}
					
					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getYAMLConfig().InvalidCommandMessage));
					return true;
				}
			}
		}
		return true;
	}
}
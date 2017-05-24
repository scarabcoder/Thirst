package com.gamerking195.dev.thirst.listener;

import com.gamerking195.dev.thirst.util.UtilActionBar;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.potion.PotionEffect;

import com.gamerking195.dev.thirst.Main;
import com.gamerking195.dev.thirst.Thirst;
import com.gamerking195.dev.thirst.ThirstData;

public class PlayerGamemodeChangeListener
		implements Listener
{
	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event)
	{
		Player p = event.getPlayer();

		if (event.getNewGameMode() != GameMode.CREATIVE || Main.getInstance().getYAMLConfig().ignoreCreative)
		{
			Thirst.getThirst().displayThirst(p);
			return;
		}
		else if (event.getNewGameMode() == GameMode.CREATIVE && !Main.getInstance().getYAMLConfig().ignoreCreative)
		{
			for (PotionEffect effect : p.getActivePotionEffects())
			{
				p.removePotionEffect(effect.getType());
			}
		}

		if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("scoreboard"))
		{
			if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("SCOREBOARD"))
				p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
		}
		else if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("action"))
		{
			UtilActionBar.getInstance().sendActionBar(p, "");
		}
		else if (Main.getInstance().getYAMLConfig().displayType.equalsIgnoreCase("bossbar"))
		{
			ThirstData data = Thirst.getThirst().getThirstData(p);

			if (data.getBar() != null)
			{
				data.getBar().removePlayer(p);
			}
		}
	}
}
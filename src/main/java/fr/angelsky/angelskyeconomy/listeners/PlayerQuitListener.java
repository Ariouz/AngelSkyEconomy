package fr.angelsky.angelskyeconomy.listeners;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import fr.angelsky.angelskyeconomy.eco.TempPlayerEco;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

	private final AngelSkyEconomy angelSkyEconomy;

	public PlayerQuitListener(AngelSkyEconomy angelSkyEconomy)
	{
		this.angelSkyEconomy = angelSkyEconomy;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		TempPlayerEco tempPlayerEco = angelSkyEconomy.getAccounts().get(player.getUniqueId());
		angelSkyEconomy.getEco().saveTempEco(tempPlayerEco);
	}

}

package fr.angelsky.angelskyeconomy.listeners;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

  private final AngelSkyEconomy angelSkyEconomy;

  public PlayerJoinListener(AngelSkyEconomy angelSkyEconomy){
    this.angelSkyEconomy = angelSkyEconomy;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    if (!angelSkyEconomy.getEco().hasAccount(player.getUniqueId())) {
      new BukkitRunnable() {
        public void run() {
          angelSkyEconomy.getEco().createAccount(player.getUniqueId());
        }
      }.runTaskAsynchronously(angelSkyEconomy);
    }
  }
}

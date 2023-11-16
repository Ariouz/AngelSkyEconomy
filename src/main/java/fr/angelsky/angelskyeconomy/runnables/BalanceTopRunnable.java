package fr.angelsky.angelskyeconomy.runnables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import fr.angelsky.angelskyeconomy.eco.PlayerBalance;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BalanceTopRunnable extends BukkitRunnable {
  private List<PlayerBalance> balanceTop = new ArrayList<>();

  private final AngelSkyEconomy angelSkyEconomy;

  public BalanceTopRunnable(AngelSkyEconomy angelSkyEconomy){
    this.angelSkyEconomy = angelSkyEconomy;
  }

  public void run() {
    List<PlayerBalance> btop = new ArrayList<>(angelSkyEconomy.getEco().getPlayers());
    btop.sort(Comparator.<PlayerBalance>comparingDouble(PlayerBalance::getBalance).reversed());
    
    this.balanceTop = btop;
  }

  public void start(int interval) {
    runTaskTimerAsynchronously((Plugin)angelSkyEconomy, 1L, interval);
  }

  public List<PlayerBalance> getBalanceTop() {
    return this.balanceTop;
  }
}

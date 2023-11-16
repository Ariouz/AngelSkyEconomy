package fr.angelsky.angelskyeconomy.commands.money;

import java.util.Collections;
import java.util.List;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import fr.angelsky.angelskyeconomy.commands.CommandExecutor;
import fr.angelsky.angelskyeconomy.runnables.BalanceTopRunnable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MoneyReloadCommand extends CommandExecutor {

  private final AngelSkyEconomy angelSkyEconomy;

  public MoneyReloadCommand(AngelSkyEconomy angelSkyEconomy) {
    this.angelSkyEconomy = angelSkyEconomy;
    setName("reload");
    setPermission("economy.command.reload");
    setUsage(angelSkyEconomy.getConfigHandler().getMessage("money.reload.usage"));
    setBoth(true);
    setLengths(Collections.singletonList(1));
    setAliases(Collections.singletonList("rl"));
  }
  
  public void execute(CommandSender sender, String[] args) {
    angelSkyEconomy.reloadConfig();
    angelSkyEconomy.setSuffixes(angelSkyEconomy.getConfigHandler().getSuffixes());
    
    angelSkyEconomy.getBalanceTopRunnable().cancel();
    angelSkyEconomy.setBalanceTopRunnable(new BalanceTopRunnable(angelSkyEconomy));
    angelSkyEconomy.getBalanceTopRunnable().start(angelSkyEconomy.getConfigHandler().getBalanceTopInterval());
    
    angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.reload.reloaded");
  }


  
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    return null;
  }
}

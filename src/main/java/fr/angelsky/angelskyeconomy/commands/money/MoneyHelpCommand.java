package fr.angelsky.angelskyeconomy.commands.money;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import fr.angelsky.angelskyeconomy.commands.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class MoneyHelpCommand extends CommandExecutor {
  
  private final AngelSkyEconomy angelSkyEconomy;
  public MoneyHelpCommand(AngelSkyEconomy angelSkyEconomy) {
    this.angelSkyEconomy = angelSkyEconomy;
    setName("help");
    setPermission("economy.command.help");
    setUsage(angelSkyEconomy.getConfigHandler().getMessage("money.help.usage"));
    setPlayer(true);
    setConsole(false);
    setLengths(Collections.singletonList(1));
  }


  
  public void execute(CommandSender sender, String[] args) {
    angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.help.message");
  }




  
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    return null;
  }
}

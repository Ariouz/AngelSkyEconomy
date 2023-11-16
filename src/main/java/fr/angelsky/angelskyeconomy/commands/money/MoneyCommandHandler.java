package fr.angelsky.angelskyeconomy.commands.money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class MoneyCommandHandler implements CommandExecutor, TabCompleter {
  private Map<String, fr.angelsky.angelskyeconomy.commands.CommandExecutor> commands = new HashMap<>();
  
  private final AngelSkyEconomy angelSkyEconomy;
  
  public MoneyCommandHandler(AngelSkyEconomy angelSkyEconomy) {
    this.angelSkyEconomy = angelSkyEconomy;
    this.commands.put("give", new MoneyGiveCommand(angelSkyEconomy));
    this.commands.put("help", new MoneyHelpCommand(angelSkyEconomy));
    this.commands.put("reload", new MoneyReloadCommand(angelSkyEconomy));
    this.commands.put("set", new MoneySetCommand(angelSkyEconomy));
    this.commands.put("take", new MoneyTakeCommand(angelSkyEconomy));
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length == 0) {
      fr.angelsky.angelskyeconomy.commands.CommandExecutor command = this.commands.get("help");
      command.execute(sender, args);
    }
    else {
      String subcmd = args[0].toLowerCase();
      if (this.commands.containsKey(subcmd) || aliasesContains(subcmd)) {
        fr.angelsky.angelskyeconomy.commands.CommandExecutor command = getCommand(subcmd);

        if (!sender.hasPermission(command.getPermission())) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.nopermission");
          return true;
        }
        if (!command.isBoth()) {
          
          if (command.isConsole() && sender instanceof org.bukkit.entity.Player) {
            angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.consoleOnly");
            return true;
          } 
          
          if (command.isPlayer() && sender instanceof org.bukkit.command.ConsoleCommandSender) {
            angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.playerOnly");
            return true;
          }
        }
        if (!command.getLengths().isEmpty() && !command.getLengths().contains(Integer.valueOf(args.length))) {
          angelSkyEconomy.getStringUtils().sendMessage(sender, command.getUsage());
          return true;
        }
        command.execute(sender, args);
      }
      else {
        angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.invalidSubCommand");
        return true;
      }
    }
    return false;
  }

  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length == 1) {
      List<String> results = new ArrayList<>();
      for (String name : this.commands.keySet()) {
        fr.angelsky.angelskyeconomy.commands.CommandExecutor command = this.commands.get(name);

        if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
          results.add(name.toLowerCase());
        }
        
        for (String alias : command.getAliases()) {
          if (alias.toLowerCase().startsWith(args[0].toLowerCase())) {
            if (sender.hasPermission(command.getPermission()))
              results.add(alias.toLowerCase());
          }
        } 
      }
      return results;
    } 
    
    if (args.length > 1) {
      fr.angelsky.angelskyeconomy.commands.CommandExecutor command = getCommand(args[0]);
      if (command != null) {
        return command.onTabComplete(sender, cmd, label, args);
      }
    }
    return null;
  }

  private boolean aliasesContains(String subcmd) {
    for (fr.angelsky.angelskyeconomy.commands.CommandExecutor cmd : this.commands.values()) {
      if (cmd.getAliases().contains(subcmd)) {
        return true;
      }
    } 
    return false;
  }
  
  public fr.angelsky.angelskyeconomy.commands.CommandExecutor getCommand(String subcmd) {
    for (String name : this.commands.keySet()) {
      if (name.equalsIgnoreCase(subcmd)) {
        return this.commands.get(name);
      }
      for (String alias : this.commands.get(name).getAliases()) {
        if (alias.equalsIgnoreCase(subcmd)) {
          return this.commands.get(name);
        }
      } 
    } 
    return null;
  }
  
  public Map<String, fr.angelsky.angelskyeconomy.commands.CommandExecutor> getCommands() {
    return this.commands;
  }
  
  public void addCommand(String name, fr.angelsky.angelskyeconomy.commands.CommandExecutor cmd) {
    this.commands.put(name, cmd);
  }
}

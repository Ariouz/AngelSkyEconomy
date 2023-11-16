package fr.angelsky.angelskyeconomy.commands.money;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import fr.angelsky.angelskyeconomy.commands.CommandExecutor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyTakeCommand extends CommandExecutor {
  
  private final AngelSkyEconomy angelSkyEconomy;
  
  public MoneyTakeCommand(AngelSkyEconomy angelSkyEconomy) {
    this.angelSkyEconomy = angelSkyEconomy;
    setName("take");
    setPermission("economy.command.take");
    setUsage(angelSkyEconomy.getConfigHandler().getMessage("money.take.usage"));
    setBoth(true);
    setLengths(Collections.singletonList(3));
    setAliases(Collections.singletonList("remove"));
  }

  public void execute(CommandSender sender, String[] args) {
    List<OfflinePlayer> others = angelSkyEconomy.getPlayersFromString(sender, args[1]);
    
    if (others.isEmpty() && !args[1].equals("@a")) {
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.take.otherDoesntExist", ImmutableMap.of(
            "%player%", args[1]));
      
      return;
    } 
    double amount = 0.0D;
    try {
      amount = angelSkyEconomy.getAmountFromString(args[2]);
    }
    catch (NumberFormatException e) {
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.take.invalidAmount", ImmutableMap.of(
            "%amount%", args[2]));
      return;
    } 
    if (amount < 0.0D) {
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.take.invalidAmount", ImmutableMap.of(
            "%amount%", args[2]));
      
      return;
    } 
    int total = 0;
    boolean failed = false;
    
    for (OfflinePlayer other : others) {
      
      if (!angelSkyEconomy.getEco().hasAccount(other.getUniqueId())) {
        angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.take.otherNoAccount", ImmutableMap.of(
              "%player%", Objects.requireNonNull(other.getName())));
        failed = true;
        
        continue;
      } 
      if (!angelSkyEconomy.getEco().has(other.getUniqueId(), amount)) {
        angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.take.insufficientFunds", ImmutableMap.of(
              "%player%", Objects.requireNonNull(other.getName())));
        failed = true;
        
        continue;
      }
      angelSkyEconomy.getEco().withdraw(other.getUniqueId(), amount);
      
      if (other instanceof Player && (
        !(sender instanceof Player) || !((Player)sender).equals(other))) {
        angelSkyEconomy.getStringUtils().sendConfigMessage((Player)other, "messages.money.take.taken", ImmutableMap.of(
              "%amount%", angelSkyEconomy.format(amount)));
      }
      total++;
    } 

    if (others.size() == 1) {
      if (!failed)
      {
        angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.take.take", ImmutableMap.of(
              "%amount%", angelSkyEconomy.format(amount),
              "%player%", ((OfflinePlayer)others.get(0)).getName()));
      }
    }
    else {
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.take.takeMultiple", ImmutableMap.of(
            "%total%", (new StringBuilder(String.valueOf(total))).toString(), 
            "%amount%", angelSkyEconomy.format(amount)));
    } 
  }

  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    return null;
  }
}

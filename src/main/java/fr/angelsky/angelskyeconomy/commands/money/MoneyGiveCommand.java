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

public class MoneyGiveCommand extends CommandExecutor {

  private final AngelSkyEconomy angelSkyEconomy;

  public MoneyGiveCommand(AngelSkyEconomy angelSkyEconomy) {
    this.angelSkyEconomy = angelSkyEconomy;
    setName("give");
    setPermission("economy.command.give");
    setUsage(angelSkyEconomy.getConfigHandler().getMessage("money.give.usage"));
    setBoth(true);
    setLengths(Collections.singletonList(3));
  }

  public void execute(CommandSender sender, String[] args) {
    List<OfflinePlayer> others = angelSkyEconomy.getPlayersFromString(sender, args[1]);
    
    if (others.isEmpty() && !args[1].equals("@a")) {
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.give.otherDoesntExist", ImmutableMap.of(
            "%player%", args[1]));
      
      return;
    } 
    double amount = 0.0D;
    try {
      amount = angelSkyEconomy.getAmountFromString(args[2]);
    }
    catch (NumberFormatException e) {
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.give.invalidAmount", ImmutableMap.of(
            "%amount%", args[2]));
      return;
    } 
    if (amount < 0.0D) {
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.give.invalidAmount", ImmutableMap.of(
            "%amount%", args[2]));
      
      return;
    } 
    int total = 0;
    boolean failed = false;
    
    for (OfflinePlayer other : others) {
      
      if (!angelSkyEconomy.getEco().hasAccount(other.getUniqueId())) {
        angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.give.otherNoAccount", ImmutableMap.of(
              "%player%", Objects.requireNonNull(other.getName())));
        failed = true;
        
        continue;
      } 
      angelSkyEconomy.getEco().deposit(other.getUniqueId(), amount);
      if (other instanceof Player && (
        !(sender instanceof Player) || !((Player)sender).equals(other))) {
        angelSkyEconomy.getStringUtils().sendConfigMessage((Player)other, "messages.money.give.received", ImmutableMap.of(
              "%amount%", angelSkyEconomy.format(amount)));
      }
      total++;
    } 

    if (others.size() == 1) {
      if (!failed)
      {
        OfflinePlayer other = others.get(0);
        angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.give.sent", ImmutableMap.of(
              "%amount%", angelSkyEconomy.format(amount), 
              "%player%", Objects.requireNonNull(other.getName())));
      }
    }
    else {
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.give.sentMultiple", ImmutableMap.of(
            "%total%", (new StringBuilder(String.valueOf(total))).toString(), 
            "%amount%", angelSkyEconomy.format(amount)));
    } 
  }

  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    return null;
  }
}

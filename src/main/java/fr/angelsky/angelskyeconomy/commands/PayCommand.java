package fr.angelsky.angelskyeconomy.commands;

import com.google.common.collect.ImmutableMap;
import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PayCommand implements CommandExecutor {

  private final AngelSkyEconomy angelSkyEconomy;

  public PayCommand(AngelSkyEconomy angelSkyEconomy){
    this.angelSkyEconomy = angelSkyEconomy;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String lebel, String[] args) {
    if (sender.hasPermission("economy.command.pay")) {
      
      if (args.length == 2) {
        
        if (!(sender instanceof Player)) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.playersOnly");
          return true;
        } 
        
        Player player = (Player)sender;
        
        if (!angelSkyEconomy.getEco().hasAccount(player.getUniqueId())) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(player, "messages.pay.noAccount");
          return true;
        } 
        
        OfflinePlayer other = Bukkit.getOfflinePlayer(args[0]);

        if (!angelSkyEconomy.getEco().hasAccount(other.getUniqueId())) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(player, "messages.pay.otherNoAccount", ImmutableMap.of(
                "%player%", other.getName()));
          return true;
        } 
        
        if (other.getUniqueId().equals(player.getUniqueId())) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(player, "messages.pay.cannotPaySelf");
          return true;
        } 
        
        double amount;
        try {
          amount = angelSkyEconomy.getAmountFromString(args[1]);
        }
        catch (NumberFormatException e) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(player, "messages.pay.invalidAmount", ImmutableMap.of(
                "%amount%", args[1]));
          return true;
        } 
        if (amount <= 0.0D) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.money.give.invalidAmount", ImmutableMap.of(
                "%amount%", args[1]));
          return true;
        } 
        
        if (!angelSkyEconomy.getEco().has(player.getUniqueId(), amount)) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(player, "messages.pay.insufficientFunds");
          return true;
        } 
        
        angelSkyEconomy.getEco().withdraw(player.getUniqueId(), amount);
        angelSkyEconomy.getStringUtils().sendConfigMessage(player, "messages.pay.paid", ImmutableMap.of(
              "%player%", Objects.requireNonNull(other.getName()),
              "%amount%", angelSkyEconomy.format(amount)));
        
        angelSkyEconomy.getEco().deposit(other.getUniqueId(), amount);
        if (other instanceof Player) {
          angelSkyEconomy.getStringUtils().sendConfigMessage((Player)other, "messages.pay.received", ImmutableMap.of(
                "%player%", player.getName(), 
                "%amount%", angelSkyEconomy.format(amount)));
        }
        return true;
      } 
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.pay.usage");
      return true;
    }
    angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.nopermission");
    return true;
  }
}

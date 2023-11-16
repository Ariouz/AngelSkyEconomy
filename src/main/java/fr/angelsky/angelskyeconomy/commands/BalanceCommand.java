package fr.angelsky.angelskyeconomy.commands;

import com.google.common.collect.ImmutableMap;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BalanceCommand implements CommandExecutor {

  private final AngelSkyEconomy angelSkyEconomy;

  public BalanceCommand(AngelSkyEconomy angelSkyEconomy){
    this.angelSkyEconomy = angelSkyEconomy;
  }

  public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
    if (sender.hasPermission("economy.command.balance")) {
      if (args.length == 0) {
        if (!(sender instanceof Player)) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.playersOnly");
          return true;
        }
        Player player = (Player)sender;

        if (!angelSkyEconomy.getEco().hasAccount(player.getUniqueId())) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(player, "messages.balance.noAccount");
          return true;
        }
        double balance = angelSkyEconomy.getEco().getBalance(player.getUniqueId()).getBalance();
        angelSkyEconomy.getStringUtils().sendConfigMessage(player, "messages.balance.balance", ImmutableMap.of(
                "%balance%", String.valueOf(angelSkyEconomy.format(balance))));

        return true;
      }

      if (args.length == 1) {
        OfflinePlayer other = Bukkit.getOfflinePlayer(args[0]);
        if (!angelSkyEconomy.getEco().hasAccount(other.getUniqueId())) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.balance.otherNoAccount", ImmutableMap.of(
                  "%player%", Objects.requireNonNull(other.getName())));
          return true;
        }
        double balance = angelSkyEconomy.getEco().getBalance(other.getUniqueId()).getBalance();
        angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.balance.otherBalance", ImmutableMap.of(
                "%player%", Objects.requireNonNull(other.getName()),
                "%balance%", String.valueOf(angelSkyEconomy.format(balance))));

        return true;
      }
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.balance.usage");
      return true;
    }
    angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.nopermission");
    return true;
  }
}

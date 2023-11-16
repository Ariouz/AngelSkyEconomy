package fr.angelsky.angelskyeconomy.commands;

import com.google.common.collect.ImmutableMap;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import fr.angelsky.angelskyeconomy.eco.PlayerBalance;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BalanceTopCommand implements CommandExecutor {

  private final AngelSkyEconomy angelSkyEconomy;

  public BalanceTopCommand(AngelSkyEconomy angelSkyEconomy){
    this.angelSkyEconomy = angelSkyEconomy;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender.hasPermission("economy.command.balancetop")) {

      if (args.length < 2) {

        if (angelSkyEconomy.getBalanceTopRunnable().getBalanceTop().isEmpty()) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.top.noAccounts");
          return true;
        }

        int top = 0;
        if (args.length == 1) {
          try {
            top = Integer.valueOf(args[0]) - 1;
          } catch (NumberFormatException e) {
            angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.top.invalidTop", ImmutableMap.of(
                    "%top%", args[0]));
            return true;
          }
        }
        if (top < 0) {
          angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.top.invalidTop", ImmutableMap.of(
                  "%top%", args[0]));
          return true;
        }
        List<PlayerBalance> playerBalances = angelSkyEconomy.getBalanceTopRunnable().getBalanceTop();

        int i = top * 10;
        int j = 0;
        while (i < (top + 1) * 10) {
          if (playerBalances.size() > i) {
            PlayerBalance playerBalance = playerBalances.get(i);
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerBalance.getUUID());
            if (player.getName() != null) {
              angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.top.message", ImmutableMap.of(
                      "%rank%", String.valueOf(i + 1 - j),
                      "%player%", player.getName(),
                      "%balance%", String.valueOf(angelSkyEconomy.format(playerBalance.getBalance()))));
            }
            else {

              j++;
            }
          }
          else if (i == top * 10) {
            angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.top.notEnoughPlayers");
            return true;
          }

          i++;
        }

        if (sender instanceof Player) {
          Player player = (Player)sender;
          if (angelSkyEconomy.getEco().hasAccount(player.getUniqueId())) {
            PlayerBalance playerBalance = null;
            int playerIndex = -1;
            for (PlayerBalance pb : playerBalances) {
              if (pb.getUUID().equals(player.getUniqueId())) {
                playerBalance = pb;
                playerIndex = playerBalances.indexOf(pb);
              }
            }
            if (playerBalance != null && (
                    playerIndex < top * 10 || playerIndex > (top + 1) * 10)) {
              angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.top.self", ImmutableMap.of(
                      "%rank%", (new StringBuilder(String.valueOf(playerIndex))).toString(),
                      "%player%", player.getName(),
                      "%balance%", (new StringBuilder(String.valueOf(angelSkyEconomy.format(playerBalance.getBalance())))).toString()));
            }
          }
        }
        return true;
      }
      angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.top.usage");

      return true;
    }





    angelSkyEconomy.getStringUtils().sendConfigMessage(sender, "messages.nopermission");

    return true;
  }
}

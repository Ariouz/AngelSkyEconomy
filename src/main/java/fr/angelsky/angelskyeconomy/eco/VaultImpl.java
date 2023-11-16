package fr.angelsky.angelskyeconomy.eco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VaultImpl implements Economy {
  
  private final AngelSkyEconomy angelSkyEconomy;
  
  public VaultImpl(AngelSkyEconomy angelSkyEconomy){
    this.angelSkyEconomy = angelSkyEconomy;
  }
  
  public boolean isEnabled() {
    return (angelSkyEconomy != null);
  }
  
  public String currencyNamePlural() {
    return angelSkyEconomy.getConfigHandler().getCurrencyNamePlural();
  }

  public String currencyNameSingular() {
    return angelSkyEconomy.getConfigHandler().getCurrencyNameSingular();
  }
  
  public String format(double v) {
    BigDecimal bd = (new BigDecimal(v)).setScale(2, RoundingMode.HALF_EVEN);
    return String.valueOf(bd.doubleValue());
  }

  public int fractionalDigits() {
    return -1;
  }

  public boolean createPlayerAccount(String name) {
    return createAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
  }

  public boolean createPlayerAccount(OfflinePlayer player) {
    return createAccount(player.getUniqueId());
  }

  public boolean createPlayerAccount(String name, String world) {
    return createAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
  }

  public boolean createPlayerAccount(OfflinePlayer player, String world) {
    return createAccount(player.getUniqueId());
  }
  
  public boolean createAccount(UUID uuid) {
    return angelSkyEconomy.getEco().createAccount(uuid);
  }

  public EconomyResponse depositPlayer(String name, double amount) {
    return deposit(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
  }

  public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
    return deposit(player.getUniqueId(), amount);
  }

  public EconomyResponse depositPlayer(String name, String world, double amount) {
    return deposit(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
  }

  public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
    return deposit(player.getUniqueId(), amount);
  }
  
  private EconomyResponse deposit(UUID uuid, double amount) {
    if (!angelSkyEconomy.getEco().deposit(uuid, amount)) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Failed to deposit funds.");
    }
    return new EconomyResponse(amount, getBalance(uuid), EconomyResponse.ResponseType.SUCCESS, "");
  }

  public double getBalance(String name) {
    return getBalance(Bukkit.getOfflinePlayer(name).getUniqueId());
  }

  public double getBalance(OfflinePlayer player) {
    return getBalance(player.getUniqueId());
  }

  
  public double getBalance(String name, String world) {
    return getBalance(Bukkit.getOfflinePlayer(name).getUniqueId());
  }

  public double getBalance(OfflinePlayer player, String world) {
    return getBalance(player.getUniqueId());
  }
  
  public double getBalance(UUID uuid) {
    return angelSkyEconomy.getEco().getBalance(uuid).getBalance();
  }

  public String getName() {
    return "Economy";
  }

  public boolean has(String name, double amount) {
    return has(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
  }
  
  public boolean has(OfflinePlayer player, double amount) {
    return has(player.getUniqueId(), amount);
  }

  public boolean has(String name, String world, double amount) {
    return has(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
  }

  public boolean has(OfflinePlayer player, String world, double amount) {
    return has(player.getUniqueId(), amount);
  }
  
  private boolean has(UUID uuid, double amount) {
    return angelSkyEconomy.getEco().has(uuid, amount);
  }

  public boolean hasAccount(String name) {
    return hasAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
  }

  public boolean hasAccount(OfflinePlayer player) {
    return hasAccount(player.getUniqueId());
  }

  public boolean hasAccount(String name, String world) {
    return hasAccount(Bukkit.getOfflinePlayer(name).getUniqueId());
  }

  public boolean hasAccount(OfflinePlayer player, String world) {
    return hasAccount(player.getUniqueId());
  }
  
  private boolean hasAccount(UUID uuid) {
    return angelSkyEconomy.getEco().hasAccount(uuid);
  }

  public EconomyResponse withdrawPlayer(String name, double amount) {
    return withdraw(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
  }
  
  public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
    return withdraw(player.getUniqueId(), amount);
  }
  
  public EconomyResponse withdrawPlayer(String name, String world, double amount) {
    return withdraw(Bukkit.getOfflinePlayer(name).getUniqueId(), amount);
  }

  public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
    return withdraw(player.getUniqueId(), amount);
  }
  
  private EconomyResponse withdraw(UUID uuid, double amount) {
    if (!angelSkyEconomy.getEco().withdraw(uuid, amount)) {
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Failed to withdraw funds.");
    }
    return new EconomyResponse(amount, getBalance(uuid), EconomyResponse.ResponseType.SUCCESS, "");
  }

  public boolean hasBankSupport() {
    return false;
  }

  public List<String> getBanks() {
    return null;
  }

  public EconomyResponse isBankMember(String arg0, String arg1) {
    return null;
  }

  public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
    return null;
  }

  public EconomyResponse isBankOwner(String arg0, String arg1) {
    return null;
  }

  public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
    return null;
  }

  public EconomyResponse bankBalance(String arg0) {
    return null;
  }

  public EconomyResponse bankDeposit(String arg0, double arg1) {
    return null;
  }

  public EconomyResponse bankHas(String arg0, double arg1) {
    return null;
  }

  public EconomyResponse bankWithdraw(String arg0, double arg1) {
    return null;
  }
  
  public EconomyResponse createBank(String arg0, String arg1) {
    return null;
  }
  
  public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
    return null;
  }

  public EconomyResponse deleteBank(String arg0) {
    return null;
  }
}
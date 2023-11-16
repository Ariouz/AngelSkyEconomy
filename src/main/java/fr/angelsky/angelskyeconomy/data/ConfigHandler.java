package fr.angelsky.angelskyeconomy.data;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {

  private final AngelSkyEconomy angelSkyEconomy;

  public ConfigHandler(AngelSkyEconomy angelSkyEconomy){
    this.angelSkyEconomy = angelSkyEconomy;
  }

  private FileConfiguration getConfig() {
    return angelSkyEconomy.getConfig();
  }
  
  public Object get(String path) {
    return getConfig().get(path);
  }
  
  public List<String> getMessage(String path) {
    return getConfig().getStringList("messages." + path);
  }
  
  public int getBalanceTopInterval() {
    return getConfig().getInt("BalanceTopTimerInterval");
  }
  
  public String getCurrencyNameSingular() {
    return getConfig().getString("currencyNameSingular");
  }
  
  public String getCurrencyNamePlural() {
    return getConfig().getString("currencyNamePlural");
  }
  
  public double getStartingBalance() {
    return getConfig().getDouble("startingBalance");
  }
  
  public Locale getLocale() {
    return Locale.forLanguageTag(getConfig().getString("locale"));
  }
  
  public boolean isCustomSymbol() {
    return getConfig().getBoolean("customSymbolEnabled");
  }
  
  public String getCustomSymbol() {
    return getConfig().getString("customSymbol");
  }
  
  public boolean isSQL() {
    return getConfig().getBoolean("mysql.use-mysql");
  }
  
  public String getHost() {
    return getConfig().getString("mysql.host");
  }
  
  public int getPort() {
    return getConfig().getInt("mysql.port");
  }
  
  public String getDatabase() {
    return getConfig().getString("mysql.database");
  }
  
  public String getUsername() {
    return getConfig().getString("mysql.username");
  }
  
  public String getPassword() {
    return getConfig().getString("mysql.password");
  }
  public String getTable() {
    return getConfig().getString("mysql.table");
  }

  public Map<String, Integer> getSuffixes() {
    Map<String, Integer> suffixes = new HashMap<>();
    for (String suffix : getConfig().getConfigurationSection("suffixes").getKeys(false)) {
      suffixes.put(suffix, angelSkyEconomy.getConfig().getInt("suffixes." + suffix));
    }
    return suffixes;
  }
}

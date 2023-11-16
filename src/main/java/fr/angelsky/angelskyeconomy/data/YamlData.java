package fr.angelsky.angelskyeconomy.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlData {
  private FileConfiguration dataConfig = null;
  private File configFile = null;
  private String fileName = null;
  private String path = null;
  
  private final AngelSkyEconomy angelSkyEconomy;
  
  public YamlData(AngelSkyEconomy angelSkyEconomy, String fileName, String path) {
    this.angelSkyEconomy = angelSkyEconomy;
    this.fileName = fileName;
    this.path = path;
    saveDefaultConfig();
  }
  
  public void reloadConfig() {
    if (this.configFile == null)
      this.configFile = new File(this.path, this.fileName); 
    this.dataConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configFile);
    InputStream defaultStream = angelSkyEconomy.getResource(this.fileName);
    if (defaultStream != null) {
      YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
      this.dataConfig.setDefaults((Configuration)defaultConfig);
    } 
  }
  
  public FileConfiguration getConfig() {
    if (this.dataConfig == null)
      reloadConfig(); 
    return this.dataConfig;
  }
  
  public void saveConfig() {
    if (this.dataConfig == null || this.configFile == null)
      return; 
    try {
      getConfig().save(this.configFile);
    } catch (IOException e) {
      angelSkyEconomy.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
    } 
  }
  
  public void saveDefaultConfig() {
    if (this.configFile == null)
      this.configFile = new File(this.path, this.fileName); 
    try {
      this.configFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    } 
    if (!this.configFile.exists())
      angelSkyEconomy.saveResource(String.valueOf(this.path) + "/" + this.fileName, false); 
  }
}

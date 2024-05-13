package fr.angelsky.angelskyeconomy.eco;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import fr.angelsky.angelskyeconomy.data.YamlData;

public class YamlEconomy implements Economy {
  
  private final AngelSkyEconomy angelSkyEconomy;
  
  public YamlEconomy(AngelSkyEconomy angelSkyEconomy) {
    this.angelSkyEconomy = angelSkyEconomy;
    Path dataDir = Paths.get(this.angelSkyEconomy.getPath() + "/data/");
    if (!Files.exists(dataDir)) {
      try {
        Files.createDirectory(dataDir);
      } catch (IOException e) {
        this.angelSkyEconomy.warn("Une erreur est survenue lors de la creation du directory Data.");
      } 
    }
  }

  public boolean createAccount(UUID uuid) {
    set(uuid, angelSkyEconomy.getConfigHandler().getStartingBalance());
    return true;
  }

  public boolean hasAccount(UUID uuid) {
    for (PlayerBalance pb : getPlayers()) {
      if (pb.getUUID().equals(uuid)) {
        return true;
      }
    } 
    return false;
  }

  public boolean delete(UUID uuid) {
    File islandFile = new File(String.valueOf(angelSkyEconomy.getPath()) + "/data/" + uuid.toString() + ".yml");
    islandFile.delete();
    return true;
  }

  public boolean withdraw(UUID uuid, double amount) {
    return set(uuid, getBalance(uuid).getBalance() - amount);
  }

  public boolean deposit(UUID uuid, double amount) {
    return set(uuid, getBalance(uuid).getBalance() + amount);
  }

  public boolean set(UUID uuid, double amount) {
    if (amount < 0.0D)
      return false; 
    YamlData data = new YamlData(angelSkyEconomy, uuid.toString() + ".yml", angelSkyEconomy.getPath() + "/data");
    data.getConfig().set("UUID", uuid.toString());
    data.getConfig().set("Balance", Double.valueOf(amount));
    data.saveConfig();
    return true;
  }

  public boolean has(UUID uuid, double amount) {
    return (getBalance(uuid).getBalance() >= amount);
  }

  @Override
  public PlayerBalance getSQLBalance(UUID paramUUID) {
    return null;
  }

  public PlayerBalance getBalance(UUID uuid) {
    try {
      YamlData data = new YamlData(angelSkyEconomy, uuid.toString() + ".yml",angelSkyEconomy.getPath() + "/data");
      double balance = data.getConfig().getDouble("Balance");
      return new PlayerBalance(uuid, balance);
    } catch (Exception e) {
      return new PlayerBalance(uuid, 0.0D);
    } 
  }

  
  public List<PlayerBalance> getPlayers() {
    List<PlayerBalance> playerData = new ArrayList<>();
    File[] files = new File(angelSkyEconomy.getPath() + "/data").listFiles();
    byte b;
    int i;
    File[] filesArray;

    for (i = (Objects.requireNonNull(filesArray = files)).length, b = 0; b < i; ) {
      File file = filesArray[b];
      playerData.add(getBalance(UUID.fromString(file.getName().replace(".yml", ""))));
      b++;
    }
    return playerData;
  }

  @Override
  public void saveTempEco(TempPlayerEco tempPlayerEco) {
  }

  @Override
  public void setBalance(UUID uniqueId, double amount) {
    this.set(uniqueId, amount);
  }
}

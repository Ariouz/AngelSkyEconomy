package fr.angelsky.angelskyeconomy.eco;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import fr.angelsky.angelskyeconomy.data.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SQLEconomy implements Economy {
  private MySQL sql;
  private final AngelSkyEconomy angelSkyEconomy;
  
  public SQLEconomy(AngelSkyEconomy angelSkyEconomy) {
    this.angelSkyEconomy = angelSkyEconomy;
    this.sql = new MySQL(
        angelSkyEconomy.getConfigHandler().getHost(), 
        angelSkyEconomy.getConfigHandler().getPort(), 
        angelSkyEconomy.getConfigHandler().getDatabase(), 
        angelSkyEconomy.getConfigHandler().getUsername(), 
        angelSkyEconomy.getConfigHandler().getPassword());
    
    connectToSQL();
    
    if (this.sql.isConnected()) {
      
      try {
        Statement statement = this.sql.getConnection().createStatement();
        DatabaseMetaData md = this.sql.getConnection().getMetaData();
        statement.execute("CREATE TABLE IF NOT EXISTS Economy (UUID VARCHAR(36) NOT NULL);");
        for (Map.Entry<String, String> column : angelSkyEconomy.getSQLColumns().entrySet()) {
          if (!md.getColumns(null, null, "Economy", column.getKey()).next()) {
            statement.execute("ALTER TABLE Economy ADD " + column.getKey() + " " + column.getValue() + ";");
          }
        } 
        statement.close();
      } catch (SQLException e) {
        angelSkyEconomy.disable("There was an error with creating the database table.");
        return;
      } 
      try {
        PreparedStatement statement = this.sql.getConnection().prepareStatement("ALTER TABLE Economy MODIFY COLUMN Balance " + 
            angelSkyEconomy.getSQLColumns().get("Balance"));
        statement.executeUpdate();
        statement.close();
      } catch (SQLException e) {
        angelSkyEconomy.disable("There was an error updating the sql balance from 1dp to 2dp.");
      } 
    } 
  }
    
  private void connectToSQL() {
    try {
      this.sql.connect();
      angelSkyEconomy.warn("Successfully connected to mysql database.");
    }
    catch (SQLException e) {
      angelSkyEconomy.warn("There was an error connecting to the database. " + e.getMessage());
      Bukkit.getPluginManager().disablePlugin(angelSkyEconomy);
      
      return;
    } catch (ClassNotFoundException e) {
      angelSkyEconomy.getLogger().warning("The MySQL driver class could not be found.");
      Bukkit.getPluginManager().disablePlugin(angelSkyEconomy);
      return;
    } 
  }

  
  public boolean createAccount(UUID uuid) {
    PlayerBalance playerBalance = new PlayerBalance(uuid, angelSkyEconomy.getConfigHandler().getStartingBalance());
    try {
      PreparedStatement statement = this.sql.getConnection().prepareStatement("INSERT INTO Economy (UUID, Balance) VALUES (?, ?);");
      
      statement.setString(1, playerBalance.getUUID().toString());
      statement.setDouble(2, playerBalance.getBalance());
      statement.executeUpdate();
      statement.close();
    } catch (SQLException e) {
      angelSkyEconomy.warn(e.getMessage());
      return false;
    } 
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
    try {
      PreparedStatement statement = this.sql.getConnection().prepareStatement("DELETE FROM Economy WHERE UUID=?");
      
      statement.setString(1, uuid.toString());
      statement.executeUpdate();
      statement.close();
    } catch (SQLException e) {
      angelSkyEconomy.warn(e.getMessage());
      return false;
    } 
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
    try {
      PreparedStatement statement = this.sql.getConnection().prepareStatement("UPDATE Economy SET UUID=?, Balance=? WHERE UUID=?");
      
      statement.setString(1, uuid.toString());
      statement.setDouble(2, amount);
      statement.setString(3, uuid.toString());
      statement.executeUpdate();
      statement.close();
    } catch (SQLException e) {
      angelSkyEconomy.warn(e.getMessage());
      return false;
    } 
    return true;
  }

  
  public boolean has(UUID uuid, double amount) {
    return (getBalance(uuid).getBalance() >= amount);
  }

  
  public PlayerBalance getBalance(UUID uuid) {
    try {
      PreparedStatement statement = this.sql.getConnection().prepareStatement("SELECT * FROM Economy WHERE UUID=?");
      
      statement.setString(1, uuid.toString());
      ResultSet result = statement.executeQuery();
      result.next();
      double balance = result.getDouble("Balance");
      return new PlayerBalance(uuid, balance);
    } catch (SQLException e) {
      return new PlayerBalance(uuid, 0.0D);
    } 
  }

  
  public List<PlayerBalance> getPlayers() {
    try {
      List<PlayerBalance> playerData = new ArrayList<>();
      Statement statement = this.sql.getConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM Economy;");
      while (result.next()) {
        UUID uuid = UUID.fromString(result.getString("UUID"));
        double balance = result.getDouble("Balance");
        playerData.add(new PlayerBalance(uuid, balance));
      } 
      return playerData;
    } catch (SQLException e) {
      angelSkyEconomy.warn(e.getMessage());
      return null;
    } 
  }
}

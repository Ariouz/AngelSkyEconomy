package fr.angelsky.angelskyeconomy.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL
{
  public String host;
  public int port;
  public String database;
  public String username;
  public String password;
  public Connection connection;
  
  public MySQL(String host, int port, String database, String username, String password) {
    this.host = host;
    this.port = port;
    this.database = database;
    this.username = username;
    this.password = password;
  }
  
  public void connect() throws ClassNotFoundException, SQLException {
    if (!isConnected()) {
      this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true&useSSL=false", this.username, this.password);
    }
  }
  
  public void disconnect() throws SQLException {
    if (isConnected()) {
      this.connection.close();
    }
  }
  
  public boolean isConnected() {
    return !(this.connection == null);
  }
  
  public Connection getConnection() {
    return this.connection;
  }
}

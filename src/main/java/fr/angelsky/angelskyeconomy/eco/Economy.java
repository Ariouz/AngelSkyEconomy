package fr.angelsky.angelskyeconomy.eco;

import java.util.List;
import java.util.UUID;

public interface Economy {
  boolean createAccount(UUID paramUUID);
  
  boolean hasAccount(UUID paramUUID);
  
  boolean delete(UUID paramUUID);
  
  boolean withdraw(UUID paramUUID, double paramDouble);
  
  boolean deposit(UUID paramUUID, double paramDouble);
  
  boolean set(UUID paramUUID, double paramDouble);
  
  boolean has(UUID paramUUID, double paramDouble);
  
  PlayerBalance getSQLBalance(UUID paramUUID);
  PlayerBalance getBalance(UUID paramUUID);

  List<PlayerBalance> getPlayers();

  void saveTempEco(TempPlayerEco tempPlayerEco);

  void setBalance(UUID uniqueId, double amount);
}

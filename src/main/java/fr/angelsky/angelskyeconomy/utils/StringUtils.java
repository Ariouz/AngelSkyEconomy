package fr.angelsky.angelskyeconomy.utils;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import fr.angelsky.angelskyeconomy.AngelSkyEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StringUtils {
  
  private final AngelSkyEconomy angelSkyEconomy;

  public StringUtils(AngelSkyEconomy angelSkyEconomy){
    this.angelSkyEconomy = angelSkyEconomy;
  }
  
  private String getPrefix() {
    return color(angelSkyEconomy.getConfig().getString("messages.prefix"));
  }
  
  public String color(String msg) {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }
  
  public void sendMessage(CommandSender sender, List<String> message) {
    for (String line : message) {
      line = line.replace("%prefix%", getPrefix());
      sender.sendMessage(color(line));
    } 
  }
  
  public void sendMessage(Player player, List<String> message) {
    for (String line : message) {
      line = line.replace("%prefix%", getPrefix());
      player.sendMessage(color(line));
    } 
  }
  
  public void sendMessage(Player player, List<String> message, ImmutableMap<String, String> placeholders) {
    for (String line : message) {
      line = line.replace("%prefix%", getPrefix());
      for (Map.Entry<String, String> placeholder : (Iterable<Map.Entry<String, String>>)placeholders.entrySet()) {
        line = line.replace(placeholder.getKey(), placeholder.getValue());
      }
      player.sendMessage(color(line));
    } 
  }
  
  public void sendMessage(CommandSender sender, List<String> message, ImmutableMap<String, String> placeholders) {
    for (String line : message) {
      line = line.replace("%prefix%", getPrefix());
      for (Map.Entry<String, String> placeholder : (Iterable<Map.Entry<String, String>>)placeholders.entrySet()) {
        line = line.replace(placeholder.getKey(), placeholder.getValue());
      }
      sender.sendMessage(color(line));
    } 
  }
  
  public void sendConfigMessage(CommandSender sender, String path) {
    for (String line : angelSkyEconomy.getConfig().getStringList(path)) {
      line = line.replace("%prefix%", getPrefix());
      sender.sendMessage(color(line));
    } 
  }
  
  public void sendConfigMessage(Player player, String path) {
    for (String line : angelSkyEconomy.getConfig().getStringList(path)) {
      line = line.replace("%prefix%", getPrefix());
      player.sendMessage(color(line));
    } 
  }
  
  public void sendConfigMessage(Player player, String path, ImmutableMap<String, String> placeholders) {
    for (String line : angelSkyEconomy.getConfig().getStringList(path)) {
      line = line.replace("%prefix%", getPrefix());
      for (Map.Entry<String, String> placeholder : (Iterable<Map.Entry<String, String>>)placeholders.entrySet()) {
        line = line.replace(placeholder.getKey(), placeholder.getValue());
      }
      player.sendMessage(color(line));
    } 
  }
  
  public void sendConfigMessage(CommandSender sender, String path, ImmutableMap<String, String> placeholders) {
    for (String line : angelSkyEconomy.getConfig().getStringList(path)) {
      line = line.replace("%prefix%", getPrefix());
      for (Map.Entry<String, String> placeholder : (Iterable<Map.Entry<String, String>>)placeholders.entrySet()) {
        line = line.replace(placeholder.getKey(), placeholder.getValue());
      }
      sender.sendMessage(color(line));
    } 
  }
}

package fr.angelsky.angelskyeconomy.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public abstract class CommandExecutor {
  private String permission = new String();
  private String name = new String();
  private List<Integer> lengths = new ArrayList<>();
  private List<String> usage = new ArrayList<>();
  private List<String> aliases = new ArrayList<>();
  private boolean both = false;
  private boolean console = false;
  private boolean player = false;
  
  public abstract void execute(CommandSender paramCommandSender, String[] paramArrayOfString);
  
  public abstract List<String> onTabComplete(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString);
  
  public String getPermission() {
    return this.permission;
  }
  
  public void setPermission(String permission) {
    this.permission = permission;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public boolean isBoth() {
    return this.both;
  }
  
  public void setBoth(boolean both) {
    this.both = both;
  }
  
  public boolean isConsole() {
    return this.console;
  }
  
  public void setConsole(boolean console) {
    this.console = console;
  }
  
  public boolean isPlayer() {
    return this.player;
  }
  
  public void setPlayer(boolean player) {
    this.player = player;
  }
  
  public List<Integer> getLengths() {
    return this.lengths;
  }
  
  public void setLengths(List<Integer> lengths) {
    this.lengths = lengths;
  }
  
  public List<String> getAliases() {
    return this.aliases;
  }
  
  public void setAliases(List<String> aliases) {
    this.aliases = aliases;
  }
  
  public List<String> getUsage() {
    return this.usage;
  }
  
  public void setUsage(List<String> list) {
    this.usage = list;
  }
}

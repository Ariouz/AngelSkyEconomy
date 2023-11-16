package fr.angelsky.angelskyeconomy;

import fr.angelsky.angelskyeconomy.commands.BalanceCommand;
import fr.angelsky.angelskyeconomy.commands.BalanceTopCommand;
import fr.angelsky.angelskyeconomy.commands.PayCommand;
import fr.angelsky.angelskyeconomy.commands.money.MoneyCommandHandler;
import fr.angelsky.angelskyeconomy.data.ConfigHandler;
import fr.angelsky.angelskyeconomy.eco.Economy;
import fr.angelsky.angelskyeconomy.eco.SQLEconomy;
import fr.angelsky.angelskyeconomy.eco.VaultImpl;
import fr.angelsky.angelskyeconomy.eco.YamlEconomy;
import fr.angelsky.angelskyeconomy.listeners.PlayerJoinListener;
import fr.angelsky.angelskyeconomy.runnables.BalanceTopRunnable;
import fr.angelsky.angelskyeconomy.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.NumberFormat;
import java.util.*;

public final class AngelSkyEconomy extends JavaPlugin {

    private MoneyCommandHandler moneyCommandHandler;
    private BalanceTopRunnable balanceTopRunnable;
    private final Map<String, String> sqlColumns = new HashMap<>();
    private VaultImpl vaultImpl;
    private Economy eco;
    private Map<String, Integer> suffixes = new HashMap<>();
    private StringUtils stringUtils;
    private ConfigHandler configHandler;

    public void onEnable() {
        saveDefaultConfig();

        this.stringUtils = new StringUtils(this);
        this.configHandler = new ConfigHandler(this);
        addSqlColumns();

        suffixes = configHandler.getSuffixes();
        vaultImpl = new VaultImpl(this);

        balanceTopRunnable = new BalanceTopRunnable(this);
        balanceTopRunnable.start(configHandler.getBalanceTopInterval());

        if (!setupEconomy()) {
            disable("API Vault non trouve!");
            return;
        }
        getLogger().info("Vault API trouvee, AngelSkyEconomy enregistre.");
        moneyCommandHandler = new MoneyCommandHandler(this);
        Objects.requireNonNull(getCommand("money")).setExecutor(moneyCommandHandler);
        Objects.requireNonNull(getCommand("money")).setTabCompleter(moneyCommandHandler);

        Objects.requireNonNull(getCommand("balance")).setExecutor(new BalanceCommand(this));
        Objects.requireNonNull(getCommand("pay")).setExecutor(new PayCommand(this));
        Objects.requireNonNull(getCommand("balancetop")).setExecutor(new BalanceTopCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        if (configHandler.isSQL())
            eco = new SQLEconomy(this);
        else
            eco = new YamlEconomy(this);
    }

    public void onDisable() {}

    private void addSqlColumns() {
        sqlColumns.put("Balance", "DECIMAL(65, 2) NOT NULL DEFAULT " + getConfig().getDouble("startingBalance"));
    }

    public StringUtils getStringUtils() {
        return stringUtils;
    }

    public String getPath() {
        return getDataFolder().getAbsolutePath();
    }

    public void warn(String message) {
        getLogger().warning(message);
    }

    public void disable(String message) {
        warn(message);
        Bukkit.getPluginManager().disablePlugin(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, vaultImpl, this, ServicePriority.Highest);
        return true;
    }

    public double getAmountFromString(String string) {
        int mult = 0;
        for (Map.Entry<String, Integer> suffix : suffixes.entrySet()) {
            if (string.endsWith(suffix.getKey())) {
                string = string.substring(0, string.length() - 1);
                mult = suffix.getValue();
            }
        }
        double pow = Math.pow(10.0D, mult);
        return Math.round(Double.parseDouble(string) * 100.0D * pow) / 100.0D * pow * pow;
    }

    public ArrayList<OfflinePlayer> getPlayersFromString(CommandSender sender, String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);

        if (name.equals("@a")) {
            ArrayList<OfflinePlayer> players = new ArrayList<>(new ArrayList<>(Bukkit.getOnlinePlayers()));
            if (sender instanceof OfflinePlayer)
                players.remove(sender);
            return players;
        }
        return new ArrayList<>(Collections.singletonList(player));
    }

    public String format(double amount) {
        Locale locale = configHandler.getLocale();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String formatted = numberFormat.format(amount).replace("&nbsp", " ").replace(" ", " ");
        if (configHandler.isCustomSymbol()) {
            formatted = formatted.replace(Currency.getInstance(locale).getSymbol(locale), configHandler.getCustomSymbol());
        }
        return formatted;
    }

    public void setSuffixes(Map<String, Integer> suffixesFromConfig) {
        suffixes = suffixesFromConfig;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public Map<String, Integer> getSuffixes() {
        return suffixes;
    }

    public Map<String, String> getSQLColumns() {
        return sqlColumns;
    }

    public BalanceTopRunnable getBalanceTopRunnable() {
        return balanceTopRunnable;
    }

    public void setBalanceTopRunnable(BalanceTopRunnable balanceTopRunnable) {
        this.balanceTopRunnable = balanceTopRunnable;
    }

    public MoneyCommandHandler getMoneyCommandHandler() {
        return moneyCommandHandler;
    }
    public Economy getEco() {
        return eco;
    }
}

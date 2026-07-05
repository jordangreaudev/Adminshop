package fr.pharahosting.admionshop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Economy econ = null;
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if (!setupEconomy()) {
            getLogger().severe("Vault non trouvé !");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("shop").setExecutor(new ShopCommand());
        getCommand("shopreload").setExecutor(new ShopReloadCommand());
        getServer().getPluginManager().registerEvents(new ShopListener(), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() { return econ; }
    public static Main getInstance() { return instance; }
}
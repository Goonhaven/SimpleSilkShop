package me.goonhaven.SimpleSilkShop;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.permission.Permission;

import net.milkbowl.vault.economy.Economy;

public class SimpleSilkShop extends JavaPlugin {
	private Economy economy;
	private static final Logger log = Logger.getLogger("Minecraft");
    private static Permission perms = null;
	

	public void onEnable() {

		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		else
			getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Integrated SimpleSilkShop with Vault successfully.");
		setupPermissions();
		// Make sure events registered AFTER eco setup so you don't pass a null pointer in.
		getServer().getPluginManager().registerEvents(new SignClickEvent(this), this);
		getServer().getPluginManager().registerEvents(new SignPlaceEvent(), this);
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Loaded SimpleSilkShop");
	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "SimpleSilkShop shut down");
	}
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
    
    public Economy getEconomy() {
    	return economy;
    }
}
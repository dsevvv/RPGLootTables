package ca.rpgcraft.rpgloottables;

import ca.rpgcraft.rpgloottables.command.Commands;
import ca.rpgcraft.rpgloottables.database.Database;
import ca.rpgcraft.rpgloottables.listeners.LootGenerate;
import ca.rpgcraft.rpgloottables.listeners.Menu;
import ca.rpgcraft.rpgloottables.listeners.RedeemBankVoucher;
import ca.rpgcraft.rpgloottables.listeners.RedeemLootVoucher;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;

import ca.rpgcraft.rpgloottables.util.VaultHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

public final class RPGLootTables extends JavaPlugin {

    private static HashMap<Player, PlayerMenuManager> playerMenuUtilityMap;
    private Database db;

    private boolean isVault = false;
    private VaultHandler vaultHandler;

    /**
     * Called on server startup or reload
     */
    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        saveDefaultConfig();

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eRunning startup..."));

        db = new Database();
        if(!db.checkLicense()) return;

        try{
            db.createDatabase();
            db.runnableStartSave();
            db.loadTables();
        }catch (SQLException e){
            e.printStackTrace();
        }

        Commands commands = new Commands();
        playerMenuUtilityMap = new HashMap<>(); //This map will hold the information within a menu instance for each player

        //registering commands
        getCommand("cl").setExecutor(commands);
        getCommand("cl").setTabCompleter(commands);

        //hooking into Vault
        isVault = Bukkit.getPluginManager().getPlugin("Vault") != null;
        if(isVault){
            vaultHandler = new VaultHandler();
            if (!vaultHandler.setupEconomy()) {
                getLogger().severe("Disabled due to no economy provider being found through Vault. Please install an economy plugin that is compatible with Vault.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        //registering listeners
        Bukkit.getPluginManager().registerEvents(new Menu(), this);
        Bukkit.getPluginManager().registerEvents(new LootGenerate(), this);
        Bukkit.getPluginManager().registerEvents(new RedeemLootVoucher(), this);
        if(isVault)
            Bukkit.getPluginManager().registerEvents(new RedeemBankVoucher(), this);

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eHello Minecraft!"));
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eTime Elapsed: &b" + (System.currentTimeMillis() - startTime) + " &ems"));
    }

    /**
     * Called on server shutdown or reload
     */
    @Override
    public void onDisable() {
        if(db.isConnected()){
            db.saveTables();
            db.disconnect();
        }
        else{
            getLogger().severe("Database is not connected! Cannot save!");
        }
        long startTime = System.currentTimeMillis();

        playerMenuUtilityMap = null; //cuz static

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eGoodbye Minecraft!"));
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eTime Elapsed: &b" + (System.currentTimeMillis() - startTime) + " &ems"));
    }

    /**
     * Will create a PlayerMenuUtility for the provided Player if one does not exist.
     * @param p Player whose PlayerMenuUtility to return
     * @return PlayerMenuUtility associated with provided Player
     */
    public static PlayerMenuManager getPlayerMenuUtility(Player p){
        if(!playerMenuUtilityMap.containsKey(p)) //pretty sure this check is redundant but just making sure
            playerMenuUtilityMap.put(p, new PlayerMenuManager(p));

        return playerMenuUtilityMap.get(p);
    }

    public static RPGLootTables getInstance(){
        return RPGLootTables.getPlugin(RPGLootTables.class);
    }

    public boolean isVault() {
        return isVault;
    }

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }
}

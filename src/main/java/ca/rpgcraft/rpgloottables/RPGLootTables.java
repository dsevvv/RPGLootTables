package ca.rpgcraft.rpgloottables;

import ca.rpgcraft.rpgloottables.command.admin.MainMenu;
import ca.rpgcraft.rpgloottables.database.Database;
import ca.rpgcraft.rpgloottables.listeners.LootGenerate;
import ca.rpgcraft.rpgloottables.listeners.Menu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

public final class RPGLootTables extends JavaPlugin {

    private static HashMap<Player, PlayerMenuManager> playerMenuUtilityMap;
    private Database db;

    /**
     * Called on server startup or reload
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eRunning startup..."));

        db = new Database();

        try{
            db.createDatabase();
            db.runnableStartSave();
            db.loadTables();
        }catch (SQLException e){
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        MainMenu mainMenu = new MainMenu();
        playerMenuUtilityMap = new HashMap<>(); //This map will hold the information within a menu instance for each player

        //registering commands
        getCommand("rpgloot").setExecutor(mainMenu);
        getCommand("rpgl").setExecutor(mainMenu);
        getCommand("rloot").setExecutor(mainMenu);
        getCommand("rl").setExecutor(mainMenu);

        //registering listeners
        Bukkit.getPluginManager().registerEvents(new Menu(), this);
        Bukkit.getPluginManager().registerEvents(new LootGenerate(), this);

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eHello Minecraft!"));
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eTime Elapsed: &b" + (System.currentTimeMillis() - startTime) + " &ems"));
    }

    /**
     * Called on server shutdown or reload
     */
    @Override
    public void onDisable() {
        db.disconnect();
        db.saveTables();
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
}

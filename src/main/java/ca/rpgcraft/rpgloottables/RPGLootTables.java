package ca.rpgcraft.rpgloottables;

import ca.rpgcraft.rpgloottables.command.admin.MainMenuCommand;
import ca.rpgcraft.rpgloottables.listeners.LootGenerateListener;
import ca.rpgcraft.rpgloottables.listeners.MenuListener;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class RPGLootTables extends JavaPlugin {

    private static HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap;

    /**
     * Called on server startup or reload
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eRunning startup..."));

        long startTime = System.currentTimeMillis();
        MainMenuCommand mainMenuCommand = new MainMenuCommand();
        playerMenuUtilityMap = new HashMap<>(); //This map will hold the information within a menu instance for each player

        //registering commands
        getCommand("rpgloot").setExecutor(mainMenuCommand);
        getCommand("rpgl").setExecutor(mainMenuCommand);
        getCommand("rloot").setExecutor(mainMenuCommand);
        getCommand("rl").setExecutor(mainMenuCommand);

        //registering listeners
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new LootGenerateListener(), this);

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eHello Minecraft!"));
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eTime Elapsed: &b" + (System.currentTimeMillis() - startTime) + " &ems"));
    }

    /**
     * Called on server shutdown or reload
     */
    @Override
    public void onDisable() {
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
    public static PlayerMenuUtility getPlayerMenuUtility(Player p){
        if(!playerMenuUtilityMap.containsKey(p)) //pretty sure this check is redundant but just making sure
            playerMenuUtilityMap.put(p, new PlayerMenuUtility(p));

        return playerMenuUtilityMap.get(p);
    }
}

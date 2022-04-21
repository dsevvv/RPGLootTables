package ca.rpgcraft.rpgloottables;

import ca.rpgcraft.rpgloottables.command.admin.MainMenu;
import ca.rpgcraft.rpgloottables.listeners.LootGenerate;
import ca.rpgcraft.rpgloottables.listeners.Menu;
import ca.rpgcraft.rpgloottables.util.PlayerMenu;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class RPGLootTables extends JavaPlugin {

    private static HashMap<Player, PlayerMenu> playerMenuUtilityMap;

    /**
     * Called on server startup or reload
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eRunning startup..."));

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
    public static PlayerMenu getPlayerMenuUtility(Player p){
        if(!playerMenuUtilityMap.containsKey(p)) //pretty sure this check is redundant but just making sure
            playerMenuUtilityMap.put(p, new PlayerMenu(p));

        return playerMenuUtilityMap.get(p);
    }

    private void test(){
        HashMap<String, VanillaLootTable> vanillaTablesMap = TableList.getLoadedVanillaTables();
        vanillaTablesMap.keySet().forEach(key -> {
            getLogger().info(String.format("\nName: %s\nKeep Vanilla Loot: %s", vanillaTablesMap.get(key).getVanillaTableName(), vanillaTablesMap.get(key).isKeepVanillaLoot()));
            getLogger().info("Associated Tables:\n");
            vanillaTablesMap.get(key).getAssociatedTableList().forEach(customLootTableUtility -> getLogger().info(customLootTableUtility.getName() + "\n"));
        });
    }
}

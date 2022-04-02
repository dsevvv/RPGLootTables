package ca.rpgcraft.rpgloottables;

import ca.rpgcraft.rpgloottables.command.admin.MainMenuCommand;
import ca.rpgcraft.rpgloottables.listeners.MenuListener;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class RPGLootTables extends JavaPlugin {

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        MainMenuCommand mainMenuCommand = new MainMenuCommand();

        getCommand("rpgloot").setExecutor(mainMenuCommand);
        getCommand("rpgl").setExecutor(mainMenuCommand);
        getCommand("rloot").setExecutor(mainMenuCommand);
        getCommand("rl").setExecutor(mainMenuCommand);

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p){
        if(!playerMenuUtilityMap.containsKey(p))
            playerMenuUtilityMap.put(p, new PlayerMenuUtility(p));

        return playerMenuUtilityMap.get(p);
    }
}

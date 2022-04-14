package ca.rpgcraft.rpgloottables;

import ca.rpgcraft.rpgloottables.command.admin.MainMenuCommand;
import ca.rpgcraft.rpgloottables.listeners.LootGenerateListener;
import ca.rpgcraft.rpgloottables.listeners.MenuListener;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import ca.rpgcraft.rpgloottables.util.TableListUtility;
import ca.rpgcraft.rpgloottables.util.VanillaLootTableUtility;
import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

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

    private void test(){
        HashMap<String, VanillaLootTableUtility> vanillaTablesMap = TableListUtility.getLoadedVanillaTables();
        vanillaTablesMap.keySet().forEach(key -> {
            getLogger().info(String.format("\nName: %s\nKeep Vanilla Loot: %s", vanillaTablesMap.get(key).getVanillaTableName(), vanillaTablesMap.get(key).isKeepVanillaLoot()));
            getLogger().info("Associated Tables:\n");
            vanillaTablesMap.get(key).getAssociatedTableList().forEach(customLootTableUtility -> getLogger().info(customLootTableUtility.getName() + "\n"));
        });
    }
}

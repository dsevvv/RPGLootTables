package ca.rpgcraft.rpgloottables.database;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class Database {

    private final String path = "plugins/RPGLootTables/rpgloot.db";
    private Connection conn;

    //Creates a db file called rpgloot in the database file.
    public void createDatabase() throws SQLException {

        File pathFile = new File(path);

        RPGLootTables plugin = RPGLootTables.getPlugin(RPGLootTables.class);

        //first checks if there is already a db file in the database folder or not
        if(!pathFile.isFile()){
            String vanilla_table = createVanillaTable();
            String custom_table = createCustomLootTable();
            String vanilla_custom_table = createVanillaCustomLootTable();
            String item_table = createItemsTable();
            try{
                conn = DriverManager.getConnection("jdbc:sqlite:" + path);
                plugin.getLogger().warning("rpgloot.db created");
                Statement stmt = conn.createStatement();
                stmt.execute(vanilla_table);
                plugin.getLogger().warning("vanilla_table created");
                stmt.execute(custom_table);
                plugin.getLogger().warning("custom_table created");
                stmt.execute(vanilla_custom_table);
                plugin.getLogger().warning("vanilla_custom table created");
                stmt.execute(item_table);
                plugin.getLogger().warning("item_table created");
            } catch (SQLException e) {
                plugin.getLogger().severe(e.getMessage());
            }
        }
        else{
            plugin.getLogger().warning("Database file already exists");
        }
    }

    private String createVanillaTable() {
        return "CREATE TABLE IF NOT EXISTS VanillaLootTable (\n"
                + "vanilla_id text PRIMARY KEY, \n"
                + "enabled integer NOT NULL, \n"
                + "custom_names text NULL \n"
                + ");";
    }
    private String createCustomLootTable() {
        return "CREATE TABLE IF NOT EXISTS CustomLootTable (\n"
                + "custom_id text PRIMARY KEY, \n"
                + "global integer NOT NULL, \n"
                + "chance real NOT NULL, \n"
                + "minItems integer NOT NULL, \n"
                + "maxItems integer NOT NULL \n"
                + ");";
    }
    private String createVanillaCustomLootTable() {
        return "CREATE TABLE IF NOT EXISTS Vanilla_Custom (\n"
                + "vanilla_id text NOT NULL, \n"
                + "custom_id text NOT NULL, \n"
                + "PRIMARY KEY (vanilla_id, custom_id),\n"
                + "FOREIGN KEY (vanilla_id) REFERENCES VanillaLootTable (vanilla_id),\n"
                + "FOREIGN KEY (custom_id) REFERENCES CustomLootTable (custom_id)\n"
                + ");";
    }
    private String createItemsTable() {
        return "CREATE TABLE IF NOT EXISTS ItemsTable (\n"
                + "uuID text PRIMARY KEY, \n"
                + "customID text NOT NULL, \n"
                + "minAmount integer NOT NULL,\n"
                + "maxAmount integer NOT NULL,\n"
                + "itemStack text NOT NULL,\n"
                + "FOREIGN KEY (customID) REFERENCES CustomLootTable (custom_id)\n"
                + ");";
    }

    public void insertVanillaTables(){
        HashMap<String, VanillaLootTable> loadedVanillaTables = TableList.getLoadedVanillaTables();
        Set<String> keys = loadedVanillaTables.keySet();

        for(String key : keys){
            VanillaLootTable vanillaLootTable = loadedVanillaTables.get(key);
            String sql = "INSERT INTO VanillaLootTable (vanilla_id,enabled,custom_names) VALUES(?,?,?)";

            try{
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                String name = vanillaLootTable.getVanillaTableName();
                int boolInt = vanillaLootTable.isKeepVanillaLoot() ? 1 : 0;
                StringBuilder innerBuilder = new StringBuilder();
                for(CustomLootTable customLootTable : vanillaLootTable.getAssociatedTableList()){
                    innerBuilder.append(customLootTable.getName() + "|");
                }
                String namesStr = innerBuilder.toString();
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, boolInt);
                preparedStatement.setString(3, namesStr);
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void testMethod(){
        HashMap<String, VanillaLootTable> vanillaTablesMap = TableList.getLoadedVanillaTables();
        vanillaTablesMap.keySet().forEach(key -> {
            RPGLootTables.getPlugin(RPGLootTables.class).getLogger().info(String.format("\nName: %s\nKeep Vanilla Loot: %s", vanillaTablesMap.get(key).getVanillaTableName(), vanillaTablesMap.get(key).isKeepVanillaLoot()));
            RPGLootTables.getPlugin(RPGLootTables.class).getLogger().info("Associated Tables:\n");
            vanillaTablesMap.get(key).getAssociatedTableList().forEach(customLootTableUtility -> RPGLootTables.getPlugin(RPGLootTables.class).getLogger().info(customLootTableUtility.getName() + "\n"));
        });
    }
}

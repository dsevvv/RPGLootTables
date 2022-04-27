package ca.rpgcraft.rpgloottables.database;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Database {

    private final String path = "plugins/RPGLootTables/rpgloot.db";

    //Creates a db file called rpgloot in the database file.
    public void createDatabase() throws SQLException {

        File pathFile = new File(path);

        //first checks if there is already a db file in the database folder or not
        if(!pathFile.isFile()){
            String vanilla_table = createVanillaTable();
            String custom_table = createCustomLootTable();
            String vanilla_custom_table = createVanillaCustomLootTable();
            String item_table = createItemsTable();
            try{
                Connection conn = DriverManager.getConnection(path);
                RPGLootTables.getPlugin(RPGLootTables.class).getLogger().warning("rpgloot.db created");
                Statement stmt = conn.createStatement();
                stmt.execute(vanilla_table);
                RPGLootTables.getPlugin(RPGLootTables.class).getLogger().warning("vanilla_table created");
                stmt.execute(custom_table);
                RPGLootTables.getPlugin(RPGLootTables.class).getLogger().warning("custom_table created");
                stmt.execute(vanilla_custom_table);
                RPGLootTables.getPlugin(RPGLootTables.class).getLogger().warning("vanilla_custom table created");
                stmt.execute(item_table);
                RPGLootTables.getPlugin(RPGLootTables.class).getLogger().warning("item_table created");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            RPGLootTables.getPlugin(RPGLootTables.class).getLogger().warning("Database file already exists");
        }
    }

    private String createVanillaTable() {
        return "CREATE TABLE IF NOT EXISTS VanillaLootTable (\n"
                + "vanilla_id text PRIMARY KEY, \n"
                + "enabled integer NOT NULL \n"
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
                + "FOREIGN KEY (custom_id) REFERENCES CustomLootTable (custom_id),\n"
                + ");";
    }
    private String createItemsTable() {
        return "CREATE TABLE IF NOT EXISTS ItemsTable (\n"
                + "uuID text PRIMARY KEY, \n"
                + "customID text NOT NULL, \n"
                + "minAmount integer NOT NULL,\n"
                + "maxAmount integer NOT NULL,\n"
                + "itemStack text NOT NULL,\n"
                + "FOREIGN KEY (custom_id) REFERENCES CustomLootTable (custom_id),\n"
                + ");";
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

package ca.rpgcraft.rpgloottables.database;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.item.TableEntry;
import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Set;

public class Database {

    private final String path = "plugins/RPGLootTables/rpgloot.db";
    RPGLootTables plugin = RPGLootTables.getPlugin(RPGLootTables.class);
    private Connection conn;

    /**
     *
     * @throws SQLException
     */

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
                + "uniqueID integer PRIMARY KEY, \n"
                + "customID text NOT NULL, \n"
                + "weight integer NOT NULL, \n"
                + "minAmount integer NOT NULL,\n"
                + "maxAmount integer NOT NULL,\n"
                + "itemStack text NOT NULL,\n"
                + "FOREIGN KEY (customID) REFERENCES CustomLootTable (custom_id)\n"
                + ");";
    }

    private void insertVanillaTables(){
        HashMap<String, VanillaLootTable> loadedVanillaTables = TableList.getLoadedVanillaTables();
        Set<String> keys = loadedVanillaTables.keySet();

        for(String key : keys){
            VanillaLootTable vanillaLootTable = loadedVanillaTables.get(key);
            String sql = "INSERT OR REPLACE INTO VanillaLootTable (vanilla_id,enabled) VALUES(?,?)";

            try{
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                String name = vanillaLootTable.getVanillaTableName();
                int boolInt = vanillaLootTable.isKeepVanillaLoot() ? 1 : 0;
                StringBuilder innerBuilder = new StringBuilder();
                for(CustomLootTable customLootTable : vanillaLootTable.getAssociatedTableList()){
                    innerBuilder.append(customLootTable.getName() + "|");
                }
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, boolInt);
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    private void insertCustomLootTables(){
        HashMap<String, CustomLootTable> loadedCustomTables = TableList.getLoadedCustomTables();
        Set<String> keys = loadedCustomTables.keySet();

        for(String key : keys){
            CustomLootTable customLootTable = loadedCustomTables.get(key);
            String sql = "INSERT OR REPLACE INTO CustomLootTable (custom_id,global,chance,minItems,maxItems) VALUES(?,?,?,?,?)";
            try{
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                String name = customLootTable.getName();
                int boolInt = customLootTable.isGlobal() ? 1 : 0;
                double chance = customLootTable.getChance();
                int minItems = customLootTable.getMinItems();
                int maxItems = customLootTable.getMaxItems();

                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, boolInt);
                preparedStatement.setDouble(3, chance);
                preparedStatement.setInt(4, minItems);
                preparedStatement.setInt(5, maxItems);
                preparedStatement.executeUpdate();

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void insertVanillaCustomLootTables(){
        HashMap<String, VanillaLootTable> loadedVanillaTables = TableList.getLoadedVanillaTables();
        Set<String> keys = loadedVanillaTables.keySet();
        for(String key : keys){
            VanillaLootTable vanillaLootTable = loadedVanillaTables.get(key);
            for(CustomLootTable customTable: vanillaLootTable.getAssociatedTableList()){
                String sql = "INSERT OR REPLACE INTO Vanilla_Custom (vanilla_id,custom_id) VALUES(?,?)";
                try{
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    String vanillaId = vanillaLootTable.getVanillaTableName();
                    String customId = customTable.getName();
                    preparedStatement.setString(1, vanillaId);
                    preparedStatement.setString(2, customId);
                    preparedStatement.executeUpdate();

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private String itemStackEncode(ItemStack item){
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        String encodedObject = null;
        try {
            BukkitObjectOutputStream bukkitByteOut = new BukkitObjectOutputStream(byteOut);
            bukkitByteOut.writeObject(item);
            bukkitByteOut.flush();
            byte[] serializedObject = byteOut.toByteArray();
            encodedObject = new String(Base64.getEncoder().encode(serializedObject));
        } catch (IOException ignored) {
            plugin.getLogger().severe("Item Serialization Failed.");
        }
        return encodedObject;
    }

    private ItemStack decodeItemStackBytes(String bytes){
        ItemStack newItem = null;
        try{
            byte[] reserializedObject = Base64.getDecoder().decode(bytes);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(reserializedObject);
            BukkitObjectInputStream bukkitByteIs = new BukkitObjectInputStream(byteIn);
            newItem = (ItemStack) bukkitByteIs.readObject();
        } catch (IOException | ClassNotFoundException ignored){
            plugin.getLogger().severe("Byte array decoding failed.");
        }
        return newItem;
    }

    private void insertItemTables(){
        HashMap<String, CustomLootTable> loadedCustomTables = TableList.getLoadedCustomTables();
        Set<String> keys = loadedCustomTables.keySet();
        for(String key : keys){
            CustomLootTable customLootTable = loadedCustomTables.get(key);
            for(TableEntry tableEntry: customLootTable.getTableEntries()){
                String sql = "INSERT OR REPLACE INTO ItemsTable (uniqueId,customID,weight,minAmount,maxAmount,itemStack) VALUES(?,?,?,?,?,?)";
                try{
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    String customId = customLootTable.getName();
                    int weight = tableEntry.getWeight();
                    int minAmount = tableEntry.getMinAmt();
                    int maxAmount = tableEntry.getMaxAmt();
                    ItemStack itemStack = tableEntry.getItemStack();
                    String encodedItemStack = itemStackEncode(itemStack);

                    preparedStatement.setInt(1, Types.NULL);
                    preparedStatement.setString(2, customId);
                    preparedStatement.setInt(3, weight);
                    preparedStatement.setInt(4, minAmount);
                    preparedStatement.setInt(5, maxAmount);
                    preparedStatement.setString(6, encodedItemStack);
                    preparedStatement.executeUpdate();

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveTables(){
        insertVanillaTables();
        insertCustomLootTables();
        insertVanillaCustomLootTables();
        insertItemTables();
    }

    public void runnableStartSave(){
        new BukkitRunnable() {
            @Override
            public void run() {
                saveTables();
            }
        }.runTaskTimer(plugin, 20 * 60 * 120, 20 * 60 * 120); // 2 hour delay
    }
}

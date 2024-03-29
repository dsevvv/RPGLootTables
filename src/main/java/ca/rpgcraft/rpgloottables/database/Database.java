package ca.rpgcraft.rpgloottables.database;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.item.TableEntry;
import ca.rpgcraft.rpgloottables.license.AdvancedLicense;
import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Database {

    private final String path = "plugins/RPGLootTables/rpgloot.db";
    RPGLootTables plugin = RPGLootTables.getPlugin(RPGLootTables.class);
    private Connection conn;

    /**
     * createDatabase() function connects to the jdbc driver and then creates or connects to a db file depending
     * if that file exists or not. After the db file is created, it then calls the create table methods below to create
     * the necessary tables in the db that are needed.
     * @throws SQLException
     */
    //Creates a db file called rpgloot in the database file.
    public void createDatabase() throws SQLException {

        File pathFile = new File(path);

        //first checks if there is already a db file in the database folder or not
        if(!pathFile.isFile()){
            String vanilla_table = createVanillaTable();
            String custom_table = createCustomLootTable();
            String item_table = createItemsTable();
            try{
                conn = DriverManager.getConnection("jdbc:sqlite:" + path);
                plugin.getLogger().warning("rpgloot.db created");
                Statement stmt = conn.createStatement();
                stmt.execute(vanilla_table);
                plugin.getLogger().warning("vanilla_table created");
                stmt.execute(custom_table);
                plugin.getLogger().warning("custom_table created");
                stmt.execute(item_table);
                plugin.getLogger().warning("item_table created");
            } catch (SQLException e) {
                plugin.getLogger().severe(e.getMessage());
            }
        }
        else{
            plugin.getLogger().warning("Database file already exists");
            conn = DriverManager.getConnection("jdbc:sqlite:" + path);
        }
    }

    /**
     * createVanillaTable function returns a SQL statement that creates the vanilla table in the db file
     * @return SQL statment to create Vanilla table
     */
    private String createVanillaTable() {
        return "CREATE TABLE IF NOT EXISTS VanillaLootTable (\n"
                + "vanilla_id text PRIMARY KEY, \n"
                + "enabled integer NOT NULL, \n"
                + "customNames text NULL \n"
                + ");";
    }

    /**
     * createCustomLootTable function returns a SQL statement that creates the custom table in the db file
     * @return SQL statment to create Custom table
     */
    private String createCustomLootTable() {
        return "CREATE TABLE IF NOT EXISTS CustomLootTable (\n"
                + "custom_id text PRIMARY KEY, \n"
                + "globalChest integer NOT NULL, \n"
                + "globalMob integer NOT NULL, \n"
                + "chance real NOT NULL, \n"
                + "minItems integer NOT NULL, \n"
                + "maxItems integer NOT NULL \n"
                + ");";
    }

    /**
     * createItemsLootTable function returns a SQL statement that creates the items table in the db file
     * @return SQL statment to create items table
     */
    private String createItemsTable() {
        return "CREATE TABLE IF NOT EXISTS ItemsTable (\n"
                + "unique_id text PRIMARY KEY, \n"
                + "custom_id text NOT NULL, \n"
                + "weight integer NOT NULL, \n"
                + "minAmount integer NOT NULL,\n"
                + "maxAmount integer NOT NULL,\n"
                + "itemStack text NOT NULL,\n"
                + "FOREIGN KEY (custom_id) REFERENCES CustomLootTable (custom_id)\n"
                + ");";
    }

    /**
     * insertVanillaTables function inserts the vanilla tables from memory into the vanilla table in the db file.
     */
    private void insertVanillaTables(){
        HashMap<String, VanillaLootTable> loadedVanillaTables = TableList.getLoadedVanillaTables();
        Set<String> keys = loadedVanillaTables.keySet();

        for(String key : keys){
            VanillaLootTable vanillaLootTable = loadedVanillaTables.get(key);
            String sql = "INSERT OR REPLACE INTO VanillaLootTable (vanilla_id,enabled, customNames) VALUES(?,?,?)";

            try{
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                String name = vanillaLootTable.getVanillaTableName();
                int boolInt = vanillaLootTable.isKeepVanillaLoot() ? 1 : 0;
                StringBuilder innerBuilder = new StringBuilder();
                for(CustomLootTable customLootTable : vanillaLootTable.getAssociatedTableList()){
                    //if custom loot table no longer exists in TableList, it will be skipped
                    if(!TableList.getLoadedCustomTables().containsKey(customLootTable.getName())){
                        continue;
                    }
                    innerBuilder.append(customLootTable.getName()).append("|");
                }
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, boolInt);
                preparedStatement.setString(3, String.valueOf(innerBuilder));
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * insertCustomLootTables function inserts the custom tables associated with each vanilla table from
     * memory into the custom table in the db file.
     */
    private void insertCustomLootTables(){
        HashMap<String, CustomLootTable> loadedCustomTables = TableList.getLoadedCustomTables();
        Set<String> keys = loadedCustomTables.keySet();

        for(String key : keys){
            CustomLootTable customLootTable = loadedCustomTables.get(key);
            String sql = "INSERT OR REPLACE INTO CustomLootTable (custom_id,globalChest,globalMob,chance,minItems,maxItems) VALUES(?,?,?,?,?,?)";
            try{
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                String name = customLootTable.getName();
                int boolInt = customLootTable.isGlobalChest() ? 1 : 0;
                int boolGlobalChest = customLootTable.isGlobalChest() ? 1 : 0;
                int boolGlobalMob = customLootTable.isGlobalMob() ? 1 : 0;
                double chance = customLootTable.getChance();
                int minItems = customLootTable.getMinItems();
                int maxItems = customLootTable.getMaxItems();

                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, boolGlobalChest);
                preparedStatement.setInt(3, boolGlobalMob);
                preparedStatement.setDouble(4, chance);
                preparedStatement.setInt(5, minItems);
                preparedStatement.setInt(6, maxItems);
                preparedStatement.executeUpdate();

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * insertItemTables function loops through all of the items that are used in each custom loot table and adds everything
     * into the ItemsTable in the db file.
     * @return ItemStack string
     */
    private void insertTableEntries(){
        HashMap<String, CustomLootTable> loadedCustomTables = TableList.getLoadedCustomTables();
        Set<String> keys = loadedCustomTables.keySet();
        for(String key : keys){
            CustomLootTable customLootTable = loadedCustomTables.get(key);
            for(TableEntry tableEntry: customLootTable.getTableEntries()){
                String sql = "INSERT OR REPLACE INTO ItemsTable (unique_id,custom_id,weight,minAmount,maxAmount,itemStack) VALUES(?,?,?,?,?,?)";
                try{
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    String uniqueID = tableEntry.getUniqueID().toString();
                    String customId = customLootTable.getName();
                    int weight = tableEntry.getWeight();
                    int minAmount = tableEntry.getMinAmt();
                    int maxAmount = tableEntry.getMaxAmt();
                    ItemStack itemStack = tableEntry.getItemStack();
                    String encodedItemStack = itemStackEncode(itemStack);

                    preparedStatement.setString(1, uniqueID);
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

    private void deleteMissingCustomTables() {
        Statement stmt;
        ResultSet res;
        //delete CustomTables that are not in the loadedCustomTables from the db
        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * from CustomLootTable");
            while (res.next()) {
                String custom_id = res.getString("custom_id");
                if (!TableList.getLoadedCustomTables().containsKey(custom_id)) {
                    String sql = "DELETE FROM CustomLootTable WHERE custom_id = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, custom_id);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMissingVanillaTables() {
        Statement stmt;
        ResultSet res;
        //delete VanillaTables that are not in the loadedVanillaTables from the db
        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * from VanillaLootTable");
            while (res.next()) {
                String vanilla_id = res.getString("vanilla_id");
                if (!TableList.getLoadedVanillaTables().containsKey(vanilla_id)) {
                    String sql = "DELETE FROM VanillaLootTable WHERE vanilla_id = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, vanilla_id);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteVanillaTable(VanillaLootTable vlt){
        String sql = "DELETE FROM VanillaLootTable WHERE vanilla_id = ?";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, vlt.getVanillaTableName());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteCustomTable(CustomLootTable clt){
        String sql = "DELETE FROM CustomLootTable WHERE custom_id = ?";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, clt.getName());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void retrieveCustomLootTables() {
        Statement stmt;
        ResultSet res;
        CustomLootTable clt;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * from CustomLootTable");
            while(res.next()){
                String custom_id = res.getString("custom_id");
                boolean globalChest = res.getBoolean("globalChest");
                boolean globalMob = res.getBoolean("globalMob");
                double chance = res.getDouble("chance");
                int minAmount = res.getInt("minItems");
                int maxAmount = res.getInt("maxItems");

                clt = new CustomLootTable(custom_id, new LinkedList<TableEntry>(), globalChest, globalMob, chance, minAmount, maxAmount);
                TableList.getLoadedCustomTables().put(custom_id,clt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retrieveItemTables() {
        Statement stmt;
        ResultSet res;
        TableEntry te;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * FROM ItemsTable");
            while(res.next()){
                String uniqueIDStr = res.getString("unique_id");
                UUID uniqueID = UUID.fromString(uniqueIDStr);
                String custom_id = res.getString("custom_id");
                CustomLootTable clt = TableList.getLoadedCustomTables().get(custom_id);
                if(clt == null){
                    deleteCustomTable(clt);
                    continue;
                }
                Integer weight = res.getInt("weight");
                Integer minAmount = res.getInt("minAmount");
                Integer maxAmount = res.getInt("maxAmount");
                String itemStack = res.getString("itemStack");
                ItemStack item = decodeItemStackBytes(itemStack);

                te = new TableEntry(uniqueID, item, weight,minAmount,maxAmount);

                clt.getTableEntries().add(te);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retrieveVanillaTables() {
        Statement stmt;
        ResultSet res;
        VanillaLootTable vlt;

        try {
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT * from VanillaLootTable");
            while(res.next()){
                String vanilla_id = res.getString("vanilla_id");
                boolean enabled = res.getBoolean("enabled");
                String customNames = res.getString("customNames");
                String[] customNameSplit = customNames.split("\\|");
                vlt = new VanillaLootTable(vanilla_id,new LinkedList<CustomLootTable>(),enabled);
                //skipping VanillaTable entry if they have default values
                if(vlt.isKeepVanillaLoot() && vlt.getAssociatedTableList().size() == 0){
                    continue;
                }
                for(String name: customNameSplit){
                    if(TableList.getLoadedCustomTables().containsKey(name))
                        vlt.getAssociatedTableList().add(TableList.getLoadedCustomTables().get(name));
                }
                TableList.getLoadedVanillaTables().put(vanilla_id,vlt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadTables(){
        retrieveCustomLootTables();
        retrieveItemTables();
        retrieveVanillaTables();
    }

    /**
     * saveTables function Calls all insert methods to be called at once.
     */
    public void saveTables(){
        deleteMissingCustomTables();
        deleteMissingVanillaTables();
        insertVanillaTables();
        insertTableEntries();
        insertCustomLootTables();
    }

    public boolean isConnected() {return conn != null;}

    public void disconnect(){
        if (isConnected()){
            try {
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * runnableStartSave is a runnable that runs every 2 hours (1min for testing purposes) so save all tables in memory.
     */
    public void runnableStartSave(){
        new BukkitRunnable() {
            @Override
            public void run() {
                saveTables();
            }
        }.runTaskTimer(plugin, 20 * 60 * 2, 20 * 60 * 2); // 2 hour delay
    }

    public boolean checkLicense(){
        String keyHex = "6c6963656e73652d6b6579";
        String linkHex = "68747470733a2f2f7365767363686d696474706c7567696e732e303030776562686f73746170702e636f6d2f7665726966792e706870";
        byte[] pathBytes;
        byte[] linkBytes;
        String path = "";
        String link = "";
        try{
            pathBytes = Hex.decodeHex(keyHex.toCharArray());
            linkBytes = Hex.decodeHex(linkHex.toCharArray());
            path = new String(pathBytes, "UTF-8");
            link = new String(linkBytes, "UTF-8");
        }catch (DecoderException | UnsupportedEncodingException ignored){
        }
        String key = plugin.getConfig().getString(path);
        return new AdvancedLicense(key, link, plugin).register();
    }

    /**
     * itemStackEncode function takes the long string from the ItemStack and "compress" the string down into an array
     * of bits.
     * @return string of bits that is the compressed version of the ItemStack string
     */
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

    /**
     * decodeItemStackBytes function takes the string of bits from the itemStackEncode function and converts it back
     * into the original string.
     * @return ItemStack string
     */
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
}

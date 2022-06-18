package ca.rpgcraft.rpgloottables.util;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.item.TableEntry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.*;

public class CustomLootTable implements LootTable {

    private String name;
    private LinkedList<TableEntry> tableEntries;
    private boolean isGlobalChest;
    private boolean isGlobalMob;
    private double chance;
    private int minItems;
    private int maxItems;

    /**
     * This object holds all relevant information for custom tables.
     * On startup, custom table information will be loaded into objects from the database.
     * These objects will then be put into the map TableListUtility.getLoadedCustomTables()
     * using their name parameter as the key
     * @param name String name of custom table
     * @param tableEntries List of item entries in this table
     * @param isGlobalChest if true this table will be rolled on EVERY SINGLE CHEST loot event, PERIOD! VERY DANGEROUS!!!!
     * @param isGlobalMob if true this table will be rolled on EVERY SINGLE MOB loot event, PERIOD! VERY DANGEROUS!!!!
     * @param chance double between 0.00-100.00 percentage chance that this table will be rolled
     * @param minItems int minimum amount of items that can generate from this table
     * @param maxItems int maximum amount of items that can generate from this table
     */
    public CustomLootTable(String name, LinkedList<TableEntry> tableEntries, boolean isGlobalChest, boolean isGlobalMob, double chance, int minItems, int maxItems){
        this.name = name;
        this.tableEntries = tableEntries;
        this.isGlobalChest = isGlobalChest;
        this.isGlobalMob = isGlobalMob;
        this.chance = chance;
        this.minItems = minItems;
        this.maxItems = maxItems;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        Collection<ItemStack> finalLoot = new ArrayList<>();
        int totalWeight = 0;
        int bound1 = maxItems - minItems;
        int slots = bound1 <= 0 ? minItems : random.nextInt(bound1) + minItems;

        if(random.nextDouble(100) > chance) return finalLoot;

        for(TableEntry entry : tableEntries){
            totalWeight += entry.getWeight();
        }

        for(int i = 0; i < slots; i++){
            int roll = random.nextInt(totalWeight)+1;
            for(TableEntry entry : tableEntries){
                ItemStack itemStack = entry.getItemStack();
                int weight = entry.getWeight();
                int bound2 = entry.getMaxAmt() - entry.getMinAmt() == 0 ? 1 : entry.getMaxAmt() - entry.getMinAmt();
                int amount = random.nextInt(bound2)+ entry.getMinAmt();
                itemStack.setAmount(amount);
                if(roll <= weight){
                    finalLoot.add(itemStack);
                    break;
                }
                roll -= weight;
            }
        }

        return finalLoot;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        ArrayList<Integer> filledSlots = new ArrayList<>();
        int maxSlots = inventory.getSize();

        for(int i = 0; i < maxSlots; i++){
            if(inventory.getContents()[i] != null){
                filledSlots.add(i);
            }
        }

        for(ItemStack itemStack : populateLoot(random, context)){
            int slot;
            do{
                slot = random.nextInt(maxSlots);
            }while(filledSlots.contains(slot) && filledSlots.size() < maxSlots);
            filledSlots.add(slot);
            inventory.setItem(slot, itemStack);
        }
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(RPGLootTables.getPlugin(RPGLootTables.class), name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGlobalChest() {
        return isGlobalChest;
    }

    public boolean isGlobalMob() {
        return isGlobalMob;
    }

    public void setGlobalChest(boolean globalChest) {
        isGlobalChest = globalChest;
    }

    public void setGlobalMob(boolean globalMob) {
        isGlobalMob = globalMob;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public int getMinItems() {
        return minItems;
    }

    public void setMinItems(int minItems) {
        this.minItems = minItems;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public LinkedList<TableEntry> getTableEntries() {
        return tableEntries;
    }

    public void setTableEntries(LinkedList<TableEntry> tableEntries) {
        this.tableEntries = tableEntries;
    }

    @Override
    public String toString() {
        return "CustomLootTable{" +
                "name='" + name + '\'' +
                ", tableEntries=" + tableEntries +
                ", isGlobalChest=" + isGlobalChest +
                ", isGlobalMob=" + isGlobalMob +
                ", chance=" + chance +
                ", minItems=" + minItems +
                ", maxItems=" + maxItems +
                '}';
    }
}

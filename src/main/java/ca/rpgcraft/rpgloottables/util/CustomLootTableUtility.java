package ca.rpgcraft.rpgloottables.util;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.item.TableEntry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.*;

public class CustomLootTableUtility implements LootTable {

    private String name;
    private LinkedList<TableEntry> tableEntries;
    private final LinkedList<TableEntry> entriesCopy;
    private boolean isEnabled;
    private double chance;
    private int minItems;
    private int maxItems;

    public CustomLootTableUtility(String name, LinkedList<TableEntry> tableEntries, boolean isEnabled, double chance, int minItems, int maxItems){
        this.name = name;
        this.tableEntries = tableEntries;
        this.entriesCopy = tableEntries;
        this.isEnabled = isEnabled;
        this.chance = chance;
        this.minItems = minItems;
        this.maxItems = maxItems;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        Collection<ItemStack> finalLoot = new ArrayList<>();
        int totalWeight = 0;
        int slots = random.nextInt(maxItems-minItems)+minItems;

        if(random.nextDouble(100) > chance) return finalLoot;

        for(TableEntry entry : tableEntries){
            totalWeight += entry.getWeight();
        }

        for(int i = 0; i <= slots; i++){
            int roll = random.nextInt(totalWeight)+1;
            for(TableEntry entry : tableEntries){
                ItemStack itemStack = entry.getItemStack();
                int weight = entry.getWeight();
                int amount = random.nextInt(entry.getMaxAmt()-entry.getMinAmt())+ entry.getMinAmt();
                itemStack.setAmount(amount);
                if(roll <= weight){
                    finalLoot.add(itemStack);
                    totalWeight -= weight;
                    tableEntries.remove(itemStack);
                    break;
                }
                roll -= weight;
            }
        }

        tableEntries.clear();
        tableEntries.addAll(entriesCopy);
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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
}

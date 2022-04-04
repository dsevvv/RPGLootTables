package ca.rpgcraft.rpgloottables.util;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.*;

public class CustomLootTableUtility implements LootTable {

    private String name;
    private HashMap<Map<String, ?>, Integer> items;
    private final HashMap<Map<String, ?>, Integer> itemsCopy;
    private boolean isEnabled;
    private double chance;
    private int minItems;
    private int maxItems;

    public CustomLootTableUtility(String name, HashMap<Map<String, ?>, Integer> items, boolean isEnabled, double chance, int minItems, int maxItems){
        this.name = name;
        this.items = items;
        this.itemsCopy = items;
        this.isEnabled = isEnabled;
        this.chance = chance;
        this.minItems = minItems;
        this.maxItems = maxItems;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        Collection<ItemStack> finalLoot = new ArrayList<>();
        Set<Map<String, ?>> serializedItemMap = items.keySet();
        int totalWeight = 0;
        int slots = random.nextInt(maxItems-minItems)+minItems;

        for(int weight : items.values()){
            totalWeight += weight;
        }

        for(int i = 0; i <= slots; i++){
            int roll = random.nextInt(totalWeight)+1;
            for(Map<String, ?> serializedItemStack : serializedItemMap){
                int weight = items.get(serializedItemStack);
                ItemStack item = (ItemStack) ConfigurationSerialization.deserializeObject(serializedItemStack, ItemStack.class);
                if(roll <= weight){
                    finalLoot.add(item);
                    totalWeight -= weight;
                    items.remove(serializedItemStack);
                    break;
                }
                roll -= weight;
            }
        }

        items.putAll(itemsCopy);
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

    public HashMap<Map<String, ?>, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<Map<String, ?>, Integer> items) {
        this.items = items;
    }
}

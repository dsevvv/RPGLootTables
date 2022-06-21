package ca.rpgcraft.rpgloottables.item;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TableEntry {

    private UUID uniqueID;
    private ItemStack itemStack;
    private Integer weight;
    private Integer minAmt;
    private Integer maxAmt;

    /**
     * Custom table contents will be made up of instances of this object.
     * @param itemStack ItemStack for this entry
     * @param weight int likeliness that this entry will be chosen. Higher weight = higher chance.
     * @param minAmt int minimum amount of ItemStacks
     * @param maxAmt int maximum amount of ItemStacks
     */
    public TableEntry(ItemStack itemStack, Integer weight, Integer minAmt, Integer maxAmt){
        this.uniqueID = UUID.randomUUID();
        this.itemStack = itemStack;
        this.weight = weight;
        this.minAmt = minAmt;
        this.maxAmt = maxAmt;
    }

    public TableEntry(UUID uniqueID, ItemStack itemStack, Integer weight, Integer minAmt, Integer maxAmt){
        this.uniqueID = uniqueID;
        this.itemStack = itemStack;
        this.weight = weight;
        this.minAmt = minAmt;
        this.maxAmt = maxAmt;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(Integer minAmt) {
        this.minAmt = minAmt;
    }

    public Integer getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(Integer maxAmt) {
        this.maxAmt = maxAmt;
    }

    public UUID getUniqueID() {
        return uniqueID;
    }
}

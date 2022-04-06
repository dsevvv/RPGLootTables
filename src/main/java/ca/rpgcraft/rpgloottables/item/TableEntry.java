package ca.rpgcraft.rpgloottables.item;

import org.bukkit.inventory.ItemStack;

public class TableEntry {

    private ItemStack itemStack;
    private Integer weight;
    private Integer minAmt;
    private Integer maxAmt;

    public TableEntry(ItemStack itemStack, Integer weight, Integer minAmt, Integer maxAmt){
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
}

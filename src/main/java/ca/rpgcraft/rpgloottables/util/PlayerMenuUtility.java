package ca.rpgcraft.rpgloottables.util;

import ca.rpgcraft.rpgloottables.item.TableEntry;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class PlayerMenuUtility {

    private Player owner;
    private String lootTableName;
    private boolean isEnabled;
    private double chance;
    private int minTableItems;
    private int maxTableItems;
    private LinkedList<TableEntry> tableEntries;
    private LinkedList<CustomLootTableUtility> associatedTables;

    public PlayerMenuUtility(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getLootTableName() {
        return lootTableName;
    }

    public void setLootTableName(String lootTableName) {
        this.lootTableName = lootTableName;
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

    public int getMinTableItems() {
        return minTableItems;
    }

    public void setMinTableItems(int minTableItems) {
        this.minTableItems = minTableItems;
    }

    public int getMaxTableItems() {
        return maxTableItems;
    }

    public void setMaxTableItems(int maxTableItems) {
        this.maxTableItems = maxTableItems;
    }

    public LinkedList<TableEntry> getTableEntries() {
        return tableEntries;
    }

    public void setTableEntries(LinkedList<TableEntry> tableEntries) {
        this.tableEntries = tableEntries;
    }

    public LinkedList<CustomLootTableUtility> getAssociatedTables() {
        return associatedTables;
    }

    public void setAssociatedTables(LinkedList<CustomLootTableUtility> associatedTables) {
        this.associatedTables = associatedTables;
    }
}

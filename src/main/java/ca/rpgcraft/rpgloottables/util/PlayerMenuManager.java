package ca.rpgcraft.rpgloottables.util;

import ca.rpgcraft.rpgloottables.item.TableEntry;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class PlayerMenuManager {

    private final Player owner;
    private String lootTableName;
    private boolean isGlobalChest;
    private boolean isGlobalMob;
    private double chance;
    private int minTableItems;
    private int maxTableItems;
    private LinkedList<TableEntry> tableEntries;
    private LinkedList<CustomLootTable> associatedTables;

    /**
     * Creates an object that will store information for the player while they are navigating the menu.
     * Input data will be read from this object and saved to the appropriate locations.
     * @param owner Player that owns this instance.
     */
    public PlayerMenuManager(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public String getLootTableName() {
        return lootTableName;
    }

    public void setLootTableName(String lootTableName) {
        this.lootTableName = lootTableName;
    }

    public boolean isGlobalChest() {
        return isGlobalChest;
    }

    public void setGlobalChest(boolean globalChest) {
        isGlobalChest = globalChest;
    }

    public boolean isGlobalMob() {
        return isGlobalMob;
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

    public LinkedList<CustomLootTable> getAssociatedTables() {
        return associatedTables;
    }

    public void setAssociatedTables(LinkedList<CustomLootTable> associatedTables) {
        this.associatedTables = associatedTables;
    }
}

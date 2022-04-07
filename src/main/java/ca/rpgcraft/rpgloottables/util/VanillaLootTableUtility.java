package ca.rpgcraft.rpgloottables.util;

import java.util.LinkedList;

public class VanillaLootTableUtility {

    private final String vanillaTableName;
    private LinkedList<CustomLootTableUtility> associatedTableList;
    private boolean keepVanillaLoot;

    /**
     * Used to hold information for a vanilla loot table.
     * @param vanillaTableName String name of the table as registered in Minecraft
     * @param associatedTableList List of custom tables that will roll along with this vanilla table
     * @param keepVanillaLoot if false the loot determined by Minecraft will not generate
     */
    public VanillaLootTableUtility(String vanillaTableName, LinkedList<CustomLootTableUtility> associatedTableList, boolean keepVanillaLoot){
        this.vanillaTableName = vanillaTableName;
        this.associatedTableList = associatedTableList;
        this.keepVanillaLoot = keepVanillaLoot;
    }

    public String getVanillaTableName() {
        return vanillaTableName;
    }

    public LinkedList<CustomLootTableUtility> getAssociatedTableList() {
        return associatedTableList;
    }

    public void setAssociatedTableList(LinkedList<CustomLootTableUtility> associatedTableList) {
        this.associatedTableList = associatedTableList;
    }

    public boolean isKeepVanillaLoot() {
        return keepVanillaLoot;
    }

    public void setKeepVanillaLoot(boolean keepVanillaLoot) {
        this.keepVanillaLoot = keepVanillaLoot;
    }
}

package ca.rpgcraft.rpgloottables.hook.worldguard;

import ca.rpgcraft.rpgloottables.RPGLootTables;

import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

import java.util.Random;

public class RegionLoot {

    private static final boolean DEBUG = false;
    private static final RPGLootTables plugin = RPGLootTables.getInstance();

    public static void onLootGenerate(LootGenerateEvent event){
        if(event.getEntity() == null)
            return;

        ApplicableRegionSet regions = plugin.getWorldGuardHandler().getRegionsStandingIn(event.getEntity());
        regions.getRegions().forEach(region -> {
            if(TableList.getLoadedVanillaTables().containsKey(region.getId())){
                VanillaLootTable vlt = TableList.getLoadedVanillaTables().get(region.getId());
                if(!vlt.isKeepVanillaLoot())
                    event.setLoot(null);
                for(CustomLootTable customLootTable : vlt.getAssociatedTableList()){
                    CustomLootTable clone = new CustomLootTable(customLootTable.getName(), customLootTable.getTableEntries(), customLootTable.isGlobalChest(), customLootTable.isGlobalMob(), customLootTable.getChance(), customLootTable.getMinItems(), customLootTable.getMaxItems());
                    clone.fillInventory(event.getInventoryHolder().getInventory(), new Random(), event.getLootContext());
                }
            }
        });

        for(CustomLootTable customLootTable : TableList.getLoadedCustomTables().values()){
            if(customLootTable.isGlobalChest()){
                CustomLootTable clone = new CustomLootTable(customLootTable.getName(), customLootTable.getTableEntries(), customLootTable.isGlobalChest(), customLootTable.isGlobalMob(), customLootTable.getChance(), customLootTable.getMinItems(), customLootTable.getMaxItems());
                clone.fillInventory(event.getInventoryHolder().getInventory(), new Random(), event.getLootContext());
            }
        }
    }

    public static void onEntityDeath(EntityDeathEvent event, Inventory tempInv){

        ApplicableRegionSet regions = plugin.getWorldGuardHandler().getRegionsStandingIn(event.getEntity());
        regions.getRegions().forEach(region -> {
            if(TableList.getLoadedVanillaTables().containsKey(region.getId())){
                VanillaLootTable vlt = TableList.getLoadedVanillaTables().get(region.getId());
                if(!vlt.isKeepVanillaLoot()){
                    for(ItemStack drop : event.getDrops()){
                        drop.setType(Material.AIR);
                    }
                    tempInv.clear();
                }
                for(CustomLootTable customLootTable : vlt.getAssociatedTableList()){
                    CustomLootTable clone = new CustomLootTable(customLootTable.getName(), customLootTable.getTableEntries(), customLootTable.isGlobalChest(), customLootTable.isGlobalMob(), customLootTable.getChance(), customLootTable.getMinItems(), customLootTable.getMaxItems());
                    clone.fillInventory(tempInv, new Random(), new LootContext.Builder(event.getEntity().getLocation()).build());
                }
            }
        });

        for(CustomLootTable customLootTable : TableList.getLoadedCustomTables().values()){
            if(customLootTable.isGlobalMob()){
                CustomLootTable clone = new CustomLootTable(customLootTable.getName(), customLootTable.getTableEntries(), customLootTable.isGlobalChest(), customLootTable.isGlobalMob(), customLootTable.getChance(), customLootTable.getMinItems(), customLootTable.getMaxItems());
                clone.fillInventory(tempInv, new Random(), new LootContext.Builder(event.getEntity().getLocation()).build());
            }
        }
    }
}

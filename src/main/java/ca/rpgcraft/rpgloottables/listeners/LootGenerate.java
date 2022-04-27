package ca.rpgcraft.rpgloottables.listeners;

import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

import java.util.Random;

/**
 * Listens for loot events and will apply custom tables need be.
 */
public class LootGenerate implements Listener {

    @EventHandler
    public void onLootGenerate(LootGenerateEvent e){
        if(!TableList.getLoadedVanillaTables().containsKey(e.getLootTable().toString())) return;
        VanillaLootTable vanillaLootTable = TableList.getLoadedVanillaTables().get(e.getLootTable().toString());
        if(!vanillaLootTable.isKeepVanillaLoot())
            e.setLoot(null);
        for(CustomLootTable customLootTable : TableList.getLoadedCustomTables().values()){
            if(customLootTable.isGlobal()){
                CustomLootTable clone = new CustomLootTable(customLootTable.getName(), customLootTable.getTableEntries(), customLootTable.isGlobal(), customLootTable.getChance(), customLootTable.getMinItems(), customLootTable.getMaxItems());
                clone.fillInventory(e.getInventoryHolder().getInventory(), new Random(), e.getLootContext());
            }
        }
        for(CustomLootTable customLootTable : vanillaLootTable.getAssociatedTableList()){
            CustomLootTable clone = new CustomLootTable(customLootTable.getName(), customLootTable.getTableEntries(), customLootTable.isGlobal(), customLootTable.getChance(), customLootTable.getMinItems(), customLootTable.getMaxItems());
            clone.fillInventory(e.getInventoryHolder().getInventory(), new Random(), e.getLootContext());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        String entityType = e.getEntityType().name().toLowerCase();
        String key = "minecraft:entities/" + entityType;
        Inventory tempInv = Bukkit.createInventory(null, 54);
        if(!TableList.getLoadedVanillaTables().containsKey(key)) return;
        VanillaLootTable vanillaLootTable = TableList.getLoadedVanillaTables().get(key);
        if(!vanillaLootTable.isKeepVanillaLoot())
            for(ItemStack drop : e.getDrops()){
                drop.setType(Material.AIR);
            }
        for(CustomLootTable customLootTable : TableList.getLoadedCustomTables().values()){
            if(customLootTable.isGlobal()){
                CustomLootTable clone = new CustomLootTable(customLootTable.getName(), customLootTable.getTableEntries(), customLootTable.isGlobal(), customLootTable.getChance(), customLootTable.getMinItems(), customLootTable.getMaxItems());
                clone.fillInventory(tempInv, new Random(), new LootContext.Builder(e.getEntity().getLocation()).build());
            }
        }
        for(CustomLootTable customLootTable : vanillaLootTable.getAssociatedTableList()){
            CustomLootTable clone = new CustomLootTable(customLootTable.getName(), customLootTable.getTableEntries(), customLootTable.isGlobal(), customLootTable.getChance(), customLootTable.getMinItems(), customLootTable.getMaxItems());
            clone.fillInventory(tempInv, new Random(), new LootContext.Builder(e.getEntity().getLocation()).build());
        }
        for(ItemStack item : tempInv.getContents()){
            if(item == null) continue;
            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), item);
        }

    }
}

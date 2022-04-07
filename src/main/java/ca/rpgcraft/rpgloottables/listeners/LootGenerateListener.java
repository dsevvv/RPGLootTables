package ca.rpgcraft.rpgloottables.listeners;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.util.CustomLootTableUtility;
import ca.rpgcraft.rpgloottables.util.TableListUtility;
import ca.rpgcraft.rpgloottables.util.VanillaLootTableUtility;
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

public class LootGenerateListener implements Listener {

    @EventHandler
    public void onLootGenerate(LootGenerateEvent e){
        if(!TableListUtility.getLoadedVanillaTables().containsKey(e.getLootTable().toString())) return;
        VanillaLootTableUtility vanillaLootTableUtility = TableListUtility.getLoadedVanillaTables().get(e.getLootTable().toString());
        if(!vanillaLootTableUtility.isKeepVanillaLoot())
            e.setLoot(null);
        for(CustomLootTableUtility customLootTableUtility : vanillaLootTableUtility.getAssociatedTableList()){
            CustomLootTableUtility clone = new CustomLootTableUtility(customLootTableUtility.getName(), customLootTableUtility.getTableEntries(), customLootTableUtility.isEnabled(), customLootTableUtility.getChance(), customLootTableUtility.getMinItems(), customLootTableUtility.getMaxItems());
            clone.fillInventory(e.getInventoryHolder().getInventory(), new Random(), e.getLootContext());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        String entityType = e.getEntityType().name().toLowerCase();
        String key = "minecraft:entities/" + entityType;
        Inventory tempInv = Bukkit.createInventory(null, 54);
        if(!TableListUtility.getLoadedVanillaTables().containsKey(key)) return;
        VanillaLootTableUtility vanillaLootTableUtility = TableListUtility.getLoadedVanillaTables().get(key);
        if(!vanillaLootTableUtility.isKeepVanillaLoot())
            for(ItemStack drop : e.getDrops()){
                drop.setType(Material.AIR);
            }
        for(CustomLootTableUtility customLootTableUtility : vanillaLootTableUtility.getAssociatedTableList()){
            CustomLootTableUtility clone = new CustomLootTableUtility(customLootTableUtility.getName(), customLootTableUtility.getTableEntries(), customLootTableUtility.isEnabled(), customLootTableUtility.getChance(), customLootTableUtility.getMinItems(), customLootTableUtility.getMaxItems());
            clone.fillInventory(tempInv, new Random(), new LootContext.Builder(e.getEntity().getLocation()).build());
        }
        for(ItemStack item : tempInv.getContents()){
            if(item == null) continue;
            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), item);
        }

    }
}

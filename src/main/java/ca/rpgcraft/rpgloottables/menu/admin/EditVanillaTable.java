package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.hook.mythic.ListMythic;
import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditVanillaTable extends Menu {
    public EditVanillaTable(PlayerMenuManager playerMenuManager) {
        super(playerMenuManager);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            //toggle vanilla loot
            case 10:
                playerMenuManager.setGlobalChest(!playerMenuManager.isGlobalChest());
                if(playerMenuManager.isGlobalChest())
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Vanilla Loot &aon&e."));
                else
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Vanilla Loot &coff&e."));
                open();
                break;
            //add associated loot table
            case 12:
                new ListAddCustomTable(playerMenuManager, "&0         Add Custom Table").open();
                break;
            //remove associated table
            case 14:
                if(playerMenuManager.getAssociatedTables().size() == 0){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have no custom tables associated with this loot table!"));
                    open();
                    break;
                }
                new ListRemoveCustomTable(playerMenuManager, "&0       Remove Custom Table").open();
                break;
            //save and close
            case 16:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSaved &6" + playerMenuManager.getLootTableName().replace("minecraft:", "") + "&a."));
                if(!playerMenuManager.isGlobalChest() || playerMenuManager.getAssociatedTables().size() > 0) //we are only saving to memory if defaults have been modified
                    TableList.getLoadedVanillaTables().put(playerMenuManager.getLootTableName(), new VanillaLootTable(playerMenuManager.getLootTableName(), playerMenuManager.getAssociatedTables(), playerMenuManager.isGlobalChest()));
                else
                    TableList.getLoadedVanillaTables().remove(playerMenuManager.getLootTableName());
                new MainMenu(playerMenuManager).open();
                break;
            //back
            case 22:
                new MainMenu(playerMenuManager).open();
                break;
            default:
                open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, InventoryType.CHEST, ChatColor.translateAlternateColorCodes('&', "&0Edit " + playerMenuManager.getLootTableName().replace("minecraft:", "")));

        addMenuBorderSmall(inv, false);

        inv.setItem(10, toggleGlobalStatus());
        inv.setItem(12, createItem(
                Material.GREEN_CONCRETE,
                ChatColor.translateAlternateColorCodes('&', "&aAdd Custom Table")
        ));
        inv.setItem(14, createItem(
                Material.WRITABLE_BOOK,
                ChatColor.translateAlternateColorCodes('&', "&eRemove Custom Table")
        ));
        inv.setItem(16, createItem(
                Material.EMERALD_BLOCK,
                ChatColor.translateAlternateColorCodes('&', "&aSave and Quit")
        ));

        return inv;
    }

    private ItemStack toggleGlobalStatus(){
        ItemStack itemStack = new ItemStack(Material.STONE);
        if(playerMenuManager.isGlobalChest()){
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table generate"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7it's vanilla loot?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&aEnabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Vanilla Loot"));
            itemStack.setType(Material.GREEN_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }else {
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table generate"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7it's vanilla loot?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&cDisabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Vanilla Loot"));
            itemStack.setType(Material.RED_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }
    }
}

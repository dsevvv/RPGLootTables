package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;

public class ChoiceCustomTable extends Menu {
    public ChoiceCustomTable(PlayerMenuManager playerMenuManager) {
        super(playerMenuManager);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 12:
                playerMenuManager.setLootTableName("Table " + (TableList.getLoadedCustomTables().size() + 1));
                playerMenuManager.setGlobalChest(false);
                playerMenuManager.setGlobalMob(false);
                playerMenuManager.setChance(100);
                playerMenuManager.setMaxTableItems(1);
                playerMenuManager.setMinTableItems(1);
                playerMenuManager.setTableEntries(new LinkedList<>());
                new EditCustomTable(playerMenuManager).open();
                break;
            case 14:
                new ListEditCustomTable(playerMenuManager, ChatColor.translateAlternateColorCodes('&', "     &0Edit Custom Loot Tables")).open();
                break;
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
        Inventory inv = Bukkit.createInventory(this, InventoryType.CHEST, ChatColor.translateAlternateColorCodes('&', "&0         Custom Table Menu"));

        addMenuBorderSmall(inv, false);

        ItemStack createLootTableItem = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta meta0 = createLootTableItem.getItemMeta();
        meta0.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aCreate Loot Table"));
        createLootTableItem.setItemMeta(meta0);

        ItemStack editLootTableItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta1 = editLootTableItem.getItemMeta();
        meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Loot Table"));
        editLootTableItem.setItemMeta(meta1);

        inv.setItem(12, createLootTableItem);
        inv.setItem(14, editLootTableItem);

        return inv;
    }
}

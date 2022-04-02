package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends Menu {


    public MainMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {

    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', "     &6RPGLoot Main Menu"));

        ItemStack createLootTableItem = new ItemStack(Material.EMERALD_BLOCK);

        ItemStack editLootTableItem = new ItemStack(Material.WRITABLE_BOOK);

        ItemStack deleteLootTableItem = new ItemStack(Material.REDSTONE_BLOCK);

        inv.addItem(createLootTableItem, editLootTableItem, deleteLootTableItem);

        return inv;
    }
}

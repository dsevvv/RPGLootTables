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
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu extends Menu {
    public MainMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 1 -> new ListChestMenu(playerMenuUtility).open();
            case 2 -> new ListMobMenu(playerMenuUtility).open();
            case 3 -> new ChoiceCustomTableMenu(playerMenuUtility).open();
            default -> open();
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', "     &6RPGLoot Main Menu"));

        ItemStack createLootTableItem = new ItemStack(Material.CHEST);
        ItemMeta meta0 = createLootTableItem.getItemMeta();
        meta0.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Chest Tables"));
        createLootTableItem.setItemMeta(meta0);

        ItemStack editLootTableItem = new ItemStack(Material.ZOMBIE_HEAD);
        ItemMeta meta1 = editLootTableItem.getItemMeta();
        meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Mob Tables"));
        editLootTableItem.setItemMeta(meta1);

        ItemStack deleteLootTableItem = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta2 = deleteLootTableItem.getItemMeta();
        meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Custom Tables"));
        deleteLootTableItem.setItemMeta(meta2);

        inv.addItem(BLANK_ITEM, createLootTableItem, editLootTableItem, deleteLootTableItem, BLANK_ITEM);

        return inv;
    }
}

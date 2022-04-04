package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
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
import org.bukkit.scheduler.BukkitRunnable;

public class MainMenu extends Menu {
    public MainMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 12 -> new ListChestMenu(playerMenuUtility, "    &0Vanilla Chest Loot Tables").open();
            case 13 -> new ListMobMenu(playerMenuUtility, "     &0Vanilla Mob Loot Tables").open();
            case 14 -> new ChoiceCustomTableMenu(playerMenuUtility).open();
            case 22 -> new BukkitRunnable() {
                @Override
                public void run() {
                    whoClicked.closeInventory();
                }
            }.runTaskLater(RPGLootTables.getPlugin(RPGLootTables.class), 1);
            default -> open();
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, InventoryType.CHEST, ChatColor.translateAlternateColorCodes('&', "&0              Main Menu"));

        for(int i = 0; i < 27; i++){
            if((i > 9 && i < 17) || i == 22) continue;
            inv.setItem(i, BLANK_ITEM);
        }
        inv.setItem(22, CLOSE_ITEM);

        ItemStack editChestTablesItem = new ItemStack(Material.CHEST);
        ItemMeta meta0 = editChestTablesItem.getItemMeta();
        meta0.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Chest Tables"));
        editChestTablesItem.setItemMeta(meta0);

        ItemStack editMobTablesItem = new ItemStack(Material.ZOMBIE_HEAD);
        ItemMeta meta1 = editMobTablesItem.getItemMeta();
        meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Mob Tables"));
        editMobTablesItem.setItemMeta(meta1);

        ItemStack editCustomTablesItem = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta2 = editCustomTablesItem.getItemMeta();
        meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Custom Tables"));
        editCustomTablesItem.setItemMeta(meta2);

        inv.setItem(12, editChestTablesItem);
        inv.setItem(13, editMobTablesItem);
        inv.setItem(14, editCustomTablesItem);

        return inv;
    }
}

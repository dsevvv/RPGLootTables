package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class MainMenu extends Menu {
    public MainMenu(PlayerMenuManager playerMenuManager) {
        super(playerMenuManager);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 10 -> new ListChest(playerMenuManager, "    &0Vanilla Chest Loot Tables").open();
            case 12 -> new ListMob(playerMenuManager, "     &0Vanilla Mob Loot Tables").open();
            case 14 -> new ListGameplay(playerMenuManager, "      &0Gameplay Loot Tables").open();
            case 16 -> new ChoiceCustomTable(playerMenuManager).open();
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

        addMenuBorderSmall(inv, true);

        ItemStack editChestTablesItem = new ItemStack(Material.CHEST);
        ItemMeta meta0 = editChestTablesItem.getItemMeta();
        meta0.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Chest Tables"));
        editChestTablesItem.setItemMeta(meta0);

        ItemStack editMobTablesItem = new ItemStack(Material.ZOMBIE_HEAD);
        ItemMeta meta1 = editMobTablesItem.getItemMeta();
        meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Mob Tables"));
        editMobTablesItem.setItemMeta(meta1);

        ItemStack editGameplayTablesItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta2 = editGameplayTablesItem.getItemMeta();
        meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Gameplay Tables"));
        meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        editGameplayTablesItem.setItemMeta(meta2);

        ItemStack editCustomTablesItem = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta3 = editCustomTablesItem.getItemMeta();
        meta3.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Custom Tables"));
        editCustomTablesItem.setItemMeta(meta3);

        inv.setItem(10, editChestTablesItem);
        inv.setItem(12, editMobTablesItem);
        inv.setItem(14, editGameplayTablesItem);
        inv.setItem(16, editCustomTablesItem);

        return inv;
    }
}

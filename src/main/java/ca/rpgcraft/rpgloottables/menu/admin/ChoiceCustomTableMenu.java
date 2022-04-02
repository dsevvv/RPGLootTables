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

public class ChoiceCustomTableMenu extends Menu {
    public ChoiceCustomTableMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 1 -> new CreateCustomTableMenu(playerMenuUtility).open();
            case 2 -> new EditCustomTableMenu(playerMenuUtility).open();
            case 3 -> new DeleteCustomTableMenu(playerMenuUtility).open();
            default -> open();
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', "     &6Custom Table Menu"));

        ItemStack createLootTableItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta meta0 = createLootTableItem.getItemMeta();
        meta0.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aCreate Loot Table"));
        createLootTableItem.setItemMeta(meta0);

        ItemStack editLootTableItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta1 = editLootTableItem.getItemMeta();
        meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Loot Table"));
        editLootTableItem.setItemMeta(meta1);

        ItemStack deleteLootTableItem = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta2 = deleteLootTableItem.getItemMeta();
        meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cDelete Loot Table"));
        deleteLootTableItem.setItemMeta(meta2);

        inv.addItem(BLANK_ITEM, createLootTableItem, editLootTableItem, deleteLootTableItem, BLANK_ITEM);

        return inv;
    }
}

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

import java.util.UUID;

public class ChoiceCustomTableMenu extends Menu {
    public ChoiceCustomTableMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 11:
                playerMenuUtility.setLootTableName(UUID.randomUUID().toString().replace("-", ""));
                playerMenuUtility.setEnabled(false);
                playerMenuUtility.setChance(100);
                playerMenuUtility.setMaxTableItems(1);
                playerMenuUtility.setMinTableItems(1);
                new CreateCustomTableMenu(playerMenuUtility).open();
                break;
            case 13:
                new EditCustomTableMenu(playerMenuUtility).open();
                break;
            case 15:
                new DeleteCustomTableMenu(playerMenuUtility).open();
                break;
            case 22:
                new MainMenu(playerMenuUtility).open();
                break;
            default:
                open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, InventoryType.CHEST, ChatColor.translateAlternateColorCodes('&', "&0         Custom Table Menu"));

        for(int i = 0; i < 27; i++){
            if((i > 9 && i < 17) || i == 22) continue;
            inv.setItem(i, BLANK_ITEM);
        }
        inv.setItem(22, BACK_ITEM);

        ItemStack createLootTableItem = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta meta0 = createLootTableItem.getItemMeta();
        meta0.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aCreate Loot Table"));
        createLootTableItem.setItemMeta(meta0);

        ItemStack editLootTableItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta1 = editLootTableItem.getItemMeta();
        meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEdit Loot Table"));
        editLootTableItem.setItemMeta(meta1);

        ItemStack deleteLootTableItem = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta2 = deleteLootTableItem.getItemMeta();
        meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cDelete Loot Table"));
        deleteLootTableItem.setItemMeta(meta2);

        inv.setItem(11, createLootTableItem);
        inv.setItem(13, editLootTableItem);
        inv.setItem(15, deleteLootTableItem);

        return inv;
    }
}

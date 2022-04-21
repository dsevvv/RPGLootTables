package ca.rpgcraft.rpgloottables.menu.standard;

import ca.rpgcraft.rpgloottables.util.PlayerMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;
    protected Inventory inventory;

    public PaginatedMenu(PlayerMenu playerMenu, String inventoryName) {
        super(playerMenu);
        inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', inventoryName));
    }

    /**
     * Adds border and buttons to a paginated menu.
     */
    public void addPaginatedMenuBorder(){
        inventory.setItem(48, createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "<--"));

        inventory.setItem(49, BACK_ITEM);

        inventory.setItem(50, createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "-->"));

        for (int i = 0; i < 10; i++) {
            if(i == 4){
                ItemStack pageItem = createItem(Material.PAPER, "&7Page " + (page + 1));
                pageItem.setAmount(page + 1);
                inventory.setItem(i, pageItem);
                continue;
            }
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, BLANK_ITEM);
            }
        }

        inventory.setItem(17, BLANK_ITEM);
        inventory.setItem(18, BLANK_ITEM);
        inventory.setItem(26, BLANK_ITEM);
        inventory.setItem(27, BLANK_ITEM);
        inventory.setItem(35, BLANK_ITEM);
        inventory.setItem(36, BLANK_ITEM);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, BLANK_ITEM);
            }
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}

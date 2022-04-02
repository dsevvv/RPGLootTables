package ca.rpgcraft.rpgloottables.menu.standard;

import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Menu implements InventoryHolder {

    protected PlayerMenuUtility playerMenuUtility;

    protected static ItemStack BLANK_ITEM;

    public Menu(PlayerMenuUtility playerMenuUtility){
        this.playerMenuUtility = playerMenuUtility;
        ItemStack blank = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta blankMeta = blank.getItemMeta();
        blankMeta.setDisplayName("");
        blank.setItemMeta(blankMeta);
        BLANK_ITEM = blank;
    }

    /**
     * This method will handle what happens when a menu is clicked.
     * @param whoClicked Player who clicked the inventory
     * @param rawSlot Slot index of the clicked slot
     */
    public abstract void onMenuClick(Player whoClicked, int rawSlot);

    /**
     * Opens the menu for the owner of the associated PlayerMenuUtility
     */
    public void open(){
        playerMenuUtility.getOwner().openInventory(getInventory());
    }
}

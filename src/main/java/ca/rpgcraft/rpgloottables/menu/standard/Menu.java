package ca.rpgcraft.rpgloottables.menu.standard;

import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {

    protected PlayerMenuUtility playerMenuUtility;

    public Menu(PlayerMenuUtility playerMenuUtility){
        this.playerMenuUtility = playerMenuUtility;
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

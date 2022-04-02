package ca.rpgcraft.rpgloottables.menu.standard;

import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {

    protected PlayerMenuUtility playerMenuUtility;

    public Menu(PlayerMenuUtility playerMenuUtility){
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract void onMenuClick(Player whoClicked, int rawSlot);

    public void open(){
        playerMenuUtility.getOwner().openInventory(getInventory());
    }
}

package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CreateTableMenu extends Menu {
    public CreateTableMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {

    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
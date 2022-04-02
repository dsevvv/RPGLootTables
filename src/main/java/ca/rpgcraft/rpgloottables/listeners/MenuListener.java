package ca.rpgcraft.rpgloottables.listeners;

import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e){
        if(!(e.getInventory().getHolder() instanceof Menu menu)) return;
        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();
        menu.onMenuClick(player, e.getRawSlot());
    }
}

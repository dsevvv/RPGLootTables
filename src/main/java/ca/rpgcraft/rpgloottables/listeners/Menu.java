package ca.rpgcraft.rpgloottables.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Listens for Menu clicks.
 */
public class Menu implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(!(e.getInventory().getHolder() instanceof ca.rpgcraft.rpgloottables.menu.standard.Menu menu)) return;
        if(e.getCurrentItem() == null) return;

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        menu.onMenuClick(player, e.getRawSlot());
    }
}

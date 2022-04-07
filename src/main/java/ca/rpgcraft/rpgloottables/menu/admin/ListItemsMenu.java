package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.item.TableEntry;
import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class ListItemsMenu extends PaginatedMenu {
    public ListItemsMenu(PlayerMenuUtility playerMenuUtility, String inventoryName) {
        super(playerMenuUtility, inventoryName);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {

    }

    @Override
    public Inventory getInventory() {
        LinkedList<TableEntry> tableEntries = playerMenuUtility.getTableEntries();

        inventory.clear();
        addPaginatedMenuBorder();

        for(int i = 0; i < getMaxItemsPerPage(); i++){
            index = getMaxItemsPerPage() * page + i;
            if(index >= tableEntries.size()) break;
            ItemStack tempItem = tableEntries.get(i).getItemStack();
            ItemMeta meta = tempItem.getItemMeta();
            List<String> lore = new LinkedList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eWeight&7: " + tableEntries.get(i).getWeight()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eMin Amount&7: " + tableEntries.get(i).getMinAmt()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eMax Amount&7: " + tableEntries.get(i).getMaxAmt()));
            lore.add("");
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            tempItem.setItemMeta(meta);
            inventory.addItem(tempItem);
        }

        return inventory;
    }
}

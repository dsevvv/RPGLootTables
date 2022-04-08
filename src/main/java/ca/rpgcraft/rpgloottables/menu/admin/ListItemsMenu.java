package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.item.TableEntry;
import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import ca.rpgcraft.rpgloottables.util.TableListUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        switch(rawSlot){
            case 48:
                if(page == 0){
                    playerMenuUtility.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already on the first page!"));
                    open();
                    break;
                }
                page -= 1;
                open();
                break;
            case 49:
                new EditCustomTableMenu(playerMenuUtility).open();
                break;
            case 50:
                if(index + 1 >= playerMenuUtility.getTableEntries().size()){
                    playerMenuUtility.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already on the last page!"));
                    open();
                    break;
                }
                page += 1;
                open();
                break;
            case 10,11,12,13,14,15,16:
            case 19,20,21,22,23,24,25:
            case 28,29,30,31,32,33,34:
            case 37,38,39,40,41,42,43:
                int index = getMaxItemsPerPage() * page + rawSlot - 10;
                if(rawSlot >= 19)
                    index -= 2;
                if(rawSlot >= 28)
                    index -= 2;
                if(rawSlot >= 37)
                    index -= 2;
                new EditTableEntryMenu(playerMenuUtility,
                                       index,
                                       playerMenuUtility.getTableEntries().get(index).getItemStack(),
                                       playerMenuUtility.getTableEntries().get(index).getWeight(),
                                       playerMenuUtility.getTableEntries().get(index).getMinAmt(),
                                       playerMenuUtility.getTableEntries().get(index).getMaxAmt()).open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        LinkedList<TableEntry> tableEntries = playerMenuUtility.getTableEntries();

        inventory.clear();
        addPaginatedMenuBorder();

        for(int i = 0; i < getMaxItemsPerPage(); i++){
            index = getMaxItemsPerPage() * page + i;
            if(index >= tableEntries.size()) break;
            ItemStack tempItem = tableEntries.get(index).getItemStack();
            ItemMeta meta = tempItem.getItemMeta();
            List<String> lore = new LinkedList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eWeight&7: " + tableEntries.get(index).getWeight()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eMin Amount&7: " + tableEntries.get(index).getMinAmt()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eMax Amount&7: " + tableEntries.get(index).getMaxAmt()));
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            tempItem.setItemMeta(meta);
            for(int j = 0; j < inventory.getSize(); j++){
                if(inventory.getItem(j) == null){
                    inventory.setItem(j, tempItem);
                    break;
                }
            }
        }

        return inventory;
    }
}

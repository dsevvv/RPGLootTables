package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.LootTableUtility;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ListChestMenu extends PaginatedMenu {
    public ListChestMenu(PlayerMenuUtility playerMenuUtility, String inventoryName) {
        super(playerMenuUtility, inventoryName);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        if(getInventory().getItem(rawSlot) == null) return;
        if(getInventory().getItem(rawSlot).getItemMeta() == null) return;

        ItemStack clickedItem = getInventory().getItem(rawSlot);

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
                new MainMenu(playerMenuUtility).open();
                playerMenuUtility.setLootTableName("");
                break;
            case 50:
                if(index + 1 >= LootTableUtility.getChestLootTables().length){
                    playerMenuUtility.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already on the last page!"));
                    open();
                    break;
                }
                page += 1;
                open();
                break;
            default:
                if(!getInventory().getItem(rawSlot).getType().equals(Material.CHEST)){
                    open();
                    break;
                }
                playerMenuUtility.setLootTableName(clickedItem.getItemMeta().getDisplayName());
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou chose &6" + clickedItem.getItemMeta().getDisplayName().replace("minecraft:", "") + "&a."));
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        String[] vanillaChestTables = LootTableUtility.getChestLootTables();

        inventory.clear();
        addMenuBorder();

        for(int i = 0; i < getMaxItemsPerPage(); i++){
            index = getMaxItemsPerPage() * page + i;
            if(index >= vanillaChestTables.length) break;
            if(!vanillaChestTables[index].isBlank()){

                inventory.addItem(createItem(Material.CHEST, vanillaChestTables[index]));
            }
        }

        return inventory;
    }
}

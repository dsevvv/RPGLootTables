package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public class ListChest extends PaginatedMenu {
    public ListChest(PlayerMenuManager playerMenuManager, String inventoryName) {
        super(playerMenuManager, inventoryName);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        if(getInventory().getItem(rawSlot) == null) return;
        if(getInventory().getItem(rawSlot).getItemMeta() == null) return;

        ItemStack clickedItem = getInventory().getItem(rawSlot);

        switch(rawSlot){
            case 48:
                if(page == 0){
                    playerMenuManager.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already on the first page!"));
                    open();
                    break;
                }
                page -= 1;
                open();
                break;
            case 49:
                new MainMenu(playerMenuManager).open();
                playerMenuManager.setLootTableName("");
                break;
            case 50:
                if(index + 1 >= TableList.getChestLootTables().length){
                    playerMenuManager.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already on the last page!"));
                    open();
                    break;
                }
                page += 1;
                open();
                break;
            default:
                if(!getInventory().getItem(rawSlot).getType().equals(Material.CHEST)
                && !getInventory().getItem(rawSlot).getType().equals(Material.ENDER_CHEST)){
                    open();
                    break;
                }
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou chose &6" + clickedItem.getItemMeta().getDisplayName().replace("minecraft:", "") + "&a."));
                if(TableList.getLoadedVanillaTables().containsKey(clickedItem.getItemMeta().getDisplayName())){
                    playerMenuManager.setLootTableName(clickedItem.getItemMeta().getDisplayName());
                    playerMenuManager.setEnabled(TableList.getLoadedVanillaTables().get(clickedItem.getItemMeta().getDisplayName()).isKeepVanillaLoot());
                    playerMenuManager.setAssociatedTables(TableList.getLoadedVanillaTables().get(clickedItem.getItemMeta().getDisplayName()).getAssociatedTableList());
                    new EditVanillaTable(playerMenuManager).open();
                    break;
                }else{
                    playerMenuManager.setLootTableName(clickedItem.getItemMeta().getDisplayName());
                    playerMenuManager.setEnabled(true);
                    playerMenuManager.setAssociatedTables(new LinkedList<>());
                    new EditVanillaTable(playerMenuManager).open();
                    break;
                }
        }
    }

    @Override
    public Inventory getInventory() {
        String[] vanillaChestTables = TableList.getChestLootTables();

        inventory.clear();
        addPaginatedMenuBorder();

        for(int i = 0; i < getMaxItemsPerPage(); i++){
            index = getMaxItemsPerPage() * page + i;
            if(index >= vanillaChestTables.length) break;
            if(!vanillaChestTables[index].isBlank()){
                if(TableList.getLoadedVanillaTables().containsKey(vanillaChestTables[index])){
                    VanillaLootTable vanillaLootTable = TableList.getLoadedVanillaTables().get(vanillaChestTables[index]);
                    inventory.addItem(createItem(
                            Material.ENDER_CHEST,
                            vanillaLootTable.getVanillaTableName(),
                            ChatColor.translateAlternateColorCodes('&', "&eGenerate Vanilla Loot&7: " + vanillaLootTable.isKeepVanillaLoot()),
                            ChatColor.translateAlternateColorCodes('&', "&eAssociated Tables&7: " + vanillaLootTable.getAssociatedTableList().size())
                    ));
                }else{
                    inventory.addItem(createItem(
                            Material.CHEST,
                            vanillaChestTables[index]));
                }
            }
        }

        return inventory;
    }
}

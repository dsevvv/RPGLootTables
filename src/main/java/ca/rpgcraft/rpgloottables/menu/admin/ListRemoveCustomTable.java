package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.item.TableEntry;
import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import ca.rpgcraft.rpgloottables.util.TableList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class ListRemoveCustomTable extends PaginatedMenu {
    public ListRemoveCustomTable(PlayerMenuManager playerMenuManager, String inventoryName) {
        super(playerMenuManager, inventoryName);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        if(getInventory().getItem(rawSlot) == null) return;
        if(getInventory().getItem(rawSlot).getItemMeta() == null) return;

        ItemStack clickedItem = getInventory().getItem(rawSlot);

        switch (rawSlot){
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
                new EditVanillaTable(playerMenuManager).open();
                break;
            case 50:
                if(index + 1 >= TableList.getLoadedCustomTables().size()){
                    playerMenuManager.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already on the last page!"));
                    open();
                    break;
                }
                page += 1;
                open();
                break;
            default:
                if(!clickedItem.getType().equals(Material.CHEST)){
                    open();
                    break;
                }
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou removed &6" + clickedItem.getItemMeta().getDisplayName() + " &afrom &6" + playerMenuManager.getLootTableName().replace("minecraft:", "") + "&a."));
                playerMenuManager.getAssociatedTables().remove(TableList.getLoadedCustomTables().get(clickedItem.getItemMeta().getDisplayName()));
                new EditVanillaTable(playerMenuManager).open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        List<String> customTableNames = new LinkedList<>();
        List<Boolean> customTableGlobals = new LinkedList<>();
        List<Double> customTableChances = new LinkedList<>();
        List<Integer> customTableMinItems = new LinkedList<>();
        List<Integer> customTableMaxItems = new LinkedList<>();
        List<LinkedList<TableEntry>> customTableEntries = new LinkedList<>();
        TableList.getLoadedVanillaTables().get(playerMenuManager.getLootTableName()).getAssociatedTableList().forEach((customLootTableUtility) -> {
            customTableNames.add(customLootTableUtility.getName());
            customTableGlobals.add(customLootTableUtility.isGlobalChest());
            customTableChances.add(customLootTableUtility.getChance());
            customTableMinItems.add(customLootTableUtility.getMinItems());
            customTableMaxItems.add(customLootTableUtility.getMaxItems());
            customTableEntries.add(customLootTableUtility.getTableEntries());
        });

        inventory.clear();
        addPaginatedMenuBorder();

        for(int i = 0; i < getMaxItemsPerPage(); i++){
            index = getMaxItemsPerPage() * page + i;
            if(index >= customTableNames.size()) break;
            if(!customTableNames.get(index).isBlank()){

                inventory.addItem(createItem(
                        Material.CHEST,
                        ChatColor.translateAlternateColorCodes('&', customTableNames.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eGlobal: &7" + customTableGlobals.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eChance: &7" + customTableChances.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eMin&7/&eMax: &7" + customTableMinItems.get(index) + "/" + customTableMaxItems.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eContents&7: " + customTableEntries.get(index).size()))
                );
            }
        }

        return inventory;
    }
}

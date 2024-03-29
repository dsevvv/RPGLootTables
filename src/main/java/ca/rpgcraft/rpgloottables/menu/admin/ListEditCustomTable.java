package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.item.TableEntry;
import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ListEditCustomTable extends PaginatedMenu {
    public ListEditCustomTable(PlayerMenuManager playerMenuManager, String inventoryName) {
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
                new ChoiceCustomTable(playerMenuManager).open();
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
                if(!getInventory().getItem(rawSlot).getType().equals(Material.CHEST)){
                    open();
                    break;
                }
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou chose &6" + clickedItem.getItemMeta().getDisplayName() + "&a."));
                String name = clickedItem.getItemMeta().getDisplayName();
                HashMap<String, CustomLootTable> loadedTables = TableList.getLoadedCustomTables();
                playerMenuManager.setLootTableName(loadedTables.get(name).getName());
                playerMenuManager.setChance(loadedTables.get(name).getChance());
                playerMenuManager.setGlobalChest(loadedTables.get(name).isGlobalChest());
                playerMenuManager.setGlobalMob(loadedTables.get(name).isGlobalMob());
                playerMenuManager.setMinTableItems(loadedTables.get(name).getMinItems());
                playerMenuManager.setMaxTableItems(loadedTables.get(name).getMaxItems());
                playerMenuManager.setTableEntries(loadedTables.get(name).getTableEntries());
                new EditCustomTable(playerMenuManager).open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        List<String> customTableNames = new LinkedList<>();
        List<Boolean> customTableGlobals = new LinkedList<>();
        List<Boolean> customTableGlobalsMob = new LinkedList<>();
        List<Double> customTableChances = new LinkedList<>();
        List<Integer> customTableMinItems = new LinkedList<>();
        List<Integer> customTableMaxItems = new LinkedList<>();
        List<LinkedList<TableEntry>> customTableEntries = new LinkedList<>();
        TableList.getLoadedCustomTables().forEach((name, customLootTableUtility) -> {
            customTableNames.add(customLootTableUtility.getName());
            customTableGlobals.add(customLootTableUtility.isGlobalChest());
            customTableGlobalsMob.add(customLootTableUtility.isGlobalMob());
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
                        ChatColor.translateAlternateColorCodes('&', "&eGlobal Chest: &7" + customTableGlobals.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eGlobal Mob: &7" + customTableGlobalsMob.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eChance: &7" + customTableChances.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eMin&7/&eMax: &7" + customTableMinItems.get(index) + "/" + customTableMaxItems.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eContents&7: " + customTableEntries.get(index).size()))
                        );
            }
        }

        return inventory;
    }
}

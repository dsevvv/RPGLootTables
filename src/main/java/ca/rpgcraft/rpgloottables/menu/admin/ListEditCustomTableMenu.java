package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.CustomLootTableUtility;
import ca.rpgcraft.rpgloottables.util.LootTableUtility;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ListEditCustomTableMenu extends PaginatedMenu {
    public ListEditCustomTableMenu(PlayerMenuUtility playerMenuUtility, String inventoryName) {
        super(playerMenuUtility, inventoryName);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        if(getInventory().getItem(rawSlot) == null) return;
        if(getInventory().getItem(rawSlot).getItemMeta() == null) return;

        ItemStack clickedItem = getInventory().getItem(rawSlot);

        switch (rawSlot){
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
                new ChoiceCustomTableMenu(playerMenuUtility).open();
                break;
            case 50:
                if(index + 1 >= LootTableUtility.getLoadedTables().size()){
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
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou chose &6" + clickedItem.getItemMeta().getDisplayName() + "&a."));
                String name = clickedItem.getItemMeta().getDisplayName();
                HashMap<String, CustomLootTableUtility> loadedTables = LootTableUtility.getLoadedTables();
                playerMenuUtility.setLootTableName(name);
                playerMenuUtility.setChance(loadedTables.get(name).getChance());
                playerMenuUtility.setEnabled(loadedTables.get(name).isEnabled());
                playerMenuUtility.setMinTableItems(loadedTables.get(name).getMinItems());
                playerMenuUtility.setMaxTableItems(loadedTables.get(name).getMaxItems());
                playerMenuUtility.setItems(loadedTables.get(name).getItems());
                new EditCustomTableMenu(playerMenuUtility).open();
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
        LootTableUtility.getLoadedTables().forEach((name, customLootTableUtility) -> {
            customTableNames.add(customLootTableUtility.getName());
            customTableGlobals.add(customLootTableUtility.isEnabled());
            customTableChances.add(customLootTableUtility.getChance());
            customTableMinItems.add(customLootTableUtility.getMinItems());
            customTableMaxItems.add(customLootTableUtility.getMaxItems());
        });

        inventory.clear();
        addMenuBorder();

        for(int i = 0; i < getMaxItemsPerPage(); i++){
            index = getMaxItemsPerPage() * page + i;
            if(index >= customTableNames.size()) break;
            if(!customTableNames.get(index).isBlank()){

                inventory.addItem(createItem(
                        Material.CHEST,
                        ChatColor.translateAlternateColorCodes('&', customTableNames.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eGlobal: &7" + customTableGlobals.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eChance: &7" + customTableChances.get(index)),
                        ChatColor.translateAlternateColorCodes('&', "&eMin&7/&eMax: &7" + customTableMinItems.get(index) + "/" + customTableMaxItems.get(index))));
            }
        }

        return inventory;
    }
}

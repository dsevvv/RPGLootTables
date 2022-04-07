package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.TableListUtility;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import ca.rpgcraft.rpgloottables.util.VanillaLootTableUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

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
                if(index + 1 >= TableListUtility.getChestLootTables().length){
                    playerMenuUtility.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already on the last page!"));
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
                if(TableListUtility.getLoadedVanillaTables().containsKey(clickedItem.getItemMeta().getDisplayName())){
                    playerMenuUtility.setLootTableName(clickedItem.getItemMeta().getDisplayName());
                    playerMenuUtility.setEnabled(TableListUtility.getLoadedVanillaTables().get(clickedItem.getItemMeta().getDisplayName()).isKeepVanillaLoot());
                    playerMenuUtility.setAssociatedTables(TableListUtility.getLoadedVanillaTables().get(clickedItem.getItemMeta().getDisplayName()).getAssociatedTableList());
                    TableListUtility.getLoadedVanillaTables().put(clickedItem.getItemMeta().getDisplayName(), new VanillaLootTableUtility(clickedItem.getItemMeta().getDisplayName(), playerMenuUtility.getAssociatedTables(), playerMenuUtility.isEnabled()));
                    new EditVanillaTableMenu(playerMenuUtility).open();
                    break;
                }else{
                    playerMenuUtility.setLootTableName(clickedItem.getItemMeta().getDisplayName());
                    playerMenuUtility.setEnabled(true);
                    playerMenuUtility.setAssociatedTables(new LinkedList<>());
                    TableListUtility.getLoadedVanillaTables().put(clickedItem.getItemMeta().getDisplayName(), new VanillaLootTableUtility(clickedItem.getItemMeta().getDisplayName(), playerMenuUtility.getAssociatedTables(), playerMenuUtility.isEnabled()));
                    new EditVanillaTableMenu(playerMenuUtility).open();
                    break;
                }
        }
    }

    @Override
    public Inventory getInventory() {
        String[] vanillaChestTables = TableListUtility.getChestLootTables();

        inventory.clear();
        addPaginatedMenuBorder();

        for(int i = 0; i < getMaxItemsPerPage(); i++){
            index = getMaxItemsPerPage() * page + i;
            if(index >= vanillaChestTables.length) break;
            if(!vanillaChestTables[index].isBlank()){
                if(TableListUtility.getLoadedVanillaTables().containsKey(vanillaChestTables[index])){
                    VanillaLootTableUtility vanillaLootTableUtility = TableListUtility.getLoadedVanillaTables().get(vanillaChestTables[index]);
                    inventory.addItem(createItem(
                            Material.ENDER_CHEST,
                            vanillaLootTableUtility.getVanillaTableName(),
                            ChatColor.translateAlternateColorCodes('&', "&eGenerate Vanilla Loot&7: " + vanillaLootTableUtility.isKeepVanillaLoot()),
                            ChatColor.translateAlternateColorCodes('&', "&eAssociated Tables&7: " + vanillaLootTableUtility.getAssociatedTableList().size())
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

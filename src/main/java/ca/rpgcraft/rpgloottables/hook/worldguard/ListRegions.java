package ca.rpgcraft.rpgloottables.hook.worldguard;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.menu.admin.EditVanillaTable;
import ca.rpgcraft.rpgloottables.menu.admin.MainMenu;
import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

public class ListRegions extends PaginatedMenu {

    private static final boolean DEBUG = false;
    private final RPGLootTables plugin;
    private final Logger log;
    public ListRegions(PlayerMenuManager playerMenuManager, String inventoryName) {
        super(playerMenuManager, inventoryName);
        this.plugin = RPGLootTables.getInstance();
        this.log = plugin.getLogger();
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
                if(index + 1 >= plugin.getWorldGuardHandler().getRegions().size()){
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
                    playerMenuManager.setGlobalChest(TableList.getLoadedVanillaTables().get(clickedItem.getItemMeta().getDisplayName()).isKeepVanillaLoot());
                    playerMenuManager.setAssociatedTables(TableList.getLoadedVanillaTables().get(clickedItem.getItemMeta().getDisplayName()).getAssociatedTableList());
                    new EditVanillaTable(playerMenuManager).open();
                    break;
                }else{
                    playerMenuManager.setLootTableName(clickedItem.getItemMeta().getDisplayName());
                    playerMenuManager.setGlobalChest(true);
                    playerMenuManager.setAssociatedTables(new LinkedList<>());
                    new EditVanillaTable(playerMenuManager).open();
                    break;
                }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Collection<ProtectedRegion> regions = RPGLootTables.getInstance().getWorldGuardHandler().getRegions();
        if(DEBUG){
            log.info("Region Count: " + regions.size());
            regions.forEach(region -> {
                log.info("Region: " + region.toString());
            });
        }
        //creates an array of strings that contains all mythic mob display names
        String[] names = new String[regions.size()];
        int j = 0;
        for(ProtectedRegion region : regions){
            names[j] = String.valueOf(region.getId());
            j++;
        }
        if(DEBUG){
            log.info("Region Names: " + names.length);
            for(String name : names){
                log.info("Region Name: " + name);
            }
        }

        inventory.clear();
        addPaginatedMenuBorder();

        for(int i = 0; i < getMaxItemsPerPage(); i++){
            index = i + (page * getMaxItemsPerPage());
            if(index >= names.length) break;
            if(!names[index].isBlank() && !names[index].equalsIgnoreCase("null")){
                if(TableList.getLoadedVanillaTables().containsKey(names[index])){
                    VanillaLootTable vlt = TableList.getLoadedVanillaTables().get(names[index]);
                    inventory.addItem(createItem(
                            Material.ENDER_CHEST,
                            vlt.getVanillaTableName(),
                            ChatColor.translateAlternateColorCodes('&', "&eGenerate Region Loot&7: " + vlt.isKeepVanillaLoot()),
                            ChatColor.translateAlternateColorCodes('&', "&eAssociated Tables&7: " + vlt.getAssociatedTableList().size())
                    ));
                }
                else{
                    inventory.addItem(createItem(
                            Material.CHEST,
                            names[index]));
                }
            }
        }

        return inventory;
    }
}

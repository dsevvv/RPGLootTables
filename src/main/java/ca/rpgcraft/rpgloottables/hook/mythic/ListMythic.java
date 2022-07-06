package ca.rpgcraft.rpgloottables.hook.mythic;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.menu.admin.EditVanillaTable;
import ca.rpgcraft.rpgloottables.menu.admin.MainMenu;
import ca.rpgcraft.rpgloottables.menu.standard.PaginatedMenu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import io.lumine.mythic.api.mobs.MythicMob;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

public class ListMythic extends PaginatedMenu {

    private MythicMobsHandler mythicHandler = RPGLootTables.getInstance().getMythicMobsHandler();
    private Logger log = RPGLootTables.getInstance().getLogger();

    private static final boolean DEBUG = false;

    public ListMythic(PlayerMenuManager playerMenuManager, String inventoryName) {
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
                if(index + 1 >= RPGLootTables.getInstance().getMythicMobsHandler().getActiveMobs().size()){
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
        Collection<MythicMob> mythicMobs = RPGLootTables.getInstance().getMythicMobsHandler().getMythicMobs();
        if(DEBUG){
            log.info("MythicMob Count: " + mythicMobs.size());
            mythicMobs.forEach(mob -> {
                log.info("MythicMob: " + mob.toString());
            });
        }
        //creates an array of strings that contains all mythic mob display names
        String[] names = new String[mythicMobs.size()];
        int j = 0;
        for(MythicMob mythicMob : mythicMobs){
            names[j] = String.valueOf(mythicMob.getDisplayName());
            j++;
        }
        if(DEBUG){
            log.info("MythicMob Names: " + names.length);
            for(String name : names){
                log.info("MythicMob Name: " + name);
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
                            ChatColor.translateAlternateColorCodes('&', "&eGenerate Mythic Loot&7: " + vlt.isKeepVanillaLoot()),
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

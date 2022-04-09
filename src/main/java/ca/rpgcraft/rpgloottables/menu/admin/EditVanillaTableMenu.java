package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import ca.rpgcraft.rpgloottables.util.TableListUtility;
import ca.rpgcraft.rpgloottables.util.VanillaLootTableUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditVanillaTableMenu extends Menu {
    public EditVanillaTableMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 10:
                playerMenuUtility.setEnabled(!playerMenuUtility.isEnabled());
                if(playerMenuUtility.isEnabled()){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Vanilla Loot &aon&e."));
                }else{
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Vanilla Loot &coff&e."));
                }
                open();
                break;
            case 12:
                new ListAddCustomTableMenu(playerMenuUtility, "&0         Add Custom Table").open();
                break;
            case 14:
                new ListRemoveCustomTableMenu(playerMenuUtility, "&0       Remove Custom Table").open();
                break;
            case 16:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSaved &6" + playerMenuUtility.getLootTableName().replace("minecraft:", "") + "&a."));
                if(!playerMenuUtility.isEnabled() || playerMenuUtility.getAssociatedTables().size() > 0) //we are only saving to memory if defaults have been modified
                    TableListUtility.getLoadedVanillaTables().put(playerMenuUtility.getLootTableName(), new VanillaLootTableUtility(playerMenuUtility.getLootTableName(), playerMenuUtility.getAssociatedTables(), playerMenuUtility.isEnabled()));
                else
                    TableListUtility.getLoadedVanillaTables().remove(playerMenuUtility.getLootTableName());
                if(playerMenuUtility.getLootTableName().replace("minecraft:", "").contains("chests")){
                    new ListChestMenu(playerMenuUtility, "    &0Vanilla Chest Loot Tables").open();
                }else{
                    new ListMobMenu(playerMenuUtility, "     &0Vanilla Mob Loot Tables").open();
                }
                break;
            case 22:
                if(playerMenuUtility.getLootTableName().replace("minecraft:", "").contains("chests")){
                    new ListChestMenu(playerMenuUtility, "    &0Vanilla Chest Loot Tables").open();
                }else{
                    new ListMobMenu(playerMenuUtility, "     &0Vanilla Mob Loot Tables").open();
                }
                break;
            default:
                open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, InventoryType.CHEST, ChatColor.translateAlternateColorCodes('&', "&0Edit " + playerMenuUtility.getLootTableName().replace("minecraft:", "")));

        addMenuBorderSmall(inv, false);

        inv.setItem(10, toggleGlobalStatus());
        inv.setItem(12, createItem(
                Material.GREEN_CONCRETE,
                ChatColor.translateAlternateColorCodes('&', "&aAdd Custom Table")
        ));
        inv.setItem(14, createItem(
                Material.WRITABLE_BOOK,
                ChatColor.translateAlternateColorCodes('&', "&eRemove Custom Table")
        ));
        inv.setItem(16, createItem(
                Material.EMERALD_BLOCK,
                ChatColor.translateAlternateColorCodes('&', "&aSave and Quit")
        ));

        return inv;
    }

    private ItemStack toggleGlobalStatus(){
        ItemStack itemStack = new ItemStack(Material.STONE);
        if(playerMenuUtility.isEnabled()){
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table generate"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7it's vanilla loot?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&aEnabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Vanilla Loot"));
            itemStack.setType(Material.GREEN_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }else {
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table generate"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7it's vanilla loot?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&cDisabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Vanilla Loot"));
            itemStack.setType(Material.RED_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }
    }
}

package ca.rpgcraft.rpgloottables.menu.standard;

import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class Menu implements InventoryHolder {

    protected PlayerMenuUtility playerMenuUtility;

    protected static ItemStack BLANK_ITEM;
    protected static ItemStack BACK_ITEM;
    protected static ItemStack CLOSE_ITEM;

    public Menu(PlayerMenuUtility playerMenuUtility){
        this.playerMenuUtility = playerMenuUtility;

        ItemStack blank = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta blankMeta = blank.getItemMeta();
        blankMeta.setDisplayName(ChatColor.WHITE + "");
        blank.setItemMeta(blankMeta);
        BLANK_ITEM = blank;

        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Back"));
        back.setItemMeta(backMeta);
        BACK_ITEM = back;

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Close"));
        close.setItemMeta(closeMeta);
        CLOSE_ITEM = close;
    }

    /**
     * This method will handle what happens when a menu is clicked.
     * @param whoClicked Player who clicked the inventory
     * @param rawSlot Slot index of the clicked slot
     */
    public abstract void onMenuClick(Player whoClicked, int rawSlot);

    /**
     * Opens the menu for the owner of the associated PlayerMenuUtility
     */
    public void open(){
        playerMenuUtility.getOwner().openInventory(getInventory());
    }

    /**
     * Creates an ItemStack of given Material type with given display name String.
     * @param material Material type to set on item
     * @param displayName Display name to set on item
     * @return ItemStack of type material with name displayName (color code friendly, use '&')
     */
    public ItemStack createItem(Material material, String displayName){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates an ItemStack of given Material type with given display name String, and given lore.
     * @param material Material type to set on item
     * @param displayName String to be set as ItemStack display name
     * @param lore Strings to be added to ItemStack lore
     * @return ItemStack of type material, a name displayName, and lore from provided lore Strings.
     */
    public ItemStack createItem(Material material, String displayName, String... lore){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        meta.setLore(Arrays.stream(lore).toList());
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Adds menu border to provided single chest
     * @param singleChest Inventory 28 slots to have border
     * @param willClose if true, inventory will have a "close" button instead of a "back" button
     */
    public void addMenuBorderSmall(Inventory singleChest, boolean willClose){
        for(int i = 0; i < 27; i++){
            if((i > 9 && i < 17) || i == 22) continue;
            singleChest.setItem(i, BLANK_ITEM);
        }
        if(willClose)
            singleChest.setItem(22, CLOSE_ITEM);
        else
            singleChest.setItem(22, BACK_ITEM);
    }

    /**
     * Adds menu border to provided double chest
     * @param doubleChest Inventory 54 slots to have border
     * @param willClose if true, inventory will have a "close" button instead of a "back" button
     */
    public void addMenuBorderLarge(Inventory doubleChest, boolean willClose){
        for(int i = 0; i < 54; i++){
            if((i > 9 && i < 17)
                    || (i > 18 && i < 26)
                    || (i > 27 && i < 35)
                    || (i > 36 && i < 44)
                    || i == 49) continue;
            doubleChest.setItem(i, BLANK_ITEM);
        }
        if(willClose)
            doubleChest.setItem(49, CLOSE_ITEM);
        else
            doubleChest.setItem(49, BACK_ITEM);
    }
}

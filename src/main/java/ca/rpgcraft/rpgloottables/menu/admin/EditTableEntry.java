package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.item.TableEntry;
import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EditTableEntry extends Menu {

    private ItemStack itemStack;
    private final int index;
    private int weight;
    private int min;
    private int max;

    public EditTableEntry(PlayerMenuManager playerMenuManager, int index, ItemStack itemStack, int weight, int min, int max) {
        super(playerMenuManager);
        this.index = index;
        this.itemStack = itemStack;
        this.weight = weight;
        this.min = min;
        this.max = max;
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 10:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aIf you wish to &ccancel&a, click the left piece of paper."));
                new AnvilGUI.Builder()
                        .itemLeft(new ItemStack(Material.PAPER))
                        .text(String.valueOf(weight))
                        .title(ChatColor.translateAlternateColorCodes('&', "&0Enter new weight..."))
                        .plugin(RPGLootTables.getPlugin(RPGLootTables.class))
                        .onClose(player -> open())
                        .onLeftInputClick(player -> {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWeight change cancelled."));
                            open();
                        })
                        .onComplete((player, text) -> {
                            if(!NumberUtils.isNumber(text))
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cNot a number!"));
                            int newWeight = Integer.parseInt(text);
                            if(newWeight < 1)
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cCannot be 0 or less!"));
                            setWeight(newWeight);
                            open();
                            return AnvilGUI.Response.close();
                        })
                        .open(whoClicked);
                break;
            case 12:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aIf you wish to &ccancel&a, click the left piece of paper."));
                new AnvilGUI.Builder()
                        .itemLeft(new ItemStack(Material.PAPER))
                        .text(String.valueOf(min))
                        .title(ChatColor.translateAlternateColorCodes('&', "&0Enter new min..."))
                        .plugin(RPGLootTables.getPlugin(RPGLootTables.class))
                        .onClose(player -> open())
                        .onLeftInputClick(player -> {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMin Amount change cancelled."));
                            open();
                        })
                        .onComplete((player, text) -> {
                            if(!NumberUtils.isNumber(text))
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cNot a number!"));
                            int newMin = Integer.parseInt(text);
                            if(newMin < 1)
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cCannot be 0 or less!"));
                            if(newMin > max)
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cGreater than max!"));
                            setMin(newMin);
                            open();
                            return AnvilGUI.Response.close();
                        })
                        .open(whoClicked);
                break;
            case 14:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aIf you wish to &ccancel&a, click the left piece of paper."));
                new AnvilGUI.Builder()
                        .itemLeft(new ItemStack(Material.PAPER))
                        .text(String.valueOf(max))
                        .title(ChatColor.translateAlternateColorCodes('&', "&0Enter new max..."))
                        .plugin(RPGLootTables.getPlugin(RPGLootTables.class))
                        .onClose(player -> open())
                        .onLeftInputClick(player -> {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMax Amount change cancelled."));
                            open();
                        })
                        .onComplete((player, text) -> {
                            if(!NumberUtils.isNumber(text))
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cNot a number!"));
                            int newMax = Integer.parseInt(text);
                            if(newMax < 1)
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cCannot be 0 or less!"));
                            if(newMax < min)
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cLess than min!"));
                            setMax(newMax);
                            open();
                            return AnvilGUI.Response.close();
                        })
                        .open(whoClicked);
                break;
            case 16:
                playerMenuManager.getTableEntries().set(index, new TableEntry(itemStack, weight, min, max));
                new EditCustomTable(playerMenuManager).open();
                break;
            case 22:
                new EditCustomTable(playerMenuManager).open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        String name = itemStack.getItemMeta().getDisplayName().isBlank() ? WordUtils.capitalizeFully(itemStack.getType().name().replace("_", " ")) : itemStack.getItemMeta().getDisplayName();
        Inventory inv = Bukkit.createInventory(this, InventoryType.CHEST, ChatColor.translateAlternateColorCodes('&', "&0Editing " + name));

        addMenuBorderSmall(inv, false);
        inv.setItem(10, createItem(
                Material.WRITABLE_BOOK,
                ChatColor.translateAlternateColorCodes('&', "&eSet Weight"),
                ChatColor.translateAlternateColorCodes('&', "&eWeight&7: " + weight)
        ));
        inv.setItem(12, createItem(
                Material.WRITABLE_BOOK,
                ChatColor.translateAlternateColorCodes('&', "&eSet Min Amount"),
                ChatColor.translateAlternateColorCodes('&', "&eMin&7: " + min)
        ));
        inv.setItem(14, createItem(
                Material.WRITABLE_BOOK,
                ChatColor.translateAlternateColorCodes('&', "&eSet Max Amount"),
                ChatColor.translateAlternateColorCodes('&', "&eMax&7: " + max)
        ));
        inv.setItem(16, createItem(
                Material.EMERALD_BLOCK,
                ChatColor.translateAlternateColorCodes('&', "&aSave and Quit")
        ));

        return inv;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getIndex() {
        return index;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}

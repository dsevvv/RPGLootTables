package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.CustomLootTableUtility;
import ca.rpgcraft.rpgloottables.util.LootTableUtility;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateCustomTableMenu extends Menu {

    private NumberFormat doubleFormat = new DecimalFormat("#0.00");

    private boolean isToggled = playerMenuUtility.isEnabled();

    public CreateCustomTableMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 11:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aIf you wish to &ccancel&a, click the left piece of paper."));
                new AnvilGUI.Builder()
                        .itemLeft(new ItemStack(Material.PAPER))
                        .text(playerMenuUtility.getLootTableName())
                        .title(ChatColor.translateAlternateColorCodes('&', "&0Enter new name..."))
                        .plugin(RPGLootTables.getPlugin(RPGLootTables.class))
                        .onLeftInputClick(player -> {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cName change cancelled."));
                            open();
                        })
                        .onComplete((player, text) -> {
                            playerMenuUtility.setLootTableName(ChatColor.translateAlternateColorCodes('&', text));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTable has been renamed to &6" + playerMenuUtility.getLootTableName() + "&a."));
                            open();
                            return AnvilGUI.Response.close();
                        })
                        .open(whoClicked);
                break;
            case 13:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aIf you wish to &ccancel&a, click the left piece of paper."));
                new AnvilGUI.Builder()
                        .itemLeft(new ItemStack(Material.PAPER))
                        .text(doubleFormat.format(playerMenuUtility.getChance()))
                        .title(ChatColor.translateAlternateColorCodes('&', "&0Enter new chance..."))
                        .plugin(RPGLootTables.getPlugin(RPGLootTables.class))
                        .onLeftInputClick(player -> {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cChance change cancelled."));
                            open();
                        })
                        .onComplete((player, text) -> {
                            try {
                                Double.parseDouble(text);
                            }catch (Exception ignored){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid chance format! Must be a number between 0.00 - 100.00."));
                                open();
                                return AnvilGUI.Response.close();
                            }
                            if(Double.parseDouble(text) < 0 || Double.parseDouble(text) > 100){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid chance! Must be a number between 0.00 - 100.00."));
                                open();
                                return AnvilGUI.Response.close();
                            }
                            playerMenuUtility.setChance(Double.parseDouble(text));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChance has been changed to &6" + doubleFormat.format(playerMenuUtility.getChance()) + "&a."));
                            open();
                            return AnvilGUI.Response.close();
                        })
                        .open(whoClicked);
                break;
            case 15:
                playerMenuUtility.setEnabled(!playerMenuUtility.isEnabled());
                if(playerMenuUtility.isEnabled()){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Global setting &aon&e."));
                }else{
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Global setting &coff&e."));
                }
                open();
                break;
            case 19:
                if(playerMenuUtility.getMinTableItems() == 0){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMinimum items cannot be negative!"));
                    open();
                    break;
                }
                playerMenuUtility.setMinTableItems(playerMenuUtility.getMinTableItems() - 1);
                open();
                break;
            case 21:
                if(playerMenuUtility.getMinTableItems() == playerMenuUtility.getMaxTableItems()){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMinimum items cannot larger than maximum items!"));
                    open();
                    break;
                }
                playerMenuUtility.setMinTableItems(playerMenuUtility.getMinTableItems() + 1);
                open();
                break;
            case 23:
                if(playerMenuUtility.getMaxTableItems() == 1){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMaximum items cannot be 0!"));
                    open();
                    break;
                }
                if(playerMenuUtility.getMinTableItems() == playerMenuUtility.getMaxTableItems()){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMaximum items cannot smaller than minimum items!"));
                    open();
                    break;
                }
                playerMenuUtility.setMaxTableItems(playerMenuUtility.getMaxTableItems() - 1);
                open();
                break;
            case 25:
                playerMenuUtility.setMaxTableItems(playerMenuUtility.getMaxTableItems() + 1);
                open();
                break;
            case 43:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + playerMenuUtility.getLootTableName() + " saved!"));
                CustomLootTableUtility customLootTableUtility = new CustomLootTableUtility(playerMenuUtility.getLootTableName(), playerMenuUtility.getItems(), playerMenuUtility.isEnabled(), playerMenuUtility.getChance(), playerMenuUtility.getMinTableItems(), playerMenuUtility.getMaxTableItems());
                LootTableUtility.getLoadedTables().put(UUID.randomUUID(), customLootTableUtility);
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aLoaded Tables: "));
                LootTableUtility.getLoadedTables().forEach((uuid, customLootTable) -> {
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + customLootTable.getName()));
                });
                new ChoiceCustomTableMenu(playerMenuUtility).open();
                break;
            case 49:
                new ChoiceCustomTableMenu(playerMenuUtility).open();
                playerMenuUtility.setLootTableName("");
                break;
            default:
                open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', "&0       Custom Table Creation"));

        for(int i = 0; i < 54; i++){
            if((i > 9 && i < 17)
            || (i > 18 && i < 26)
            || (i > 27 && i < 35)
            || (i > 36 && i < 44)
            || i == 49) continue;
            inv.setItem(i, BLANK_ITEM);
        }
        inv.setItem(49, BACK_ITEM);

        ItemStack renameItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta renameMeta = renameItem.getItemMeta();
        List<String> renameLore = new ArrayList<>();
        renameLore.add(ChatColor.GRAY + playerMenuUtility.getLootTableName());
        renameMeta.setLore(renameLore);
        renameMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eRename Table"));
        renameItem.setItemMeta(renameMeta);

        ItemStack chanceButton = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta chanceMeta = chanceButton.getItemMeta();
        List<String> chanceLore = new ArrayList<>();
        chanceLore.add(ChatColor.translateAlternateColorCodes('&', "&7This setting controls"));
        chanceLore.add(ChatColor.translateAlternateColorCodes('&', "&7the likeliness that this"));
        chanceLore.add(ChatColor.translateAlternateColorCodes('&', "&7table will generate items."));
        chanceLore.add("");
        chanceLore.add(ChatColor.translateAlternateColorCodes('&', "&e" + doubleFormat.format(playerMenuUtility.getChance()) + "%"));
        chanceMeta.setLore(chanceLore);
        chanceMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eChange Roll Chance"));
        chanceButton.setItemMeta(chanceMeta);

        ItemStack decreaseMinButton = new ItemStack(Material.CRIMSON_BUTTON);
        ItemMeta dMinMeta = decreaseMinButton.getItemMeta();
        List<String> dMinLore = new ArrayList<>();
        dMinLore.add(ChatColor.GRAY + "Decrease the minimum number");
        dMinLore.add(ChatColor.GRAY + "of items that will generate");
        dMinLore.add(ChatColor.GRAY + "from this specific table.");
        dMinLore.add("");
        dMinLore.add(ChatColor.YELLOW + "Min: " + ChatColor.GRAY + String.valueOf(playerMenuUtility.getMinTableItems()));
        dMinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cDecrease Minimum Items"));
        dMinMeta.setLore(dMinLore);
        decreaseMinButton.setItemMeta(dMinMeta);

        ItemStack increaseMinButton = new ItemStack(Material.WARPED_BUTTON);
        ItemMeta iMinMeta = increaseMinButton.getItemMeta();
        List<String> iMinLore = new ArrayList<>();
        iMinLore.add(ChatColor.GRAY + "Increase the minimum number");
        iMinLore.add(ChatColor.GRAY + "of items that will generate");
        iMinLore.add(ChatColor.GRAY + "from this specific table.");
        iMinLore.add("");
        iMinLore.add(ChatColor.YELLOW + "Min: " + ChatColor.GRAY + String.valueOf(playerMenuUtility.getMinTableItems()));
        iMinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aIncrease Minimum Items"));
        iMinMeta.setLore(iMinLore);
        increaseMinButton.setItemMeta(iMinMeta);

        ItemStack decreaseMaxButton = new ItemStack(Material.CRIMSON_BUTTON);
        ItemMeta dMaxMeta = decreaseMaxButton.getItemMeta();
        List<String> dMaxLore = new ArrayList<>();
        dMaxLore.add(ChatColor.GRAY + "Decrease the maximum number");
        dMaxLore.add(ChatColor.GRAY + "of items that will generate");
        dMaxLore.add(ChatColor.GRAY + "from this specific table.");
        dMaxLore.add("");
        dMaxLore.add(ChatColor.YELLOW + "Max: " + ChatColor.GRAY + String.valueOf(playerMenuUtility.getMaxTableItems()));
        dMaxMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cDecrease Maximum Items"));
        dMaxMeta.setLore(dMaxLore);
        decreaseMaxButton.setItemMeta(dMaxMeta);

        ItemStack increaseMaxButton = new ItemStack(Material.WARPED_BUTTON);
        ItemMeta iMaxMeta = increaseMaxButton.getItemMeta();
        List<String> iMaxLore = new ArrayList<>();
        iMaxLore.add(ChatColor.GRAY + "Increase the maximum number");
        iMaxLore.add(ChatColor.GRAY + "of items that will generate");
        iMaxLore.add(ChatColor.GRAY + "from this specific table.");
        iMaxLore.add("");
        iMaxLore.add(ChatColor.YELLOW + "Max: " + ChatColor.GRAY + String.valueOf(playerMenuUtility.getMaxTableItems()));
        iMaxMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aIncrease Maximum Items"));
        iMaxMeta.setLore(iMaxLore);
        increaseMaxButton.setItemMeta(iMaxMeta);

        ItemStack confirmButton = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirmButton.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aSave and Exit"));
        confirmButton.setItemMeta(confirmMeta);

        inv.setItem(11, renameItem);
        inv.setItem(13, chanceButton);
        inv.setItem(15, toggleGlobalStatus());
        inv.setItem(19, decreaseMinButton);
        inv.setItem(21, increaseMinButton);
        inv.setItem(23, decreaseMaxButton);
        inv.setItem(25, increaseMaxButton);
        inv.setItem(43, confirmButton);

        return inv;
    }

    private ItemStack toggleGlobalStatus(){
        ItemStack itemStack = new ItemStack(Material.STONE);
        if(playerMenuUtility.isEnabled()){
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table roll each"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7time loot is generated?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&aEnabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Global Table"));
            itemStack.setType(Material.GREEN_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }else {
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table roll each"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7time loot is generated?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&cDisabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Global Table"));
            itemStack.setType(Material.RED_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }
    }
}

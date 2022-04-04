package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.menu.standard.Menu;
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

        inv.setItem(11, renameItem);
        inv.setItem(13, chanceButton);
        inv.setItem(15, toggleGlobalStatus());

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

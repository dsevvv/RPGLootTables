package ca.rpgcraft.rpgloottables.menu.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.item.TableEntry;
import ca.rpgcraft.rpgloottables.menu.standard.Menu;
import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EditCustomTable extends Menu {

    private NumberFormat doubleFormat = new DecimalFormat("#0.00");
    private List<String> oldNames = new LinkedList<>();

    public EditCustomTable(PlayerMenuManager playerMenuManager) {
        super(playerMenuManager);
    }

    @Override
    public void onMenuClick(Player whoClicked, int rawSlot) {
        switch (rawSlot){
            case 11:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aIf you wish to &ccancel&a, click the left piece of paper."));
                new AnvilGUI.Builder()
                        .itemLeft(new ItemStack(Material.PAPER))
                        .text(playerMenuManager.getLootTableName())
                        .title(ChatColor.translateAlternateColorCodes('&', "&0Enter new name..."))
                        .plugin(RPGLootTables.getPlugin(RPGLootTables.class))
                        .onClose(player -> open())
                        .onLeftInputClick(player -> {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cName change cancelled."));
                            open();
                        })
                        .onComplete((player, text) -> {
                            if(TableList.getLoadedCustomTables().containsKey(text)){
                                return AnvilGUI.Response.text(ChatColor.translateAlternateColorCodes('&', "&cName taken!"));
                            }
                            oldNames.add(playerMenuManager.getLootTableName());
                            playerMenuManager.setLootTableName(ChatColor.translateAlternateColorCodes('&', text));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTable has been renamed to &6" + playerMenuManager.getLootTableName() + "&a."));
                            open();
                            return AnvilGUI.Response.close();
                        })
                        .open(whoClicked);
                break;
            case 13:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aIf you wish to &ccancel&a, click the left piece of paper."));
                new AnvilGUI.Builder()
                        .itemLeft(new ItemStack(Material.PAPER))
                        .text(doubleFormat.format(playerMenuManager.getChance()))
                        .title(ChatColor.translateAlternateColorCodes('&', "&0Enter new chance..."))
                        .plugin(RPGLootTables.getPlugin(RPGLootTables.class))
                        .onClose(player -> open())
                        .onLeftInputClick(player -> open())
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
                            playerMenuManager.setChance(Double.parseDouble(text));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChance has been changed to &6" + doubleFormat.format(playerMenuManager.getChance()) + "&a."));
                            open();
                            return AnvilGUI.Response.close();
                        })
                        .open(whoClicked);
                break;
            case 15:
                playerMenuManager.setGlobalChest(!playerMenuManager.isGlobalChest());
                if(playerMenuManager.isGlobalChest()){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Global Chest setting &aon&e."));
                }else{
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Global Chest setting &coff&e."));
                }
                open();
                break;
            case 16:
                playerMenuManager.setGlobalMob(!playerMenuManager.isGlobalMob());
                if(playerMenuManager.isGlobalMob()){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Global Mob setting &aon&e."));
                }else{
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eToggled Global Mob setting &coff&e."));
                }
                open();
                break;
            case 19:
                if(playerMenuManager.getMinTableItems() == 0){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMinimum items cannot be negative!"));
                    open();
                    break;
                }
                playerMenuManager.setMinTableItems(playerMenuManager.getMinTableItems() - 1);
                open();
                break;
            case 21:
                if(playerMenuManager.getMinTableItems() == playerMenuManager.getMaxTableItems()){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMinimum items cannot larger than maximum items!"));
                    open();
                    break;
                }
                playerMenuManager.setMinTableItems(playerMenuManager.getMinTableItems() + 1);
                open();
                break;
            case 23:
                if(playerMenuManager.getMaxTableItems() == 1){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMaximum items cannot be 0!"));
                    open();
                    break;
                }
                if(playerMenuManager.getMinTableItems() == playerMenuManager.getMaxTableItems()){
                    whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMaximum items cannot smaller than minimum items!"));
                    open();
                    break;
                }
                playerMenuManager.setMaxTableItems(playerMenuManager.getMaxTableItems() - 1);
                open();
                break;
            case 25:
                playerMenuManager.setMaxTableItems(playerMenuManager.getMaxTableItems() + 1);
                open();
                break;
            case 31:
                whoClicked.sendTitle(
                        ChatColor.translateAlternateColorCodes('&', "&bAdd Item"),
                        "",
                        10,
                        40,
                        10);
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aType &eadd &ato add the item in your main hand to the loot table.\nType &ecancel &ato cancel item entry."));
                whoClicked.closeInventory();
                class PlayerChatListener implements Listener {
                    @EventHandler
                    public void onPlayerChat(PlayerChatEvent e){
                        if(!e.getPlayer().equals(whoClicked)) return;
                        switch (e.getMessage()){
                            case "add":
                                String itemName = whoClicked.getInventory().getItemInMainHand().getItemMeta().getDisplayName().isBlank() ?
                                       ChatColor.YELLOW + WordUtils.capitalizeFully(whoClicked.getInventory().getItemInMainHand().getType().name().replace("_", " ")) : ChatColor.YELLOW + whoClicked.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                                e.setCancelled(true);
                                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', itemName + " &aadded to &6" + playerMenuManager.getLootTableName() + "&a."));
                                ItemStack singleItem = whoClicked.getInventory().getItemInMainHand().clone();
                                singleItem.setAmount(1);
                                playerMenuManager.getTableEntries().add(new TableEntry(singleItem, 1, 1, 1));
                                open();
                                HandlerList.unregisterAll(this);
                                break;
                            case "cancel":
                                e.setCancelled(true);
                                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cItem entry cancelled."));
                                open();
                                HandlerList.unregisterAll(this);
                                break;
                        }
                    }
                }
                Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), RPGLootTables.getPlugin(RPGLootTables.class));
                break;
            case 33:
                new ListItems(playerMenuManager, ChatColor.translateAlternateColorCodes('&', "&0            Item List")).open();
                break;
            case 37:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + playerMenuManager.getLootTableName() + " &cdeleted!"));
                TableList.getLoadedCustomTables().remove(playerMenuManager.getLootTableName());
                for(String name : oldNames)
                    TableList.getLoadedCustomTables().remove(name);
                new ChoiceCustomTable(playerMenuManager).open();
                break;
            case 43:
                whoClicked.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + playerMenuManager.getLootTableName() + " &asaved!"));
                CustomLootTable customLootTable = new CustomLootTable(playerMenuManager.getLootTableName(), playerMenuManager.getTableEntries(), playerMenuManager.isGlobalChest(), playerMenuManager.isGlobalMob(), playerMenuManager.getChance(), playerMenuManager.getMinTableItems(), playerMenuManager.getMaxTableItems());
                TableList.getLoadedCustomTables().put(playerMenuManager.getLootTableName(), customLootTable);
                for(VanillaLootTable vanillaLootTable : TableList.getLoadedVanillaTables().values()){
                    int i = 0;
                    for(CustomLootTable customUtility : vanillaLootTable.getAssociatedTableList()){
                        if(customUtility.getName().equalsIgnoreCase(playerMenuManager.getLootTableName())
                        || oldNames.contains(customUtility.getName())){
                            vanillaLootTable.getAssociatedTableList().remove(i);
                            vanillaLootTable.getAssociatedTableList().add(customLootTable);
                        }
                        i++;
                    }
                }
                for(String name : oldNames)
                    TableList.getLoadedCustomTables().remove(name);
                new ChoiceCustomTable(playerMenuManager).open();
                break;
            case 49:
                new ChoiceCustomTable(playerMenuManager).open();
                playerMenuManager.setLootTableName("");
                break;
            default:
                open();
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', "&0Editing " + playerMenuManager.getLootTableName()));

        addMenuBorderLarge(inv, false);

        inv.setItem(11, createItem(
                Material.WRITABLE_BOOK,
                ChatColor.translateAlternateColorCodes('&', "&eRename Table"),
                ChatColor.GRAY + playerMenuManager.getLootTableName())
        );
        inv.setItem(13, createItem(
                Material.WRITABLE_BOOK,
                ChatColor.translateAlternateColorCodes('&', "&eChange Roll Chance"),
                ChatColor.translateAlternateColorCodes('&', "&7This setting controls"),
                ChatColor.translateAlternateColorCodes('&', "&7the likeliness that this"),
                ChatColor.translateAlternateColorCodes('&', "&7table will generate items."),
                "",
                ChatColor.translateAlternateColorCodes('&', "&e" + doubleFormat.format(playerMenuManager.getChance()) + "%")
        ));
        inv.setItem(15, toggleGlobalChest());
        inv.setItem(16, toggleGlobalMob());
        inv.setItem(19, createItem(
                Material.CRIMSON_BUTTON,
                ChatColor.translateAlternateColorCodes('&', "&cDecrease Minimum Items"),
                ChatColor.GRAY + "Decrease the minimum number",
                ChatColor.GRAY + "of items that will generate",
                ChatColor.GRAY + "from this specific table.",
                "",
                ChatColor.YELLOW + "Min: " + ChatColor.GRAY + playerMenuManager.getMinTableItems()
        ));
        inv.setItem(21, createItem(
                Material.WARPED_BUTTON,
                ChatColor.translateAlternateColorCodes('&', "&aIncrease Minimum Items"),
                ChatColor.GRAY + "Increase the minimum number",
                ChatColor.GRAY + "of items that will generate",
                ChatColor.GRAY + "from this specific table.",
                "",
                ChatColor.YELLOW + "Min: " + ChatColor.GRAY + playerMenuManager.getMinTableItems()
        ));
        inv.setItem(23, createItem(
                Material.CRIMSON_BUTTON,
                ChatColor.translateAlternateColorCodes('&', "&cDecrease Maximum Items"),
                ChatColor.GRAY + "Decrease the maximum number",
                ChatColor.GRAY + "of items that will generate",
                ChatColor.GRAY + "from this specific table.",
                "",
                ChatColor.YELLOW + "Max: " + ChatColor.GRAY + playerMenuManager.getMaxTableItems()
        ));
        inv.setItem(25, createItem(
                Material.WARPED_BUTTON,
                ChatColor.translateAlternateColorCodes('&', "&aIncrease Maximum Items"),
                ChatColor.GRAY + "Increase the maximum number",
                ChatColor.GRAY + "of items that will generate",
                ChatColor.GRAY + "from this specific table.",
                "",
                ChatColor.YELLOW + "Max: " + ChatColor.GRAY + playerMenuManager.getMaxTableItems()
        ));
        inv.setItem(31, createItem(
                Material.GREEN_CONCRETE,
                ChatColor.translateAlternateColorCodes('&', "&aAdd Item")
        ));
        inv.setItem(33, createItem(
                Material.WRITABLE_BOOK,
                ChatColor.translateAlternateColorCodes('&', "&eEdit Items"),
                ChatColor.translateAlternateColorCodes('&', "&eItems: &7" + playerMenuManager.getTableEntries().size())
        ));
        inv.setItem(37, createItem(
                Material.REDSTONE_BLOCK,
                ChatColor.translateAlternateColorCodes('&', "&cDelete")
        ));
        inv.setItem(43, createItem(
                Material.EMERALD_BLOCK,
                ChatColor.translateAlternateColorCodes('&', "&aSave and Exit")
        ));

        return inv;
    }

    private ItemStack toggleGlobalChest(){
        ItemStack itemStack = new ItemStack(Material.STONE);
        if(playerMenuManager.isGlobalChest()){
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table roll each"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7time chest loot is generated?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&aEnabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Global Chest Table"));
            itemStack.setType(Material.GREEN_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }else {
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table roll each"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7time chest loot is generated?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&cDisabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Global Chest Table"));
            itemStack.setType(Material.RED_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }
    }

    private ItemStack toggleGlobalMob(){
        ItemStack itemStack = new ItemStack(Material.STONE);
        if(playerMenuManager.isGlobalMob()){
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table roll each"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7time mob loot is generated?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&aEnabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Global Mob Table"));
            itemStack.setType(Material.GREEN_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }else {
            ItemMeta newMeta = itemStack.getItemMeta();
            List<String> newLore = new ArrayList<>();
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7Will this table roll each"));
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&7time mob loot is generated?"));
            newLore.add("");
            newLore.add(ChatColor.translateAlternateColorCodes('&', "&cDisabled"));
            newMeta.setLore(newLore);
            newMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eToggle Global Mob Table"));
            itemStack.setType(Material.RED_CONCRETE);
            itemStack.setItemMeta(newMeta);
            return itemStack;
        }
    }
}

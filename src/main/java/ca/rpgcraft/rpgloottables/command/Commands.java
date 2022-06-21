package ca.rpgcraft.rpgloottables.command;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import ca.rpgcraft.rpgloottables.util.TableList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Executes RPGLootTable commands.
 */
public class Commands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOnly players can use this command."));
            return false;
        }

        if(args.length == 0){
            sendHelpMessage(player);
            return false;
        }

        if(args[0].equalsIgnoreCase("help")){
            sendHelpMessage(player);
            return false;
        }

        if(args[0].equalsIgnoreCase("menu")
        || args[0].equalsIgnoreCase("m")){
            if(!player.hasPermission("rpgloot.admin")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do that."));
                return false;
            }
            PlayerMenuManager playerMenuManager = new PlayerMenuManager(player);
            ca.rpgcraft.rpgloottables.menu.admin.MainMenu mainMenu = new ca.rpgcraft.rpgloottables.menu.admin.MainMenu(playerMenuManager);
            mainMenu.open();
            return false;
        }

        if(args[0].equalsIgnoreCase("reload")){
            if(!player.hasPermission("rpgloot.reload")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do that."));
                return false;
            }
            RPGLootTables.getInstance().reloadConfig();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aconfig.yml reloaded."));
            return false;
        }

        if(args[0].equalsIgnoreCase("version")
        || args[0].equalsIgnoreCase("ver")
        || args[0].equalsIgnoreCase("v")){
            if(!player.hasPermission("rpgloot.version")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do that."));
                return false;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Author:&a dSevvv."));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + RPGLootTables.getPlugin(RPGLootTables.class).getName() + " &av" + RPGLootTables.getPlugin(RPGLootTables.class).getDescription().getVersion()) + ".");
            return false;
        }

        if(args[0].equalsIgnoreCase("bank_voucher")){
            if(!RPGLootTables.getInstance().isVault()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVault economy hook not found."));
                return false;
            }
            if(!RPGLootTables.getInstance().getConfig().getBoolean("bank_voucher.enabled")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVoucher system is disabled."));
                return false;
            }
            if(args.length == 1){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cl bank_voucher <amount>"));
                return false;
            }
            if(!player.hasPermission("rpgloot.bank_voucher")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to use this command."));
                return false;
            }
            double amount = 0;
            try{
                amount = Double.parseDouble(args[1]);
            }catch (NumberFormatException | NullPointerException e){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid amount specified."));
                return false;
            }
            ItemStack voucher = createBankVoucher(amount);
            player.getInventory().addItem(voucher);
            return false;
        }

        if(args[0].equalsIgnoreCase("loot_voucher")){
            if(!RPGLootTables.getInstance().getConfig().getBoolean("loot_voucher.enabled")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVoucher system is disabled."));
                return false;
            }
            if(args.length == 1){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cl loot_voucher <customTableName>"));
                return false;
            }
            StringBuilder nameBuilder = new StringBuilder();
            for(int i = 1; i < args.length; i++){
                nameBuilder.append(args[i]);
                nameBuilder.append(" ");
            }
            String name = nameBuilder.toString().trim();
            if(TableList.getLoadedCustomTables().containsKey(name)){
                ItemStack item = createLootVoucher(name);
                player.getInventory().addItem(item);
            }
            else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTable " + name + " not found."));
            }
            return false;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            List<String> completions = new ArrayList<>();

            completions.add("help");
            completions.add("menu");
            completions.add("reload");
            completions.add("version");
            completions.add("bank_voucher");
            completions.add("loot_voucher");

            List<String> result = new ArrayList<>();

            for(String a : completions){
                if(a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }

            return result;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("bank_voucher")){
            List<String> completions = new ArrayList<>();
            completions.add("<amount>");

            return completions;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("loot_voucher")){
            List<String> completions = new ArrayList<>();
            for(String name : TableList.getLoadedCustomTables().keySet()){
                if(name.toLowerCase().startsWith(args[1].toLowerCase()))
                    completions.add(name);
            }
            return completions;
        }

        return null;
    }

    private void sendHelpMessage(CommandSender sender){
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                "&e========== &bCustom Loot Tables &e=========="));
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                "&e/cl help &7- &bShows this help message."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                "&6/cl menu&a - Open the menu."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                "&6/cl reload&a - Reload the config.yml."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                "&6/cl version&a - Show the version of the plugin."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                "&6/cl bank_voucher <amount>&a - Get a bank voucher item that will reward the player who right-clicks this item with the amount of money specified on the voucher."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                "&e========== &bCustom Loot Tables &e=========="));
    }

    private ItemStack createBankVoucher(double amount){
        ItemStack item = new ItemStack(Material.valueOf(RPGLootTables.getInstance().getConfig().getString("bank_voucher.type")));
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes(
                '&',
                "&aValue: &b" +
                        RPGLootTables.getInstance().getVaultHandler().getEconomy().format(amount) +
                        "&a."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&a&lRight-click to redeem."));
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(new NamespacedKey(RPGLootTables.getInstance(), "bank_voucher"), PersistentDataType.DOUBLE, amount);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(RPGLootTables.getInstance().getConfig().getString("bank_voucher.name"))));
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createLootVoucher(String name){
        ItemStack item = new ItemStack(Material.valueOf(RPGLootTables.getInstance().getConfig().getString("loot_voucher.type")));
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes(
                '&',
                "&aTable: &b" +
                            name +
                            "&a."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&a&lRight-click to redeem."));
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(new NamespacedKey(RPGLootTables.getInstance(), "loot_voucher"), PersistentDataType.STRING, name);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(RPGLootTables.getInstance().getConfig().getString("loot_voucher.name"))));
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
}

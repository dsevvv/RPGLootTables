package ca.rpgcraft.rpgloottables.hook.vault;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHandler {

    private Economy econ = null;

    public boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    public void redeemVoucher(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        double voucherValue = getVoucherValue(item);
        if(voucherValue == 0) return;
        econ.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), voucherValue);
        item.setAmount(item.getAmount() - 1);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have redeemed a voucher worth &b" + RPGLootTables.getInstance().getVaultHandler().getEconomy().format(voucherValue) + "&a!"));
        event.setCancelled(true);
    }

    public boolean isVoucher(ItemStack item){
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(RPGLootTables.getInstance(), "bank_voucher"), PersistentDataType.DOUBLE);
    }

    public boolean hasEnoughMoney(Player player, double amount){
        return econ.has(Bukkit.getOfflinePlayer(player.getUniqueId()), amount);
    }

    public void withdrawMoney(Player player, double amount){
        econ.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), amount);
    }

    private double getVoucherValue(ItemStack item){
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(RPGLootTables.getInstance(), "bank_voucher"), PersistentDataType.DOUBLE);
    }

    public String format(double amount){
        return econ.format(amount);
    }

    public double getBalance(Player player){
        return econ.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId()));
    }
}

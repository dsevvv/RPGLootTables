package ca.rpgcraft.rpgloottables.listener;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.Random;

public class RedeemLootVoucher implements Listener {

    @EventHandler
    public void onRedeemLootVoucher(PlayerInteractEvent event){
        String name;
        CustomLootTable customLootTable;
        Collection<ItemStack> rewards;

        if(!event.hasItem()
                || !event.getHand().equals(EquipmentSlot.HAND)
                || (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR))) return;
        if(event.getItem() == null) return;
        if(!event.getItem().hasItemMeta()) return;

        if(!isVoucher(event.getItem())) return;

        name = getVoucherTableName(event.getItem());

        if(!TableList.getLoadedCustomTables().containsKey(name)){
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis table no longer exists or has changed names. Contact a server admin to fix this."));
            return;
        }

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have redeemed a loot voucher for the table &6" + name + "&a!"));
        customLootTable = TableList.getLoadedCustomTables().get(name);
        rewards = customLootTable.populateLoot(new Random(), new LootContext.Builder(event.getPlayer().getLocation()).build());
        for(ItemStack item : rewards){
            if(item.getItemMeta().hasDisplayName())
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have received &b" + item.getAmount() + " " + item.getItemMeta().getDisplayName() + "&a!"));
            else
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have received &b" + item.getAmount() + " " + WordUtils.capitalizeFully(item.getType().toString().replace('_', ' ')) + "&a!"));
            if(event.getPlayer().getInventory().firstEmpty() == -1)
                event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), item);
            else
                event.getPlayer().getInventory().addItem(item);
        }
        event.getItem().setAmount(event.getItem().getAmount() - 1);
        event.setCancelled(true);
    }

    private boolean isVoucher(ItemStack item){
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(RPGLootTables.getInstance(), "loot_voucher"), PersistentDataType.STRING);
    }

    private String getVoucherTableName(ItemStack item){
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(RPGLootTables.getInstance(), "loot_voucher"), PersistentDataType.STRING);
    }
}

package ca.rpgcraft.rpgloottables.listeners;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.util.VaultHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class RedeemBankVoucher implements Listener {

    @EventHandler
    public void onRedeemBankVoucher(PlayerInteractEvent event) {
        if(!event.hasItem()
        || !event.getHand().equals(EquipmentSlot.HAND)
        || (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR))) return;
        if(event.getItem() == null) return;
        if(!event.getItem().hasItemMeta()) return;

        VaultHandler vaultHandler = RPGLootTables.getInstance().getVaultHandler();

        if(!vaultHandler.isVoucher(event.getItem())) return;

        vaultHandler.redeemVoucher(event);
    }
}

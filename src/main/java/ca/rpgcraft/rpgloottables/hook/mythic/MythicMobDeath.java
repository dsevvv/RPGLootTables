package ca.rpgcraft.rpgloottables.hook.mythic;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.util.CustomLootTable;
import ca.rpgcraft.rpgloottables.util.TableList;
import ca.rpgcraft.rpgloottables.util.VanillaLootTable;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.loot.LootContext;

import java.util.Random;
import java.util.logging.Logger;

public class MythicMobDeath implements Listener {

    private Logger log = RPGLootTables.getInstance().getLogger();

    private static final boolean DEBUG = true;

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event){

        MythicMob mythicMob = event.getMobType();

        if(DEBUG){
            log.info("Calling MythicMobDeathEvent");
            log.info("MythicMob: " + mythicMob.getDisplayName());
        }

        String key = String.valueOf(mythicMob.getDisplayName());

        if(TableList.getLoadedVanillaTables().containsKey(key)){
            VanillaLootTable vlt = TableList.getLoadedVanillaTables().get(key);
            if(!vlt.isKeepVanillaLoot()){
                if(DEBUG)
                    log.info("Removing default drops");
                //if we are removing vanilla drops, clear drops
                event.getDrops().clear();
            }
            for(CustomLootTable clt : vlt.getAssociatedTableList()){
                if(DEBUG)
                    log.info("Rolling for table: " + clt.getName());
                event.getDrops().addAll(clt.populateLoot(new Random(), new LootContext.Builder(event.getEntity().getLocation()).build()));
            }
        }
    }
}

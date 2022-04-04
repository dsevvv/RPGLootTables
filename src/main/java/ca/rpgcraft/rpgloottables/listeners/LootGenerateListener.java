package ca.rpgcraft.rpgloottables.listeners;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;

public class LootGenerateListener implements Listener {

    private final RPGLootTables plugin;

    public LootGenerateListener(RPGLootTables plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLootGenerate(LootGenerateEvent e){
        if(e.getLootTable().toString().equalsIgnoreCase("minecraft:chests/nether_bridge")){
            if(!plugin.getConfig().getBoolean("config.path")){
                e.getLoot().clear();
            }

        }
    }
}

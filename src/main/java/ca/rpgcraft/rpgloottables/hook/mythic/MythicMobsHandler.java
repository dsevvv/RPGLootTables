package ca.rpgcraft.rpgloottables.hook.mythic;

import io.lumine.mythic.api.MythicPlugin;
import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.core.mobs.ActiveMob;

import java.util.Collection;

public class MythicMobsHandler {

    private MythicPlugin mythic;

    public MythicMobsHandler() {
        this.mythic = MythicProvider.get();
    }

    private void test(){
    }

    public Collection<ActiveMob> getActiveMobs(){
        return mythic.getMobManager().getActiveMobs();
    }

    public Collection<MythicMob> getMythicMobs(){
        return mythic.getMobManager().getMobTypes();
    }

    public MythicPlugin getMythic(){
        return mythic;
    }
}

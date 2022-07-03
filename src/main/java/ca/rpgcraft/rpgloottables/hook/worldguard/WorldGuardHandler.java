package ca.rpgcraft.rpgloottables.hook.worldguard;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.command.argument.WorldConverter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.config.WorldConfiguration;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.Associables;
import com.sk89q.worldguard.protection.association.DelayedRegionOverlapAssociation;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class WorldGuardHandler {

    private static final boolean DEBUG = false;
    private final RPGLootTables plugin;
    private final WorldGuard worldGuard;
    private final Logger log;
    private RegionContainer regionContainer;

    public WorldGuardHandler() {
        this.plugin = RPGLootTables.getInstance();
        this.log = plugin.getLogger();
        this.worldGuard = WorldGuard.getInstance();
        this.regionContainer = worldGuard.getPlatform().getRegionContainer();
    }

    public List<ProtectedRegion> getRegions() {
        List<RegionManager> regionManagers = regionContainer.getLoaded();
        List<ProtectedRegion> regions = new LinkedList<>();

        for(RegionManager regionManager : regionManagers) {
            Collection<ProtectedRegion> regionsInManager = regionManager.getRegions().values();
            regions.addAll(regionsInManager);
        }

        return regions;
    }

    public RegionManager getRegionManager(String worldName){
        return regionContainer.get(WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName(worldName));
    }

    public ApplicableRegionSet getRegionsStandingIn(Entity entity){
        RegionQuery regionQuery = regionContainer.createQuery();
        return regionQuery.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
    }

    public static RegionAssociable createRegionAssociable(Object cause) {
        if (cause instanceof org.bukkit.entity.Player) {
            return WorldGuardPlugin.inst().wrapPlayer((org.bukkit.entity.Player) cause);
        } else if (cause instanceof Entity entity) {
            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            WorldConfiguration config = WorldGuard.getInstance().getPlatform().getGlobalStateManager().get(BukkitAdapter.adapt(entity.getWorld()));
            org.bukkit.Location loc = entity.getLocation(); // getOrigin() can be used on Paper if present
            return new DelayedRegionOverlapAssociation(query, BukkitAdapter.adapt(loc), config.useMaxPriorityAssociation);
        } else if (cause instanceof Block block) {
            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            WorldConfiguration config = WorldGuard.getInstance().getPlatform().getGlobalStateManager().get(BukkitAdapter.adapt(block.getWorld()));
            org.bukkit.Location loc = block.getLocation();
            return new DelayedRegionOverlapAssociation(query, BukkitAdapter.adapt(loc), config.useMaxPriorityAssociation);
        } else {
            return Associables.constant(Association.NON_MEMBER);
        }
    }
}

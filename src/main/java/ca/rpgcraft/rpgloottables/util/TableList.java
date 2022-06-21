package ca.rpgcraft.rpgloottables.util;

import java.util.HashMap;

/**
 * Storage class for loaded VanillaTable objects and CustomTable objects.
 * Also holds all the raw names of the vanilla tables.
 */
public class TableList {
    private static final HashMap<String, CustomLootTable> loadedCustomTables = new HashMap<>();
    private static final HashMap<String, VanillaLootTable> loadedVanillaTables = new HashMap<>();

    private static final String[] gameplayLootTables = {
            "minecraft:gameplay/piglin_bartering"
//            "minecraft:gameplay/cat_morning_gift",
//            "minecraft:gameplay/piglin_bartering",
//            "minecraft:gameplay/fishing/fish",
//            "minecraft:gameplay/fishing/junk",
//            "minecraft:gameplay/fishing/treasure",
//            "minecraft:gameplay/hero_of_the_village/armorer_gift",
//            "minecraft:gameplay/hero_of_the_village/butcher_gift",
//            "minecraft:gameplay/hero_of_the_village/cartographer_gift",
//            "minecraft:gameplay/hero_of_the_village/cleric_gift",
//            "minecraft:gameplay/hero_of_the_village/farmer_gift",
//            "minecraft:gameplay/hero_of_the_village/fisherman_gift",
//            "minecraft:gameplay/hero_of_the_village/fletcher_gift",
//            "minecraft:gameplay/hero_of_the_village/leatherworker_gift",
//            "minecraft:gameplay/hero_of_the_village/librarian_gift",
//            "minecraft:gameplay/hero_of_the_village/mason_gift",
//            "minecraft:gameplay/hero_of_the_village/shepherd_gift",
//            "minecraft:gameplay/hero_of_the_village/toolsmith_gift",
//            "minecraft:gameplay/hero_of_the_village/weaponsmith_gift"
    };

    private static final String[] mobLootTables = {
            "minecraft:entities/allay",
            "minecraft:entities/armor_stand",
            "minecraft:entities/axolotl",
            "minecraft:entities/bat",
            "minecraft:entities/bee",
            "minecraft:entities/blaze",
            "minecraft:entities/cat",
            "minecraft:entities/cave_spider",
            "minecraft:entities/chicken",
            "minecraft:entities/cod",
            "minecraft:entities/cow",
            "minecraft:entities/creeper",
            "minecraft:entities/dolphin",
            "minecraft:entities/donkey",
            "minecraft:entities/drowned",
            "minecraft:entities/elder_guardian",
            "minecraft:entities/ender_dragon",
            "minecraft:entities/enderman",
            "minecraft:entities/endermite",
            "minecraft:entities/evoker",
            "minecraft:entities/fox",
            "minecraft:entities/frog",
            "minecraft:entities/ghast",
            "minecraft:entities/giant",
            "minecraft:entities/glow_squid",
            "minecraft:entities/goat",
            "minecraft:entities/guardian",
            "minecraft:entities/hoglin",
            "minecraft:entities/horse",
            "minecraft:entities/husk",
            "minecraft:entities/illusioner",
            "minecraft:entities/iron_golem",
            "minecraft:entities/llama",
            "minecraft:entities/magma_cube",
            "minecraft:entities/mooshroom",
            "minecraft:entities/mule",
            "minecraft:entities/ocelot",
            "minecraft:entities/panda",
            "minecraft:entities/parrot",
            "minecraft:entities/phantom",
            "minecraft:entities/pig",
            "minecraft:entities/piglin",
            "minecraft:entities/piglin_brute",
            "minecraft:entities/pillager",
            "minecraft:entities/player",
            "minecraft:entities/polar_bear",
            "minecraft:entities/pufferfish",
            "minecraft:entities/rabbit",
            "minecraft:entities/ravager",
            "minecraft:entities/salmon",
            "minecraft:entities/sheep",
            "minecraft:entities/shulker",
            "minecraft:entities/silverfish",
            "minecraft:entities/skeleton",
            "minecraft:entities/skeleton_horse",
            "minecraft:entities/slime",
            "minecraft:entities/snow_golem",
            "minecraft:entities/spider",
            "minecraft:entities/squid",
            "minecraft:entities/stray",
            "minecraft:entities/strider",
            "minecraft:entities/tadpole",
            "minecraft:entities/trader_llama",
            "minecraft:entities/tropical_fish",
            "minecraft:entities/turtle",
            "minecraft:entities/vex",
            "minecraft:entities/villager",
            "minecraft:entities/vindicator",
            "minecraft:entities/wandering_trader",
            "minecraft:entities/warden",
            "minecraft:entities/witch",
            "minecraft:entities/wither",
            "minecraft:entities/wither_skeleton",
            "minecraft:entities/wolf",
            "minecraft:entities/zoglin",
            "minecraft:entities/zombie",
            "minecraft:entities/zombie_horse",
            "minecraft:entities/zombie_villager",
            "minecraft:entities/zombified_piglin"
    };

    private static final String[] chestLootTables = {
            "minecraft:chests/abandoned_mineshaft",
            "minecraft:chests/ancient_city",
            "minecraft:chests/ancient_city_ice_box",
            "minecraft:chests/bastion_bridge",
            "minecraft:chests/bastion_hoglin_stable",
            "minecraft:chests/bastion_other",
            "minecraft:chests/bastion_treasure",
            "minecraft:chests/buried_treasure",
            "minecraft:chests/desert_pyramid",
            "minecraft:chests/end_city_treasure",
            "minecraft:chests/igloo_chest",
            "minecraft:chests/jungle_temple",
            "minecraft:chests/jungle_temple_dispenser",
            "minecraft:chests/nether_bridge",
            "minecraft:chests/pillager_outpost",
            "minecraft:chests/ruined_portal",
            "minecraft:chests/shipwreck_map",
            "minecraft:chests/shipwreck_supply",
            "minecraft:chests/shipwreck_treasure",
            "minecraft:chests/simple_dungeon",
            "minecraft:chests/spawn_bonus_chest",
            "minecraft:chests/stronghold_corridor",
            "minecraft:chests/stronghold_crossing",
            "minecraft:chests/stronghold_library",
            "minecraft:chests/underwater_ruin_big",
            "minecraft:chests/underwater_ruin_small",
            "minecraft:chests/woodland_mansion",
            "minecraft:chests/village/village_armorer",
            "minecraft:chests/village/village_butcher",
            "minecraft:chests/village/village_cartographer",
            "minecraft:chests/village/village_desert_house",
            "minecraft:chests/village/village_fisher",
            "minecraft:chests/village/village_fletcher",
            "minecraft:chests/village/village_mason",
            "minecraft:chests/village/village_plains_house",
            "minecraft:chests/village/village_savanna_house",
            "minecraft:chests/village/village_shepherd",
            "minecraft:chests/village/village_snowy_house",
            "minecraft:chests/village/village_taiga_house",
            "minecraft:chests/village/village_tannery",
            "minecraft:chests/village/village_temple",
            "minecraft:chests/village/village_toolsmith",
            "minecraft:chests/village/village_weaponsmith"
    };

    public static String[] getChestLootTables() {
        return chestLootTables;
    }

    public static String[] getMobLootTables() {
        return mobLootTables;
    }

    public static String[] getGameplayLootTables() {
        return gameplayLootTables;
    }

    public static HashMap<String, CustomLootTable> getLoadedCustomTables() {
        return loadedCustomTables;
    }

    public static HashMap<String, VanillaLootTable> getLoadedVanillaTables(){ return loadedVanillaTables; }
}

package io.github.ultreon.mods.essentials.shop;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;

public final class ShopLoader {
    private static final float SMELTED = 0.50f;
    private static final float BAKED = 1.00f;
    private static final float ENCHANT = 20.00f;

    private static final float GLOWSTONE_DUST = 2.00f;
    private static final float GLOWSTONE = GLOWSTONE_DUST * 4;
    private static final float OAK_LOG = 4.00f;
    private static final float OAK_PLANKS = OAK_LOG / 4;
    private static final float BIRCH_LOG = 4.00f;
    private static final float BIRCH_PLANKS = BIRCH_LOG / 4;
    private static final float SPRUCE_LOG = 4.00f;
    private static final float SPRUCE_PLANKS = SPRUCE_LOG / 4;
    private static final float ACACIA_LOG = 5.00f;
    private static final float ACACIA_PLANKS = ACACIA_LOG / 4;
    private static final float JUNGLE_LOG = 5.00f;
    private static final float JUNGLE_PLANKS = ACACIA_LOG / 4;
    private static final float DARK_OAK_LOG = 5.00f;
    private static final float DARK_OAK_PLANKS = ACACIA_LOG / 4;
    private static final float STICK = OAK_PLANKS / 2;
    private static final float BOWL = OAK_PLANKS * 3 / 4;
    private static final float LEATHER = 6.00f;
    private static final float COBBLESTONE = 1.00f;
    private static final float BLACKSTONE = 4.00f;
    private static final float COBBLED_DEEPSLATE = 3.00f;
    private static final float DEEPSLATE = COBBLED_DEEPSLATE + SMELTED;
    private static final float ALT_STONE = 1.50f;
    private static final float IRON_ORE = 16.00f;
    private static final float RAW_IRON = 16.00f;
    private static final float IRON_INGOT = 18.00f;
    private static final float IRON_NUGGET = IRON_INGOT / 9;
    private static final float GOLD_ORE = 32.00f;
    private static final float RAW_GOLD = 32.00f;
    private static final float GOLD_INGOT = 45.00f;
    private static final float GOLD_NUGGET = GOLD_INGOT / 9;
    private static final float DIAMOND_ORE = 71.00f;
    private static final float DIAMOND = 81.00f;
    private static final float NETHERITE_SCRAP = 100.00f;
    private static final float NETHERITE_INGOT = NETHERITE_SCRAP * 4 + GOLD_INGOT * 4;
    private static final float COAL = 4.50f;
    private static final float REDSTONE = 10.00f;
    private static final float LAPIS_LAZULI = 9.00f;
    private static final float EMERALD = 12.00f;
    private static final float QUARTZ = 8.00f;
    private static final float EGG = 3.00f;
    private static final float MILK = 5.00f;
    private static final float BUCKET = IRON_INGOT * 3;
    private static final float MILK_BUCKET = BUCKET + MILK;
    private static final float CARROT = 4.00f;
    private static final float POTATO = 4.00f;
    private static final float BEETROOT = 4.50f;
    private static final float PUMPKIN = 8.00f;
    private static final float MELON = 9.00f;
    private static final float MELON_SLICE = MELON / 9f;
    private static final float BAKED_POTATO = POTATO + BAKED;
    private static final float APPLE = 5.00f;
    private static final float BROWN_MUSHROOM = 2.00f;
    private static final float RED_MUSHROOM = 2.50f;
    private static final float RABBIT = 2.50f;
    private static final float CHICKEN = 6.00f;
    private static final float BEEF = 7.50f;
    private static final float MUTTON = 7.00f;
    private static final float PORKCHOP = 8.00f;
    private static final float WHEAT = 3.00f;
    private static final float SUGAR_CANE = 2.00f;
    private static final float SUGAR = SUGAR_CANE;
    private static final float GRAVEL = 0.25f;
    private static final float SAND = 0.25f;
    private static final float CLAY = 0.40f;
    private static final float GUNPOWDER = 8.00f;
    private static final float SPIDER_EYE = 4.00f;
    private static final float ROTTEN_FLESH = 0.05f;
    private static final float PHANTOM_MEMBRANE = 18.00f;
    private static final float BLAZE_ROD = 45.00f;
    private static final float GHAST_TEAR = 40.00f;
    private static final float ENDER_PEARL = 43.00f;
    private static final float STRING = 5.00f;
    private static final float SLIME_BALL = 4.00f;
    private static final float REDSTONE_TORCH = REDSTONE + STICK;
    private static final float NETHER_STAR = 8_800.00f;
    private static final float OBSIDIAN = 120.0f;
    private static final float SPAWN_EGG = 80_000;
    private static final Logger LOGGER = LogManager.getLogger("VertX ShopLoader");
    private static final String MODID_TIC = "tconstruct";

    public static void load(ServerShop shop) {
        long start = System.currentTimeMillis();
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_LOG, 1), OAK_LOG));
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_PLANKS, 1), OAK_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_SLAB, 2), OAK_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_STAIRS, 1), OAK_PLANKS * 0.75f));
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_BOAT, 1), OAK_PLANKS * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_DOOR, 1), OAK_PLANKS * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_TRAPDOOR, 1), OAK_PLANKS * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_BUTTON, 1), OAK_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.OAK_PRESSURE_PLATE, 1), OAK_PLANKS * 2));

        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_LOG, 1), BIRCH_LOG));
        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_PLANKS, 1), BIRCH_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_SLAB, 2), BIRCH_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_STAIRS, 1), BIRCH_PLANKS * 0.75f));
        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_BOAT, 1), BIRCH_PLANKS * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_DOOR, 1), BIRCH_PLANKS * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_TRAPDOOR, 1), BIRCH_PLANKS * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_BUTTON, 1), BIRCH_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.BIRCH_PRESSURE_PLATE, 1), BIRCH_PLANKS * 2));

        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_LOG, 1), SPRUCE_LOG));
        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_PLANKS, 1), SPRUCE_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_SLAB, 2), SPRUCE_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_STAIRS, 1), SPRUCE_PLANKS * 0.75f));
        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_BOAT, 1), SPRUCE_PLANKS * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_DOOR, 1), SPRUCE_PLANKS * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_TRAPDOOR, 1), SPRUCE_PLANKS * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_BUTTON, 1), SPRUCE_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.SPRUCE_PRESSURE_PLATE, 1), SPRUCE_PLANKS * 2));

        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_LOG, 1), ACACIA_LOG));
        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_PLANKS, 1), ACACIA_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_SLAB, 2), ACACIA_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_STAIRS, 1), ACACIA_PLANKS * 0.75f + 0.0125f));
        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_BOAT, 1), ACACIA_PLANKS * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_DOOR, 1), ACACIA_PLANKS * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_TRAPDOOR, 1), ACACIA_PLANKS * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_BUTTON, 1), ACACIA_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.ACACIA_PRESSURE_PLATE, 1), ACACIA_PLANKS * 2));

        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_LOG, 1), JUNGLE_LOG));
        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_PLANKS, 1), JUNGLE_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_SLAB, 2), JUNGLE_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_STAIRS, 1), JUNGLE_PLANKS * 0.75f + 0.01f));
        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_BOAT, 1), JUNGLE_PLANKS * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_DOOR, 1), JUNGLE_PLANKS * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_TRAPDOOR, 1), JUNGLE_PLANKS * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_BUTTON, 1), JUNGLE_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.JUNGLE_PRESSURE_PLATE, 1), JUNGLE_PLANKS * 2));

        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_LOG, 1), DARK_OAK_LOG));
        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_PLANKS, 1), DARK_OAK_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_SLAB, 2), DARK_OAK_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_STAIRS, 1), DARK_OAK_PLANKS * 0.75f + 0.01f));
        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_BOAT, 1), DARK_OAK_PLANKS * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_DOOR, 1), DARK_OAK_PLANKS * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_TRAPDOOR, 1), DARK_OAK_PLANKS * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_BUTTON, 1), DARK_OAK_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.DARK_OAK_PRESSURE_PLATE, 1), DARK_OAK_PLANKS * 2));

        shop.addItem(new ShopItem(new ItemStack(Items.WOODEN_SWORD, 1), OAK_PLANKS * 2 + STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.WOODEN_AXE, 1), OAK_PLANKS * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.WOODEN_PICKAXE, 1), OAK_PLANKS * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.WOODEN_SHOVEL, 1), OAK_PLANKS + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.WOODEN_HOE, 1), OAK_PLANKS * 2 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.LEATHER_HELMET, 1), LEATHER * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.LEATHER_CHESTPLATE, 1), LEATHER * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.LEATHER_LEGGINGS, 1), LEATHER * 7));
        shop.addItem(new ShopItem(new ItemStack(Items.LEATHER_BOOTS, 1), LEATHER * 4));

        shop.addItem(new ShopItem(new ItemStack(Items.STONE_SWORD, 1), COBBLESTONE * 2 + STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.STONE_AXE, 1), COBBLESTONE * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.STONE_PICKAXE, 1), COBBLESTONE * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.STONE_SHOVEL, 1), COBBLESTONE + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.STONE_HOE, 1), COBBLESTONE * 2 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.CHAINMAIL_HELMET, 1), IRON_NUGGET * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), IRON_NUGGET * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), IRON_NUGGET * 7));
        shop.addItem(new ShopItem(new ItemStack(Items.CHAINMAIL_BOOTS, 1), IRON_NUGGET * 4));

        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_SWORD, 1), GOLD_INGOT * 2 + STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_AXE, 1), GOLD_INGOT * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_PICKAXE, 1), GOLD_INGOT * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_SHOVEL, 1), GOLD_INGOT + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_HOE, 1), GOLD_INGOT * 2 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_HELMET, 1), GOLD_INGOT * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_CHESTPLATE, 1), GOLD_INGOT * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_LEGGINGS, 1), GOLD_INGOT * 7));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_BOOTS, 1), GOLD_INGOT * 4));

        shop.addItem(new ShopItem(new ItemStack(Items.IRON_SWORD, 1), IRON_INGOT * 2 + STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_AXE, 1), IRON_INGOT * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_PICKAXE, 1), IRON_INGOT * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_SHOVEL, 1), IRON_INGOT + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_HOE, 1), IRON_INGOT * 2 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_HELMET, 1), IRON_INGOT * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_CHESTPLATE, 1), IRON_INGOT * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_LEGGINGS, 1), IRON_INGOT * 7));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_BOOTS, 1), IRON_INGOT * 4));

        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_SWORD, 1), DIAMOND * 2 + STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_AXE, 1), DIAMOND * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_PICKAXE, 1), DIAMOND * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_SHOVEL, 1), DIAMOND + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_HOE, 1), DIAMOND * 2 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_HELMET, 1), DIAMOND * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_CHESTPLATE, 1), DIAMOND * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_LEGGINGS, 1), DIAMOND * 7));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_BOOTS, 1), DIAMOND * 4));

        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_SWORD, 1), NETHERITE_INGOT + DIAMOND * 2 + STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_AXE, 1), NETHERITE_INGOT + DIAMOND * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_PICKAXE, 1), NETHERITE_INGOT + DIAMOND * 3 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_SHOVEL, 1), NETHERITE_INGOT + DIAMOND + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_HOE, 1), NETHERITE_INGOT + DIAMOND * 2 + STICK * 2));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_HELMET, 1), NETHERITE_INGOT + DIAMOND * 5));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_CHESTPLATE, 1), NETHERITE_INGOT + DIAMOND * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_LEGGINGS, 1), NETHERITE_INGOT + DIAMOND * 7));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_BOOTS, 1), NETHERITE_INGOT + DIAMOND * 4));

        shop.addItem(new ShopItem(new ItemStack(Items.STICK, 1), STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.CRAFTING_TABLE, 1), OAK_PLANKS * 4));
        shop.addItem(new ShopItem(new ItemStack(Items.FURNACE, 1), COBBLESTONE * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.LEATHER, 1), 2.00f));
        shop.addItem(new ShopItem(new ItemStack(Items.COAL, 1), COAL));
        shop.addItem(new ShopItem(new ItemStack(Items.CHARCOAL, 1), COAL));
        shop.addItem(new ShopItem(new ItemStack(Items.DIRT, 16), 0.13f));
        shop.addItem(new ShopItem(new ItemStack(Items.DIRT, 32), 0.25f));
        shop.addItem(new ShopItem(new ItemStack(Items.DIRT, 64), 0.50f));

        shop.addItem(new ShopItem(new ItemStack(Items.COBBLESTONE, 1), COBBLESTONE));
        shop.addItem(new ShopItem(new ItemStack(Items.COBBLESTONE_SLAB, 1), COBBLESTONE / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.STONE, 1), COBBLESTONE + SMELTED));
        shop.addItem(new ShopItem(new ItemStack(Items.STONE_SLAB, 1), (COBBLESTONE + SMELTED) / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.STONE_BRICKS, 1), COBBLESTONE + SMELTED));
        shop.addItem(new ShopItem(new ItemStack(Items.STONE_BRICK_SLAB, 1), (COBBLESTONE + SMELTED) / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.ANDESITE, 1), ALT_STONE));
        shop.addItem(new ShopItem(new ItemStack(Items.DIORITE, 1), ALT_STONE));
        shop.addItem(new ShopItem(new ItemStack(Items.GRANITE, 1), ALT_STONE));

        shop.addItem(new ShopItem(new ItemStack(Items.DEEPSLATE, 1), DEEPSLATE));
        shop.addItem(new ShopItem(new ItemStack(Items.COBBLED_DEEPSLATE, 1), COBBLED_DEEPSLATE));
        shop.addItem(new ShopItem(new ItemStack(Items.COBBLED_DEEPSLATE_SLAB, 1), COBBLED_DEEPSLATE / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.POLISHED_DEEPSLATE, 1), DEEPSLATE));
        shop.addItem(new ShopItem(new ItemStack(Items.POLISHED_DEEPSLATE_SLAB, 1), DEEPSLATE / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.DEEPSLATE_BRICKS, 1), DEEPSLATE));
        shop.addItem(new ShopItem(new ItemStack(Items.DEEPSLATE_BRICK_SLAB, 1), DEEPSLATE / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.DEEPSLATE_TILES, 1), DEEPSLATE));
        shop.addItem(new ShopItem(new ItemStack(Items.DEEPSLATE_TILE_SLAB, 1), DEEPSLATE / 2));

        shop.addItem(new ShopItem(new ItemStack(Items.CHISELED_DEEPSLATE, 1), DEEPSLATE));
        shop.addItem(new ShopItem(new ItemStack(Items.BLACKSTONE, 1), BLACKSTONE));
        shop.addItem(new ShopItem(new ItemStack(Items.BLACKSTONE_SLAB, 1), BLACKSTONE / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.POLISHED_BLACKSTONE_BRICKS, 1), BLACKSTONE));
        shop.addItem(new ShopItem(new ItemStack(Items.POLISHED_BLACKSTONE_BRICK_SLAB, 1), BLACKSTONE / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.POLISHED_BLACKSTONE, 1), BLACKSTONE));
        shop.addItem(new ShopItem(new ItemStack(Items.POLISHED_BLACKSTONE_SLAB, 1), BLACKSTONE / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.CHISELED_POLISHED_BLACKSTONE, 1), BLACKSTONE));
        shop.addItem(new ShopItem(new ItemStack(Items.GILDED_BLACKSTONE, 1), BLACKSTONE * GOLD_NUGGET * 4));

        shop.addItem(new ShopItem(new ItemStack(Items.BLACKSTONE_STAIRS, 1), BLACKSTONE * 0.75f));
        shop.addItem(new ShopItem(new ItemStack(Items.POLISHED_BLACKSTONE_BRICK_STAIRS, 1), BLACKSTONE * 0.75f));
        shop.addItem(new ShopItem(new ItemStack(Items.POLISHED_BLACKSTONE_STAIRS, 1), BLACKSTONE * 0.75f));
        shop.addItem(new ShopItem(new ItemStack(Items.CARROT, 1), CARROT));
        shop.addItem(new ShopItem(new ItemStack(Items.POTATO, 1), POTATO));
        shop.addItem(new ShopItem(new ItemStack(Items.BEETROOT, 1), BEETROOT));
        shop.addItem(new ShopItem(new ItemStack(Items.MELON_SLICE, 1), MELON_SLICE));
        shop.addItem(new ShopItem(new ItemStack(Items.MELON, 1), MELON));
        shop.addItem(new ShopItem(new ItemStack(Items.PUMPKIN, 1), PUMPKIN));

        shop.addItem(new ShopItem(new ItemStack(Items.RABBIT, 1), RABBIT));
        shop.addItem(new ShopItem(new ItemStack(Items.CHICKEN, 1), CHICKEN));
        shop.addItem(new ShopItem(new ItemStack(Items.BEEF, 1), BEEF));
        shop.addItem(new ShopItem(new ItemStack(Items.MUTTON, 1), MUTTON));
        shop.addItem(new ShopItem(new ItemStack(Items.PORKCHOP, 1), PORKCHOP));
        shop.addItem(new ShopItem(new ItemStack(Items.BAKED_POTATO, 1), BAKED_POTATO));
        shop.addItem(new ShopItem(new ItemStack(Items.RABBIT_STEW, 1), BAKED_POTATO + CARROT + RED_MUSHROOM + RABBIT + BOWL));
        shop.addItem(new ShopItem(new ItemStack(Items.MUSHROOM_STEW, 1), RED_MUSHROOM + BROWN_MUSHROOM + BOWL));
        shop.addItem(new ShopItem(new ItemStack(Items.BEETROOT_SOUP, 1), BEETROOT + BOWL));

        shop.addItem(new ShopItem(new ItemStack(Items.COOKED_RABBIT, 1), RABBIT + BAKED));
        shop.addItem(new ShopItem(new ItemStack(Items.COOKED_CHICKEN, 1), CHICKEN + BAKED));
        shop.addItem(new ShopItem(new ItemStack(Items.COOKED_BEEF, 1), BEEF + BAKED));
        shop.addItem(new ShopItem(new ItemStack(Items.COOKED_MUTTON, 1), MUTTON + BAKED));
        shop.addItem(new ShopItem(new ItemStack(Items.COOKED_PORKCHOP, 1), PORKCHOP * BAKED));
        shop.addItem(new ShopItem(new ItemStack(Items.CAKE, 1), MILK * 3 + SUGAR * 2 + EGG + WHEAT));
        shop.addItem(new ShopItem(new ItemStack(Items.PUMPKIN_PIE, 1), PUMPKIN + SUGAR + EGG));
        shop.addItem(new ShopItem(new ItemStack(Items.BROWN_MUSHROOM, 1), BROWN_MUSHROOM));
        shop.addItem(new ShopItem(new ItemStack(Items.RED_MUSHROOM, 1), RED_MUSHROOM));

        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_CARROT, 1), GOLD_NUGGET * 8 + CARROT));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLDEN_APPLE, 1), GOLD_INGOT * 8 + APPLE));
        shop.addItem(new ShopItem(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1), GOLD_INGOT * 9 * 8 + APPLE + ENCHANT));
        shop.addItem(new ShopItem(new ItemStack(Items.EGG, 1), EGG));
        shop.addItem(new ShopItem(new ItemStack(Items.MILK_BUCKET, 1), MILK_BUCKET));
        shop.addItem(new ShopItem(new ItemStack(Items.BREAD, 1), WHEAT * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.WHEAT, 1), WHEAT));
        shop.addItem(new ShopItem(new ItemStack(Items.SUGAR, 1), SUGAR));
        shop.addItem(new ShopItem(new ItemStack(Items.BOWL, 1), BOWL));

        shop.addItem(new ShopItem(new ItemStack(Items.GRAVEL, 8), GRAVEL));
        shop.addItem(new ShopItem(new ItemStack(Items.SAND, 6), SAND));
        shop.addItem(new ShopItem(new ItemStack(Items.CLAY, 6), CLAY));
        shop.addItem(new ShopItem(new ItemStack(Items.GUNPOWDER, 1), GUNPOWDER));
        shop.addItem(new ShopItem(new ItemStack(Items.SPIDER_EYE, 1), SPIDER_EYE));
        shop.addItem(new ShopItem(new ItemStack(Items.ROTTEN_FLESH, 8), ROTTEN_FLESH));
        shop.addItem(new ShopItem(new ItemStack(Items.PHANTOM_MEMBRANE, 1), PHANTOM_MEMBRANE));
        shop.addItem(new ShopItem(new ItemStack(Items.BLAZE_ROD, 1), BLAZE_ROD));
        shop.addItem(new ShopItem(new ItemStack(Items.GHAST_TEAR, 1), GHAST_TEAR));

        shop.addItem(new ShopItem(new ItemStack(Items.BLAZE_POWDER, 1), BLAZE_ROD / 2));
        shop.addItem(new ShopItem(new ItemStack(Items.ENDER_PEARL, 1), ENDER_PEARL));
        shop.addItem(new ShopItem(new ItemStack(Items.CREEPER_HEAD, 1), GUNPOWDER * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.ZOMBIE_HEAD, 1), GUNPOWDER * 6));
        shop.addItem(new ShopItem(new ItemStack(Items.SKELETON_SKULL, 1), GUNPOWDER * 6));
        shop.addItem(new ShopItem(new ItemStack(Items.WITHER_SKELETON_SKULL, 1), 780.00f));
        shop.addItem(new ShopItem(new ItemStack(Items.DRAGON_HEAD, 1), 1560.00f));
        shop.addItem(new ShopItem(new ItemStack(Items.DRAGON_BREATH, 1), 1400.00f));
        shop.addItem(new ShopItem(new ItemStack(Items.DRAGON_EGG, 1), 4800.00f));

        shop.addItem(new ShopItem(new ItemStack(Items.STRING, 1), STRING));
        shop.addItem(new ShopItem(new ItemStack(Items.SLIME_BALL, 1), SLIME_BALL));
        shop.addItem(new ShopItem(new ItemStack(Items.BOW, 1), STRING * 3 + STICK * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.TRIPWIRE_HOOK, 1), STICK + IRON_INGOT + OAK_PLANKS));
        shop.addItem(new ShopItem(new ItemStack(Items.REDSTONE_TORCH, 1), REDSTONE + STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.DISPENSER, 1), (STRING * 3 + STICK * 3) + REDSTONE + COBBLESTONE * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.DROPPER, 1), REDSTONE + COBBLESTONE * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.PISTON, 1), OAK_PLANKS * 3 + COBBLESTONE * 4 + IRON_INGOT + REDSTONE));
        shop.addItem(new ShopItem(new ItemStack(Items.STICKY_PISTON, 1), OAK_PLANKS * 3 + COBBLESTONE * 4 + IRON_INGOT + REDSTONE + SLIME_BALL));

        shop.addItem(new ShopItem(new ItemStack(Items.GLOWSTONE, 1), GLOWSTONE_DUST * 4));
        shop.addItem(new ShopItem(new ItemStack(Items.QUARTZ, 1), QUARTZ));
        shop.addItem(new ShopItem(new ItemStack(Items.GLASS, 1), SAND + SMELTED));
        shop.addItem(new ShopItem(new ItemStack(Items.CHEST, 1), OAK_PLANKS * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.DAYLIGHT_DETECTOR, 1), (SAND + SMELTED) * 3 + QUARTZ * 3 + OAK_PLANKS / 2 * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.HOPPER, 1), IRON_INGOT * 5 + OAK_PLANKS * 8));
        shop.addItem(new ShopItem(new ItemStack(Items.REPEATER, 1), REDSTONE_TORCH * 2 + REDSTONE + (COBBLESTONE + SMELTED) * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.COMPARATOR, 1), REDSTONE_TORCH * 3 + QUARTZ + (COBBLESTONE + SMELTED) * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.REDSTONE_LAMP, 1), GLOWSTONE_DUST * 4 + REDSTONE * 4));

        shop.addItem(new ShopItem(new ItemStack(Items.REDSTONE, 1), REDSTONE));
        shop.addItem(new ShopItem(new ItemStack(Items.LAPIS_LAZULI, 1), LAPIS_LAZULI));
        shop.addItem(new ShopItem(new ItemStack(Items.EMERALD, 1), EMERALD));
        shop.addItem(new ShopItem(new ItemStack(Items.SHIELD, 1), IRON_INGOT + OAK_PLANKS * 6));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_ORE, 1), IRON_ORE));
        shop.addItem(new ShopItem(new ItemStack(Items.RAW_IRON, 1), RAW_IRON));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_INGOT, 1), IRON_INGOT));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_NUGGET, 1), IRON_NUGGET));
        shop.addItem(new ShopItem(new ItemStack(Items.IRON_BLOCK, 1), IRON_INGOT * 9));

        shop.addItem(new ShopItem(new ItemStack(Items.GOLD_ORE, 1), GOLD_ORE));
        shop.addItem(new ShopItem(new ItemStack(Items.RAW_GOLD, 1), RAW_GOLD));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLD_INGOT, 1), GOLD_INGOT));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLD_NUGGET, 1), GOLD_NUGGET));
        shop.addItem(new ShopItem(new ItemStack(Items.GOLD_BLOCK, 1), GOLD_INGOT * 9));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_ORE, 1), DIAMOND_ORE));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND, 1), DIAMOND));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_BLOCK, 1), DIAMOND * 9));
        shop.addItem(new ShopItem(new ItemStack(Items.DIAMOND_HORSE_ARMOR, 1), DIAMOND * 6));

        shop.addItem(new ShopItem(new ItemStack(Items.ANCIENT_DEBRIS, 1), NETHERITE_SCRAP));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_SCRAP, 1), NETHERITE_SCRAP));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_INGOT, 1), NETHERITE_INGOT));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_BLOCK, 1), NETHERITE_INGOT * 9));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHERITE_BLOCK, 64), NETHERITE_INGOT * 9 * 64));
        shop.addItem(new ShopItem(new ItemStack(Items.CONDUIT, 1), 760.00f));
        shop.addItem(new ShopItem(new ItemStack(Items.NETHER_STAR, 1), NETHER_STAR));
        shop.addItem(new ShopItem(new ItemStack(Items.BEACON, 1), (SAND + SMELTED) * 5 + NETHER_STAR + OBSIDIAN * 3));
        shop.addItem(new ShopItem(new ItemStack(Items.END_CRYSTAL, 1), 1_235.00f));

        shop.addItem(new ShopItem(new ItemStack(Items.SKELETON_HORSE_SPAWN_EGG, 1), SPAWN_EGG + 64_386.5f));
        shop.addItem(new ShopItem(new ItemStack(Items.ZOMBIE_HORSE_SPAWN_EGG, 1), SPAWN_EGG + 68_569.7f));
        shop.addItem(new ShopItem(new ItemStack(Items.SKELETON_SPAWN_EGG, 1), SPAWN_EGG + 6_400f));
        shop.addItem(new ShopItem(new ItemStack(Items.ZOMBIE_SPAWN_EGG, 1), SPAWN_EGG + 6_400f));
        shop.addItem(new ShopItem(new ItemStack(Items.SPIDER_SPAWN_EGG, 1), SPAWN_EGG + 6_400f));
        shop.addItem(new ShopItem(new ItemStack(Items.HUSK_SPAWN_EGG, 1), SPAWN_EGG + 8_250f));
        shop.addItem(new ShopItem(new ItemStack(Items.STRAY_SPAWN_EGG, 1), SPAWN_EGG + 9_400f));
        shop.addItem(new ShopItem(new ItemStack(Items.CAVE_SPIDER_SPAWN_EGG, 1), SPAWN_EGG + 10_760.0f));
        shop.addItem(new ShopItem(new ItemStack(Items.CREEPER_SPAWN_EGG, 1), SPAWN_EGG + 12_800.0f));

        shop.addItem(new ShopItem(new ItemStack(Items.SPAWNER, 1), 128_700.0f));
        shop.addItem(new ShopItem(new ItemStack(Items.PETRIFIED_OAK_SLAB, 1), OAK_PLANKS / 2 * 123456789 / 666));
        shop.addItem(new ShopItem(new ItemStack(Items.TORCH, 1), COAL + STICK));
        shop.addItem(new ShopItem(new ItemStack(Items.LANTERN, 1), IRON_NUGGET * 8 + (COAL + STICK)));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));

        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));
        shop.addItem(new ShopItem(ItemStack.EMPTY, 0f));

        // Tinkers Construct.
        if (isModLoaded(MODID_TIC)) {
            LOGGER.info("Making shop compat for Tinkers Construct.");

            addItem(shop, tic("blaze_head"), 225.00f);
        }

        long end = System.nanoTime();
        long time = end - start;

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(5);
        df.setMinimumFractionDigits(0);

        System.out.println("Loading shop took " + df.format((double) time / 1_000_000_000d) + " (" + df.format((double)time / 1_000_000f) + " ms)");
    }

    private static void addItem(ServerShop shop, ResourceLocation key, float price) {
        addItem(shop, key, 1, price);
    }

    private static void addItem(ServerShop shop, ResourceLocation key, int count, float price) {
        shop.addItem(new ShopItem(new ItemStack(BuiltInRegistries.ITEM.get(key), count), price));
    }

    private static ResourceLocation tic(String path) {
        return new ResourceLocation(MODID_TIC, path);
    }

    private static boolean isModLoaded(String modId) {
        return Platform.isModLoaded(modId);
    }
}

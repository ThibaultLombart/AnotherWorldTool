package com.thibault_lombart.anotherWorldTool.utils;

import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.Set;

public final class BlockCategories {

    private BlockCategories() {}

    // HACHE : tout ce qui est bois + cacao
    public static boolean forAxe(Material m) {
        return Tag.LOGS.isTagged(m) || m == Material.COCOA;
    }

    // PIOCHE : stone-like + deepslate/basalt + tous minerais
    public static boolean forPickaxe(Material m) {
        return isStoneFamily(m) || ORES.contains(m);
    }

    private static boolean isStoneFamily(Material m) {
        return switch (m) {
            case STONE, COBBLESTONE, DEEPSLATE, COBBLED_DEEPSLATE, BASALT, SMOOTH_BASALT,
                 TUFF, TUFF_SLAB, TUFF_STAIRS, TUFF_WALL -> true; // élargissable
            default -> false;
        };
    }

    // HOE (houe) : toutes les cultures
    public static boolean forHoe(Material m) {
        // Tag.CROPS couvre les cultures de base (blé, carottes, etc.)
        // On ajoute les cas nether_wart + sugar_cane + sweet_berry_bush
        return Tag.CROPS.isTagged(m)
                || m == Material.NETHER_WART
                || m == Material.SUGAR_CANE
                || m == Material.SWEET_BERRY_BUSH
                || m == Material.BAMBOO; // à toi d’ajuster selon ton design
    }

    // CISAILLES : lianes + feuillages
    public static boolean forShears(Material m) {
        return Tag.LEAVES.isTagged(m) || m == Material.VINE || m == Material.GLOW_LICHEN;
    }

    // PELLE : grass + mycelium
    public static boolean forShovel(Material m) {
        return m == Material.GRASS_BLOCK || m == Material.MYCELIUM;
    }

    private static final Set<Material> ORES = Set.of(
            Material.COAL_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.COPPER_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.REDSTONE_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.LAPIS_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.NETHER_QUARTZ_ORE,
            Material.NETHER_GOLD_ORE
    );

}

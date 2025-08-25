package com.thibault_lombart.anotherWorldTool.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;

import java.util.EnumSet;
import java.util.Set;

public final class CropMaturity {

    private CropMaturity() {}

    // Matériaux pour lesquels on exige la maturité
    private static final Set<Material> REQUIRE_MATURE = EnumSet.of(
            // HOE / cultures
            Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS,
            Material.NETHER_WART, Material.SWEET_BERRY_BUSH,
            // AXE / cacao
            Material.COCOA
    );

    /** true si on doit vérifier la maturité pour ce matériau */
    public static boolean requiresMaturity(Material m) {
        return REQUIRE_MATURE.contains(m);
    }

    /** true si le bloc est considéré "pleinement poussé" */
    public static boolean isFullyGrown(Block block) {
        Material m = block.getType();

        // La plupart des plantes utilisent Ageable (0..maxAge)
        if (block.getBlockData() instanceof Ageable ageable) {
            return ageable.getAge() >= ageable.getMaximumAge();
        }

        return true;
    }

}

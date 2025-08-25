package com.thibault_lombart.anotherWorldTool.utils;

import com.thibault_lombart.anotherWorldTool.AnotherWorldTool;
import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class BlockCategories {

    private final AnotherWorldTool plugin;

    // XP par outil et par bloc : categories.get(AXE).get(OAK_LOG) => 1
    private final Map<ToolsEnum, Map<Material, Integer>> categories = new EnumMap<>(ToolsEnum.class);
    private int defaultXp = 1;

    public BlockCategories(AnotherWorldTool plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        categories.clear();
        defaultXp = plugin.getConfig().getInt("default_xp", 1);

        ConfigurationSection root = plugin.getConfig().getConfigurationSection("categories");
        if (root == null) {
            plugin.getLogger().warning("[BlockConfig] Section 'categories' absente du config.yml");
            return;
        }

        for (String toolKey : root.getKeys(false)) {
            ToolsEnum tool = ToolsEnum.fromId(toolKey);
            if (tool == null) {
                plugin.getLogger().warning("[BlockConfig] Outil inconnu: " + toolKey);
                continue;
            }

            ConfigurationSection toolSec = root.getConfigurationSection(toolKey);
            if (toolSec == null) continue;

            Map<Material, Integer> map = new EnumMap<>(Material.class);

            for (String materialKey : toolSec.getKeys(false)) {
                Material mat = parseMaterial(materialKey);
                if (mat == null) {
                    plugin.getLogger().warning("[BlockConfig] Matériau inconnu: " + materialKey + " pour " + tool);
                    continue;
                }
                int xp = toolSec.getInt(materialKey, defaultXp);
                map.put(mat, xp);
            }

            categories.put(tool, map);
        }

        plugin.getLogger().info("[BlockConfig] Chargé : default_xp=" + defaultXp +
                ", outils=" + categories.size());
    }

    private Material parseMaterial(String key) {
        try { return Material.valueOf(key.trim().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException e) { return null; }
    }

    // --- API ---

    /** true si le bloc est dans la catégorie de l'outil */
    public boolean matches(ToolsEnum tool, Material m) {
        Map<Material, Integer> map = categories.get(tool);
        return map != null && map.containsKey(m);
    }

    /** XP gagné pour ce bloc et cet outil ; si absent, retourne default_xp */
    public int xpFor(ToolsEnum tool, Material m) {
        Map<Material, Integer> map = categories.get(tool);
        if (map == null) return defaultXp;
        return map.getOrDefault(m, defaultXp);
    }


}

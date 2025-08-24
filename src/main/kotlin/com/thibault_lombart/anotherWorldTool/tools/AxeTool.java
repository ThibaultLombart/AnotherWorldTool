package com.thibault_lombart.anotherWorldTool.tools;

import com.thibault_lombart.anotherWorldTool.enums.EnchantType;
import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class AxeTool extends Tool {

    public AxeTool(UUID user) {
        super(user,"Hache", ToolsEnum.AXE);
    }

    public AxeTool(UUID user, int xp, int totalXp, int level, int points, String name, java.util.EnumMap<EnchantType, Integer> enchantLevels) {
        super(user,xp,totalXp,level,points,name, enchantLevels, ToolsEnum.AXE);
    }

    @Override
    public ItemStack getTool() {

        Material material = this.getMaterial();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        this.changeItemMeta(meta);

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public Material getMaterial() {
        int level = getLevel();
        if (level < 5) return Material.WOODEN_AXE;
        else if (level < 10) return Material.STONE_AXE;
        else if (level < 15) return Material.IRON_AXE;
        else if (level < 20) return Material.DIAMOND_AXE;
        else return Material.NETHERITE_AXE;
    }
}

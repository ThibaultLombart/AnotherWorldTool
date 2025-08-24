package com.thibault_lombart.anotherWorldTool.tools;

import com.thibault_lombart.anotherWorldTool.enums.EnchantType;
import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class ShearsTool extends Tool {

    public ShearsTool(UUID user) {
        super(user,"Cisailles", ToolsEnum.SHEARS);
    }

    public ShearsTool(UUID user, int xp, int totalXp, int level, int points, String name, java.util.EnumMap<EnchantType, Integer> enchantLevels) {
        super(user,xp,totalXp,level,points,name, enchantLevels, ToolsEnum.SHEARS);
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
        return Material.SHEARS;
    }
}

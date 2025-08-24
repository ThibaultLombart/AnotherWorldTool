package com.thibault_lombart.anotherWorldTool.utils;

import com.thibault_lombart.anotherWorldTool.tools.Tool;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LoreUtils {

    public static void updateXpLine(ItemStack item, Tool tool) {
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();

        if (lore == null) lore = new ArrayList<>();

        // Exemple : on considère que la ligne XP est la 2ème (index 1)
        // et la ligne Level est la 1ère (index 0).

        lore.set(1, Component.text("Level : " + tool.getLevel())
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false));

        lore.set(2, Component.text("XP : " + tool.getXP() + " / " + tool.getXpToNextLevel())
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false));

        meta.lore(lore);
        item.setItemMeta(meta);
    }


}

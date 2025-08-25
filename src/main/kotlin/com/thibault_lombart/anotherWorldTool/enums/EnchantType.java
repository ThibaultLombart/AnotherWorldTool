package com.thibault_lombart.anotherWorldTool.enums;

import com.thibault_lombart.anotherWorldTool.tools.Tool;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum EnchantType {

    EFFICIENCY("Efficacité", Enchantment.EFFICIENCY, true,
            1, 1, 255, Material.NETHER_STAR, EnumSet.of(ToolsEnum.AXE, ToolsEnum.PICKAXE, ToolsEnum.SHOVEL, ToolsEnum.SHEARS)),
    SILKTOUCH("Touché de soie", Enchantment.SILK_TOUCH, true,
            5, 2, 1, Material.QUARTZ, EnumSet.of(ToolsEnum.AXE, ToolsEnum.PICKAXE, ToolsEnum.SHOVEL));

    public final String displayName;
    public final Enchantment vanilla;   // null si custom
    public final boolean isVanilla;
    public final int minimumToolLevel;  // ex: niveau d’outil requis (ton système)
    public final int pointsCost;        // coût par niveau (optionnel)
    public final int maxLevel;
    public final Material materialOfEnchant;
    public final Set<ToolsEnum> allowedTools;


    EnchantType(String displayName, Enchantment vanilla, boolean isVanilla,
                int minimumToolLevel, int pointsCost, int maxLevel, Material materialOfEnchant, Set<ToolsEnum> allowedTools) {
        this.displayName = displayName;
        this.vanilla = vanilla;
        this.isVanilla = isVanilla;
        this.minimumToolLevel = minimumToolLevel;
        this.pointsCost = pointsCost;
        this.maxLevel = maxLevel;
        this.materialOfEnchant = materialOfEnchant;
        this.allowedTools = allowedTools;
    }

    public boolean allowedOn(ToolsEnum tool) {
        return allowedTools.contains(tool);
    }

    public boolean allowedOn(Tool tool) {
        return allowedTools.contains(tool.getToolsEnum());
    }

    public void apply(ItemMeta meta, int level, boolean unsafeVanilla) {
        if (isVanilla && vanilla != null) {
            meta.addEnchant(vanilla, level, unsafeVanilla);
        } else {
            // custom "façon vanilla" dans le lore, sans PDC
            var lore = meta.lore() == null ? new java.util.ArrayList<net.kyori.adventure.text.Component>()
                    : new java.util.ArrayList<>(meta.lore());
            lore.add(Component.text(displayName + " " + level)
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false));
            meta.lore(lore);
        }
    }

    public ItemStack getItem(){
        ItemStack item = new ItemStack(materialOfEnchant);
        ItemMeta meta = item.getItemMeta();

        // Nom de l'enchant
        meta.displayName(Component.text(displayName)
                .color(NamedTextColor.AQUA)
                .decoration(TextDecoration.ITALIC, false));

        // Lore : coût, niveau max, type
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text("──────────────", NamedTextColor.DARK_GRAY)); // ligne de séparation
        lore.add(
                Component.text("Niveau maximum : ", NamedTextColor.GRAY)
                        .append(Component.text(String.valueOf(maxLevel), NamedTextColor.GOLD))
                        .decoration(TextDecoration.ITALIC, false)
        );
        lore.add(
                Component.text("Coût en points : ", NamedTextColor.GRAY)
                        .append(Component.text(String.valueOf(pointsCost), NamedTextColor.GREEN))
                        .decoration(TextDecoration.ITALIC, false)
        );
        lore.add(Component.text("──────────────", NamedTextColor.DARK_GRAY)); // ligne de séparation
        lore.add(Component.empty());

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static List<EnchantType> getCompatibleEnchantToEnchantType(ToolsEnum tool) {
        List<EnchantType> list = new ArrayList<>();
        for (EnchantType type : values()) {
            if (type.allowedOn(tool)) {
                list.add(type);
            }
        }
        return list;
    }

    public static List<ItemStack> getCompatibleEnchantToItemStack(ToolsEnum tool) {
        List<ItemStack> list = new ArrayList<>();
        for (EnchantType type : values()) {
            if (type.allowedOn(tool)) {
                list.add(type.getItem());
            }
        }
        return list;
    }

}

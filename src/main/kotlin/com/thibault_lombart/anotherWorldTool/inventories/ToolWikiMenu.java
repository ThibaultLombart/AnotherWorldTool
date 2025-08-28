package com.thibault_lombart.anotherWorldTool.inventories;

import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import com.thibault_lombart.anotherWorldTool.utils.BlockCategories;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;


public class ToolWikiMenu implements InventoryHolder {

    private final Inventory inv;

    public ToolWikiMenu(UUID player, ToolsEnum toolsEnum, BlockCategories blockCategories) {

        this.inv = Bukkit.createInventory(this, 27, Component.text("Wiki xp"));

        putItems(inv, toolsEnum, blockCategories);

    }

    private void putItems (Inventory inv, ToolsEnum toolsEnum, BlockCategories blockCategories) {
        Map<Material, Integer> map = blockCategories.getMaterialFromTool(toolsEnum);

        map.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(entry -> {
                    Material display = toDisplayableItem(entry.getKey());
                    return display == null ? null : Map.entry(display, entry.getValue());
                })
                .filter(Objects::nonNull)
                .forEach(entry -> {
                    Material material = entry.getKey();
                    int xp = entry.getValue();

                    ItemStack itemStack = new ItemStack(material);
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.empty());
                    lore.add(Component.text("──────────", NamedTextColor.DARK_GRAY));
                    lore.add(Component.text("Moyen de gagner l'XP : ", NamedTextColor.GRAY)
                            .append(Component.text("Casser.", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .decoration(TextDecoration.ITALIC, false));
                    lore.add(Component.text("Points d'XP gagnés : ", NamedTextColor.GRAY)
                            .append(Component.text(xp + (xp == 1 ? " point" : " points"),
                                    NamedTextColor.GREEN, TextDecoration.BOLD))
                            .decoration(TextDecoration.ITALIC, false));
                    lore.add(Component.text("──────────", NamedTextColor.DARK_GRAY));

                    itemMeta.lore(lore);
                    itemStack.setItemMeta(itemMeta);
                    inv.addItem(itemStack);
                });
    }

    private static @Nullable Material toDisplayableItem(Material m) {
        if (m.isItem()) return m; // déjà OK

        switch (m) {
            // 🌾 HOE
            case WHEAT: return Material.WHEAT; // pousse -> récolte
            case CARROTS: return Material.CARROT;
            case POTATOES: return Material.POTATO;
            case BEETROOTS: return Material.BEETROOT;
            case NETHER_WART: return Material.NETHER_WART;
            case SUGAR_CANE: return Material.SUGAR_CANE;
            case SWEET_BERRY_BUSH: return Material.SWEET_BERRIES;
            case BAMBOO: return Material.BAMBOO;

            // 🍫 AXE
            case COCOA: return Material.COCOA_BEANS;
            default: return Material.BARRIER;
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}

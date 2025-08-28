package com.thibault_lombart.anotherWorldTool.inventories;

import com.thibault_lombart.anotherWorldTool.enums.EnchantType;
import com.thibault_lombart.anotherWorldTool.storage.PlayerInformations;
import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList;
import com.thibault_lombart.anotherWorldTool.tools.Tool;
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

import java.util.*;

import static java.lang.Math.max;

public class ToolEnchantMenu implements InventoryHolder {

    private final Inventory inv;
    private final Tool tool;
    private int slotWiki;
    private final Map<Integer, EnchantType> slotMap = new HashMap<>();

    public ToolEnchantMenu(UUID uuid, Tool tool) {
        this.tool = tool;
        PlayerInformations informations = PlayersInformationsList.getPlayersInformations(uuid);

        this.inv = Bukkit.createInventory(this, 36, Component.text("Menu de l'outil " + tool.getName()));

        setTool(0, tool);
        setPoints(9, tool);
        setSeparation();
        setWiki(27);
        setEnchants(tool);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inv;
    }

    private void setTool(int slot, Tool tool) {
        this.inv.setItem(slot, tool.getTool());
    }

    private void setPoints(int slot, Tool tool) {
        Material coin = Material.SUNFLOWER;
        int points = tool.getPoints();
        int pointsMini1 = max(points,1);
        ItemStack coinItem = new ItemStack(coin, pointsMini1);
        ItemMeta meta = coinItem.getItemMeta();

        String pointWord = (points <= 1 ? "Point" : "Points");

        meta.displayName(
                Component.text("✦ " + pointWord + " : ", NamedTextColor.GOLD, TextDecoration.BOLD)
                        .append(Component.text(String.valueOf(tool.getPoints()), NamedTextColor.GREEN, TextDecoration.BOLD))
                        .decoration(TextDecoration.ITALIC, false)
        );
        coinItem.setItemMeta(meta);

        this.inv.setItem(slot, coinItem);
    }

    private void setSeparation() {
        Material stainedGlass = Material.GRAY_STAINED_GLASS_PANE;
        ItemStack stainedGlassItem = new ItemStack(stainedGlass, 1);
        ItemMeta meta = stainedGlassItem.getItemMeta();
        meta.displayName(Component.empty());
        stainedGlassItem.setItemMeta(meta);
        this.inv.setItem(1, stainedGlassItem);
        this.inv.setItem(10, stainedGlassItem);
        this.inv.setItem(19, stainedGlassItem);
        this.inv.setItem(28, stainedGlassItem);
    }

    private void setWiki(int slot) {
        Material book = Material.BOOK;
        ItemStack bookItem = new ItemStack(book, 1);
        ItemMeta meta = bookItem.getItemMeta();

        meta.displayName(
                Component.text("📖 Moyen d'avoir de l'xp", NamedTextColor.DARK_AQUA, TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        List<Component> lore = new ArrayList<>();

        lore.add(Component.text(" ", NamedTextColor.GRAY)); // ligne vide
        lore.add(Component.text(" • Clique droit pour voir la liste complète", NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("des moyens pour avoir de l'xp. • ", NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(" ", NamedTextColor.GRAY));

        meta.lore(lore);
        bookItem.setItemMeta(meta);

        this.inv.setItem(slot, bookItem);
        this.slotWiki = slot;
    }

    private void setEnchants(Tool tool) {
        List<EnchantType> types = EnchantType.getCompatibleEnchantToEnchantType(tool.getToolsEnum());

        List<Integer> allowedSlots = new ArrayList<>();
        for (int slot = 0; slot < inv.getSize(); slot++) {
            int col = slot % 9;
            if (col >= 2) allowedSlots.add(slot);
        }

        int i = 0;
        for (EnchantType type : types) {
            if (i >= allowedSlots.size()) break; // plus de place -> (éventuellement prévoir pagination)
            int slot = allowedSlots.get(i++);

            boolean isDisable = tool.isDesactivated(type);

            int levelOfEnchant = tool.getEnchantLevel(type);

            // Construis l’item d’affichage pour l’enchant
            // (supposons que tu as EnchantType#getItem() qui renvoie un ItemStack prêt pour le GUI)
            ItemStack icon = type.getItem();
            ItemMeta meta = icon.getItemMeta();

            List<Component> lore = meta.lore();

            lore.add(Component.text("──────────────", NamedTextColor.DARK_GRAY)); // ligne de séparation

            // Vérification de l'état (débloqué ou non)
            if(levelOfEnchant > 0) {
                lore.add(
                        Component.text("État : ", NamedTextColor.GRAY)
                                .append(Component.text("Débloqué", NamedTextColor.GREEN, TextDecoration.BOLD))
                                .decoration(TextDecoration.ITALIC, false)
                );
                lore.add(
                        Component.text("Votre niveau : ", NamedTextColor.GRAY)
                                .append(Component.text(String.valueOf(levelOfEnchant), NamedTextColor.GOLD))
                                .decoration(TextDecoration.ITALIC, false)
                );

                // Enchantement activé ou non
                if (!isDisable) {
                    lore.add(
                            Component.text("Status : ", NamedTextColor.GRAY)
                            .append(Component.text("Activé", NamedTextColor.GREEN, TextDecoration.BOLD))
                            .decoration(TextDecoration.ITALIC, false)
                    );
                } else {
                    lore.add(
                            Component.text("Status : ", NamedTextColor.GRAY)
                                    .append(Component.text("Désactivé", NamedTextColor.RED, TextDecoration.BOLD))
                                    .decoration(TextDecoration.ITALIC, false)
                    );
                }

            } else {
                lore.add(
                        Component.text("État : ", NamedTextColor.GRAY)
                                .append(Component.text("Non Débloqué", NamedTextColor.RED, TextDecoration.BOLD))
                                .decoration(TextDecoration.ITALIC, false)
                );
            }
            lore.add(Component.text("──────────────", NamedTextColor.DARK_GRAY)); // ligne de séparation
            lore.add(Component.empty());

            // Vérification du niveau
            if(levelOfEnchant >= type.maxLevel) {
                lore.add(Component.text("❌ Niveau maximum atteint ",
                                NamedTextColor.RED, TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false));
            } else {
                if (tool.getLevel() < type.minimumToolLevel) {
                    lore.add(Component.text("❌ Requiert niveau " + type.minimumToolLevel,
                                    NamedTextColor.RED, TextDecoration.BOLD)
                            .decoration(TextDecoration.ITALIC, false));
                } else {
                    if (tool.getPoints() < type.pointsCost) {

                        int manque = type.pointsCost - tool.getPoints();

                        String pointWord = (manque == 1 ? "point" : "points");

                        if (levelOfEnchant > 0) {
                            lore.add(Component.text("❌ Manque " + manque + " " + pointWord + " pour améliorer",
                                            NamedTextColor.RED, TextDecoration.BOLD)
                                    .decoration(TextDecoration.ITALIC, false));
                        } else {
                            lore.add(Component.text("❌ Manque " + manque + " " + pointWord + " pour débloquer",
                                            NamedTextColor.RED, TextDecoration.BOLD)
                                    .decoration(TextDecoration.ITALIC, false));
                        }

                    } else {
                        if (levelOfEnchant > 0) {
                            lore.add(Component.text("✔ Améliorable au niveau " + (levelOfEnchant + 1),
                                            NamedTextColor.GREEN, TextDecoration.BOLD)
                                    .decoration(TextDecoration.ITALIC, false));
                        } else {
                            lore.add(Component.text("✔ Débloquable", NamedTextColor.GREEN, TextDecoration.BOLD)
                                    .decoration(TextDecoration.ITALIC, false));
                        }
                    }
                }
            }

            if(levelOfEnchant > 0) {
                Component lore2 = Component.text("Clique droit pour : ",
                        NamedTextColor.GRAY);

                if (!isDisable) {
                    lore.add(lore2.append(Component.text("Désactiver", NamedTextColor.RED, TextDecoration.BOLD)));
                } else {
                    lore.add(lore2.append(Component.text("Activer", NamedTextColor.GREEN, TextDecoration.BOLD)));
                }
            }

            slotMap.put(slot, type);

            meta.lore(lore);
            icon.setItemMeta(meta);
            inv.setItem(slot, icon);
        }
    }

    public EnchantType getEnchantType(int slot) {
        return slotMap.get(slot);
    }

    public int getSlotWiki() {
        return this.slotWiki;
    }

    public Tool getTool() {
        return this.tool;
    }
}

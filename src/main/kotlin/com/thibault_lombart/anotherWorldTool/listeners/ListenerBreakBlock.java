package com.thibault_lombart.anotherWorldTool.listeners;

import com.thibault_lombart.anotherWorldTool.AnotherWorldTool;
import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import com.thibault_lombart.anotherWorldTool.storage.PlayerInformations;
import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList;
import com.thibault_lombart.anotherWorldTool.tools.CustomItemTag;
import com.thibault_lombart.anotherWorldTool.tools.Tool;
import com.thibault_lombart.anotherWorldTool.utils.BlockCategories;
import com.thibault_lombart.anotherWorldTool.utils.CropMaturity;
import com.thibault_lombart.anotherWorldTool.utils.LoreUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import com.thibault_lombart.anotherWorldTool.debouncers.LoreUpdateDebouncer;

import java.time.Duration;

public class ListenerBreakBlock implements Listener {

    private final AnotherWorldTool plugin;
    private final BlockCategories categories;
    private final LoreUpdateDebouncer debouncer;

    public ListenerBreakBlock(AnotherWorldTool plugin, BlockCategories categories) {
        this.plugin = plugin;
        this.categories = categories;
        this.debouncer = new LoreUpdateDebouncer(plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
        if (!CustomItemTag.isCustomItem(hand)) return; // pas ton item → ignore

        // Quel outil est-ce ?
        ToolsEnum toolEnum = fromItem(hand);
        if (toolEnum == null) return;

        // Le bloc correspond-il à la catégorie de l’outil ?
        Material m = e.getBlock().getType();
        if (!categories.matches(toolEnum, m)) return;

        if (CropMaturity.requiresMaturity(m) && !CropMaturity.isFullyGrown(e.getBlock())) {
            return;
        }

        // Récupère les infos joueur + l'instance Tool à XP
        PlayerInformations playerInformations = PlayersInformationsList.getPlayersInformations(e.getPlayer().getUniqueId());
        if (playerInformations == null) return;

        Tool tool = playerInformations.getToolByEnum(toolEnum);
        if (tool == null) return;

        int xpToGive = categories.xpFor(toolEnum, m);

        // Donne l'XP
        boolean leveled = playerInformations.addXp(tool,xpToGive);

        // Gestion des rankups et messages
        if (leveled) {
            Material current = hand.getType();
            Material expected = tool.getMaterial();

            if (!current.equals(expected)) {
                // Matériau change (ex: Stone -> Iron) : on reconstruit
                ItemStack updated = tool.getTool();
                e.getPlayer().getInventory().setItemInMainHand(updated);
            } else {
                // Même matériau : juste maj du lore (pas de “flash”)
                LoreUtils.updateXpLine(hand, tool);
            }

            Title title = Title.title(
                    Component.text("⬆ Niveau d’outil +1").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
                    Component.text(tool.getName() + " → niveau " + tool.getLevel()).color(NamedTextColor.GOLD),
                    Title.Times.times(Duration.ofMillis(200), Duration.ofSeconds(2), Duration.ofMillis(300))
            );
            e.getPlayer().showTitle(title);
            // petit son :
            e.getPlayer().playSound(e.getPlayer().getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1.2f);

            int toolLevel = tool.getLevel();
            if (toolLevel % 5 == 0) {
                Bukkit.broadcast(
                        Component.text("🎉 " + e.getPlayer().getName() +
                                " vient d'atteindre le niveau " + toolLevel +
                                " avec son item : " + tool.getName() + " !")
                );
            }

            // annule toute maj différée car on vient d’en faire une
            debouncer.cancel(e.getPlayer().getUniqueId());
            return;
        } else {
            // feedback temps réel SANS toucher à l’item
            e.getPlayer().sendActionBar(
                    net.kyori.adventure.text.Component.text("XP: " + tool.getXP() + " / " + tool.getXpToNextLevel())
                            .color(net.kyori.adventure.text.format.NamedTextColor.GRAY)
            );
        }

        // Pas de level-up → DIFFÉRER la maj du lore (évite l’animation spam)
        debouncer.schedule(e.getPlayer().getUniqueId(), () -> {
            ItemStack stillHand = e.getPlayer().getInventory().getItemInMainHand();
            // toujours le même item custom en main ?
            if (stillHand != null && CustomItemTag.isCustomItem(stillHand)) {
                LoreUtils.updateXpLine(stillHand, tool);
                e.getPlayer().updateInventory();
            }
        }, 10L); // maj groupée ~0.5s après le dernier bloc
    }

    private ToolsEnum fromItem(ItemStack item) {
        String id = CustomItemTag.getId(item);
        if (id == null) return null;

        ToolsEnum toolEnum = ToolsEnum.fromId(id);
        return toolEnum;
    }

}

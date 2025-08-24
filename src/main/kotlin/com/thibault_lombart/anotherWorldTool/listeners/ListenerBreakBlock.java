package com.thibault_lombart.anotherWorldTool.listeners;

import com.thibault_lombart.anotherWorldTool.AnotherWorldTool;
import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import com.thibault_lombart.anotherWorldTool.storage.PlayerInformations;
import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList;
import com.thibault_lombart.anotherWorldTool.tools.CustomItemTag;
import com.thibault_lombart.anotherWorldTool.tools.Tool;
import com.thibault_lombart.anotherWorldTool.utils.BlockCategories;
import com.thibault_lombart.anotherWorldTool.utils.LoreUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import com.thibault_lombart.anotherWorldTool.debouncers.LoreUpdateDebouncer;

public class ListenerBreakBlock implements Listener {

    private final AnotherWorldTool plugin;
    private final LoreUpdateDebouncer debouncer;

    public ListenerBreakBlock(AnotherWorldTool plugin) {
        this.plugin = plugin;
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
        if (!matches(toolEnum, m)) return;

        // Récupère les infos joueur + l'instance Tool à XP
        PlayerInformations playerInformations = PlayersInformationsList.getPlayersInformations(e.getPlayer().getUniqueId());
        if (playerInformations == null) return;

        Tool tool = playerInformations.getToolByEnum(toolEnum);
        if (tool == null) return;

        // Donne l'XP (choisis ta valeur, ici 1)
        boolean leveled = tool.addXP(1);

        // feedback temps réel SANS toucher à l’item
        e.getPlayer().sendActionBar(
                net.kyori.adventure.text.Component.text("XP: " + tool.getXP() + " / " + tool.getXpToNextLevel())
                        .color(net.kyori.adventure.text.format.NamedTextColor.GRAY)
        );

        // Optionnel : petit feedback level-up
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

            e.getPlayer().sendActionBar(net.kyori.adventure.text.Component.text("§aNiveau d'outil +1 (" + tool.getLevel() + ")"));
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

    private boolean matches(ToolsEnum t, Material m) {
        return switch (t) {
            case AXE     -> BlockCategories.forAxe(m);
            case PICKAXE -> BlockCategories.forPickaxe(m);
            case HOE     -> BlockCategories.forHoe(m);
            case SHEARS  -> BlockCategories.forShears(m);
            case SHOVEL  -> BlockCategories.forShovel(m);
        };
    }

}

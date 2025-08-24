package com.thibault_lombart.anotherWorldTool.listeners;

import com.thibault_lombart.anotherWorldTool.AnotherWorldTool;
import com.thibault_lombart.anotherWorldTool.tools.CustomItemTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ListenerCustomItemInventory implements Listener {

    private final AnotherWorldTool plugin;

    public ListenerCustomItemInventory(AnotherWorldTool plugin) {
        this.plugin = plugin;
    }

    // ----- DROP interdit (jeter l'objet)
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack stack = e.getItemDrop().getItemStack();
        if (CustomItemTag.isCustomItem(stack)) {
            e.setCancelled(true);
            e.getPlayer().updateInventory();
        }
    }

    // ----- Clic dans inventaires
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory top = e.getView().getTopInventory();          // coffre/établi/etc.
        Inventory bottom = e.getView().getBottomInventory();    // inventaire du joueur
        Inventory clicked = e.getClickedInventory();            // inventaire cliqué
        ItemStack current = e.getCurrentItem();                 // item dans le slot cliqué
        ItemStack cursor  = e.getCursor();                      // item tenu au curseur

        // 1) Poser un custom dans un inventaire non-joueur (top) : interdit
        if (clicked == top && CustomItemTag.isCustomItem(cursor)) {
            e.setCancelled(true);
            return;
        }

        // 2) SHIFT-CLICK : move-to-other-inventory
        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            boolean fromPlayerInv = (clicked == bottom);
            if (fromPlayerInv && CustomItemTag.isCustomItem(current)) {
                // essai de déplacer vers le top (coffre/établi) -> interdit
                e.setCancelled(true);
                return;
            }
        }

        // 3) SWAP HOTBAR (1..9) vers un inventaire non-joueur
        if ((e.getAction() == InventoryAction.HOTBAR_SWAP)
                && clicked == top) {
            int hotbarSlot = e.getHotbarButton();
            if (hotbarSlot >= 0) {
                ItemStack source = e.getWhoClicked().getInventory().getItem(hotbarSlot);
                if (CustomItemTag.isCustomItem(source)) {
                    e.setCancelled(true);
                    return;
                }
            }
        }

        // 4) DROP depuis inventaire (touche Q sur slot / curseur)
        switch (e.getAction()) {
            case DROP_ONE_SLOT, DROP_ALL_SLOT -> {
                if (CustomItemTag.isCustomItem(current)) { e.setCancelled(true); return; }
            }
            case DROP_ONE_CURSOR, DROP_ALL_CURSOR -> {
                if (CustomItemTag.isCustomItem(cursor))  { e.setCancelled(true); return; }
            }
            default -> {}
        }

        // 5) Collect-to-cursor (double-clic) vers le curseur custom dans un GUI : interdit
        if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR && CustomItemTag.isCustomItem(cursor) && clicked == top) {
            e.setCancelled(true);
        }
    }

    // ----- Drag (glisser) dans le top inventaire : interdit si curseur custom
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (!CustomItemTag.isCustomItem(e.getOldCursor())) return;
        int topSize = e.getView().getTopInventory().getSize();
        // rawSlots < topSize => cases du "top" (coffre/établi)
        boolean touchesTop = e.getRawSlots().stream().anyMatch(slot -> slot < topSize);
        if (touchesTop) {
            e.setCancelled(true);
        }
    }


    // SUPPRIMER SI MORT
    @EventHandler
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent e) {
        e.getDrops().removeIf(CustomItemTag::isCustomItem);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        Bukkit.getScheduler().runTask(plugin, () -> {
            p.performCommand("anotherworldtool get");
        });
    }



}

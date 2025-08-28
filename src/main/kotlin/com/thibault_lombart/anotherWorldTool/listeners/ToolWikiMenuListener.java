package com.thibault_lombart.anotherWorldTool.listeners;

import com.thibault_lombart.anotherWorldTool.inventories.ToolWikiMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ToolWikiMenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        if (!(inv.getHolder() instanceof ToolWikiMenu menu)) return;

        e.setCancelled(true);
    }

}

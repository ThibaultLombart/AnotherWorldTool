package com.thibault_lombart.anotherWorldTool.listeners;

import com.thibault_lombart.anotherWorldTool.enums.EnchantType;
import com.thibault_lombart.anotherWorldTool.inventories.ToolEnchantMenu;
import com.thibault_lombart.anotherWorldTool.inventories.ToolSelectMenu;
import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList;
import com.thibault_lombart.anotherWorldTool.tools.CustomItemTag;
import com.thibault_lombart.anotherWorldTool.tools.Tool;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ToolEnchantMenuListener implements Listener {

    // gère le clic dans le menu
    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        if (!(inv.getHolder() instanceof ToolEnchantMenu menu)) return;

        e.setCancelled(true); // on ne veut pas déplacer les items du menu
        if (!(e.getWhoClicked() instanceof Player p)) return;

        int slot = e.getRawSlot();
        if (slot == menu.getSlotWiki()) return;

        EnchantType selected = menu.getEnchantType(slot);
        if (selected == null) return; // clic sur autre chose qu'un enchant ou un enchant pas disponible.



        switch (e.getClick()) {
            case RIGHT -> {
                // rien pour le moment
            }
            default -> {
                Tool tool = menu.getTool();
                tool.upgradeEnchantment(selected);

                ItemStack inHand = p.getInventory().getItemInMainHand();
                if (CustomItemTag.hasId(inHand, tool.getToolsEnum().name())) {
                    p.getInventory().setItemInMainHand(tool.getTool());
                }

                p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_PLACE, 1f, 1.2f);
                ToolEnchantMenu menuEnchant = new ToolEnchantMenu(p.getUniqueId(), tool);
                p.openInventory(menuEnchant.getInventory());
            }
        }
    }

}

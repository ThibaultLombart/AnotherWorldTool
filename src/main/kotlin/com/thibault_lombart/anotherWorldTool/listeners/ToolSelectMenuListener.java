package com.thibault_lombart.anotherWorldTool.listeners;

import com.thibault_lombart.anotherWorldTool.inventories.ToolSelectMenu;
import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList;
import com.thibault_lombart.anotherWorldTool.tools.CustomItemTag;
import com.thibault_lombart.anotherWorldTool.tools.Tool;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ToolSelectMenuListener implements Listener {

    // Ouvre le menu quand on fait clic droit avec l'item custom en main
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        // on ne s'intéresse qu'à la main principale
        if (e.getHand() != EquipmentSlot.HAND) return;

        switch (e.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> {
                ItemStack inHand = e.getPlayer().getInventory().getItemInMainHand();
                if (!CustomItemTag.isCustomItem(inHand)) return;

                e.setCancelled(true);
                ToolSelectMenu menu = new ToolSelectMenu(e.getPlayer().getUniqueId());
                e.getPlayer().openInventory(menu.getInventory());
            }
            default -> {}
        }
    }

    // gère le clic dans le menu
    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        if (!(inv.getHolder() instanceof ToolSelectMenu menu)) return;

        e.setCancelled(true); // on ne veut pas déplacer les items du menu
        if (!(e.getWhoClicked() instanceof Player p)) return;

        int slot = e.getRawSlot();
        Tool selected = menu.toolAt(slot);
        if (selected == null) return; // clic en dehors des 5 slots

        // Remplacer l'item en main si c'est bien un custom
        ItemStack inHand = p.getInventory().getItemInMainHand();
        if (!CustomItemTag.isCustomItem(inHand)) {
            p.closeInventory();
            p.sendMessage(net.kyori.adventure.text.Component.text("Tu dois avoir l'item personnalisé en main."));
            return;
        }

        // Remplacer
        PlayersInformationsList.getPlayersInformations(p.getUniqueId()).changeCurrentTool(selected);
        p.getInventory().setItemInMainHand(selected.getTool());
        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.2f);
        p.closeInventory();
    }

}

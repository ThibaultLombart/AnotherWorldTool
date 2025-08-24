package com.thibault_lombart.anotherWorldTool.listeners;

import com.thibault_lombart.anotherWorldTool.AnotherWorldTool;
import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import com.thibault_lombart.anotherWorldTool.storage.PlayerInformations;
import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList;
import com.thibault_lombart.anotherWorldTool.tools.CustomItemTag;
import com.thibault_lombart.anotherWorldTool.tools.Tool;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ListenerOnJoinOnLeave implements Listener {

    private final AnotherWorldTool plugin;

    public ListenerOnJoinOnLeave(AnotherWorldTool plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        PlayerInformations playerInformations = new PlayerInformations(uuid);

        PlayersInformationsList.addPlayersInformations(uuid, playerInformations);

            if(CustomItemTag.hasCustomItem(event.getPlayer())) {

                // On parcourt l'inventaire pour trouver l'item custom.
                for (ItemStack it : event.getPlayer().getInventory().getContents()) {

                    if (CustomItemTag.isCustomItem(it)) {

                        // 1) Récupère l’ID posé par ton tag (ex: "AXE", "PICKAXE", ...)
                        String id = CustomItemTag.getId(it);

                        // 2) Convertit en enum
                        ToolsEnum toolEnum = ToolsEnum.fromId(id);

                        if (toolEnum == null) continue;

                        // 3) on le passe en current
                        if (playerInformations.getToolByEnum(toolEnum) != null) {
                            playerInformations.changeCurrentTool(playerInformations.getToolByEnum(toolEnum));
                        }

                        break; // on s’arrête au premier item custom trouvé

                    }

                }
            } else {
                event.getPlayer().getInventory().addItem(playerInformations.getCurrentTool().getTool());
                event.getPlayer().updateInventory();
            }



    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        PlayersInformationsList.removePlayersInformations(uuid);
    }

}

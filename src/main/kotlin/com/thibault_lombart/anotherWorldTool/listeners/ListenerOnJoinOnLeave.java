package com.thibault_lombart.anotherWorldTool.listeners;

import com.thibault_lombart.anotherWorldTool.AnotherWorldTool;
import com.thibault_lombart.anotherWorldTool.storage.PlayerInformations;
import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        PlayersInformationsList.removePlayersInformations(uuid);
    }

}

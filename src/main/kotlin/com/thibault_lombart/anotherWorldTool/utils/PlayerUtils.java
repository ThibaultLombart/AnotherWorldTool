package com.thibault_lombart.anotherWorldTool.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtils {

    public static String getPlayerNameFromUUID(UUID uuid) {
        Player online = Bukkit.getPlayer(uuid);
        if (online != null) {
            return online.getName(); // joueur en ligne
        }
        OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
        return offline.getName(); // peut être null si jamais venu
    }

}

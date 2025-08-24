package com.thibault_lombart.anotherWorldTool.storage;

import java.util.HashMap;
import java.util.UUID;

public class PlayersInformationsList {

    private static HashMap<UUID, PlayerInformations> playersInformationsList = new HashMap<>();

    public static PlayerInformations getPlayersInformations(UUID uuid) {
        return playersInformationsList.get(uuid);
    }

    public static void addPlayersInformations(UUID uuid, PlayerInformations playerInformations) {
        playersInformationsList.put(uuid, playerInformations);
    }

    public static void removePlayersInformations(UUID uuid) {
        playersInformationsList.remove(uuid);
    }

}

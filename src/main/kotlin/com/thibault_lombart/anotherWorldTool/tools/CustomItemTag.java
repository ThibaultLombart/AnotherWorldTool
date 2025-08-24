package com.thibault_lombart.anotherWorldTool.tools;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;

public final class CustomItemTag {

    private CustomItemTag() {}

    public static NamespacedKey ITEM_ID_KEY;
    public static NamespacedKey ITEM_USER_KEY;

    // Utilisé pour tager un item
    public static void tag(ItemMeta meta, String id) {
        meta.getPersistentDataContainer().set(ITEM_ID_KEY, PersistentDataType.STRING, id);
    }

    public static void tagUser(ItemMeta meta, String id) {
        meta.getPersistentDataContainer().set(ITEM_USER_KEY, PersistentDataType.STRING, id);
    }

    // Utilisé pour récupérer l'id d'un item
    public static @Nullable String getId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer()
                .get(ITEM_ID_KEY, PersistentDataType.STRING);
    }

    public static @Nullable String getUser(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer()
                .get(ITEM_USER_KEY, PersistentDataType.STRING);
    }

    // Utilisé pour vérifier si l'item est bien de l'id que je souhaite
    public static boolean hasId(ItemStack item, String expected) {
        String id = getId(item);
        return expected.equals(id);
    }

    public static boolean hasUser(ItemStack item, String expected) {
        String id = getUser(item);
        return expected.equals(id);
    }

    // Utilisé pour vérifier si l'item est un item custom
    public static boolean isCustomItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(ITEM_ID_KEY, PersistentDataType.STRING);
    }

    public static boolean hasCustomItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (CustomItemTag.isCustomItem(item)) {
                return true; // trouvé un item custom
            }
        }
        return false;
    }


}

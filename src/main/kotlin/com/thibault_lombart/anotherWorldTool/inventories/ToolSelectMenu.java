package com.thibault_lombart.anotherWorldTool.inventories;

import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import com.thibault_lombart.anotherWorldTool.storage.PlayerInformations;
import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList;
import com.thibault_lombart.anotherWorldTool.tools.Tool;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ToolSelectMenu implements InventoryHolder {

    private final Inventory inv;
    private final Map<Integer, Tool> slotMap = new HashMap<>();

    public ToolSelectMenu(UUID uuid) {
        this.inv = Bukkit.createInventory(this, 27, Component.text("Sélection d'outil"));

        PlayerInformations informations = PlayersInformationsList.getPlayersInformations(uuid);

        // positions jolis : 10..14
        put(11, informations.getAxeTool(), informations.getCurrentTool().equals(informations.getAxeTool()));
        put(12, informations.getShovelTool(), informations.getCurrentTool().equals(informations.getShovelTool()));
        put(13, informations.getPickaxeTool(), informations.getCurrentTool().equals(informations.getPickaxeTool()));
        put(14, informations.getShearsTool(), informations.getCurrentTool().equals(informations.getShearsTool()));
        put(15, informations.getHoeTool(), informations.getCurrentTool().equals(informations.getHoeTool()));
    }

    private void put(int slot, Tool tool, Boolean current){
        ItemStack icon = tool.getTool();
        icon.getItemMeta().removeEnchantments();

        if (current) {

            ItemMeta meta = icon.getItemMeta();
            List<Component> lore = meta.lore();
            lore.add(Component.empty());
            lore.add(
                    Component.text("» ")
                            .color(NamedTextColor.DARK_GRAY)
                            .decoration(TextDecoration.ITALIC, false)
                            .append(Component.text("Équipé")
                                    .color(NamedTextColor.GREEN)
                                    .decorate(TextDecoration.BOLD)
                                    .decoration(TextDecoration.ITALIC, false))
            );
            meta.lore(lore);
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            icon.setItemMeta(meta);

        }

        this.inv.setItem(slot, icon);

        slotMap.put(slot, tool);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public Tool toolAt(int slot) {
        return slotMap.get(slot);
    }
}

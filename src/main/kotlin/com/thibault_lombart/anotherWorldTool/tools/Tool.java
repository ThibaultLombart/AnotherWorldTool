package com.thibault_lombart.anotherWorldTool.tools;

import com.thibault_lombart.anotherWorldTool.enums.EnchantType;
import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Tool {

    private int xp = 0;
    private int totalXp = 0;
    private int xpToNextLevel = 0;
    private int level = 1;
    private int points = 0;
    private String name = "Tool";
    private ToolsEnum toolsEnum = ToolsEnum.AXE;
    private java.util.EnumMap<EnchantType, Integer> enchantLevels = new java.util.EnumMap<>(EnchantType.class);
    private UUID user;

    public Tool(UUID user, String name, ToolsEnum toolsEnum) {
        this.name = name;
        this.toolsEnum = toolsEnum;
        this.user = user;
        this.xpToNextLevel = calculateXpForNextLevel(level);
    }

    public Tool(UUID user, int xp,int totalXp, int level, int points, String name, java.util.EnumMap<EnchantType, Integer> enchantLevels, ToolsEnum toolsEnum) {
        this.xp = xp;
        this.totalXp = totalXp;
        this.level = level;
        this.points = points;
        this.name = name;
        this.enchantLevels = enchantLevels.clone();
        this.toolsEnum = toolsEnum;
        this.user = user;
        this.xpToNextLevel = calculateXpForNextLevel(level);
    }

    public abstract ItemStack getTool();

    public abstract Material getMaterial();

    public UUID getUser() {
        return user;
    }

    public java.util.Map<EnchantType, Integer> getEnchantLevels() {
        return enchantLevels;
    }

    public void setEnchantLevel(EnchantType type, int level) {
        if (level <= 0) enchantLevels.remove(type);
        else enchantLevels.put(type, level);
    }

    public int getEnchantLevel(EnchantType type) {
        return enchantLevels.getOrDefault(type, 0);
    }

    public ToolsEnum getToolsEnum() {
        return toolsEnum;
    }

    public void setToolsEnum(ToolsEnum toolsEnum) {
        this.toolsEnum = toolsEnum;
    }

    public boolean levelUp() {
        if(xpToNextLevel <= xp){
            level++;
            xp = xp - xpToNextLevel;
            points++;
            xpToNextLevel = calculateXpForNextLevel(level);
            return true;
        }
        return false;
    }

    public boolean addXP(int xp) {
        this.xp += xp;
        this.totalXp += xp;
        return levelUp();
    }

    public int getXP() {
        return this.xp;
    }

    public int getTotalXp() {
        return this.totalXp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public int calculateXpForNextLevel(int level) {
        if (level <= 30) {
            return (int) (200 * Math.pow(level, 1.9));
        } else {
            int base = (int) (200 * Math.pow(30, 1.9));
            return base + (level - 30) * 2000;
        }
    }

    public ItemMeta changeItemMeta(ItemMeta meta) {
        CustomItemTag.tag(meta, this.toolsEnum.name());
        CustomItemTag.tagUser(meta, this.getUser().toString());
        meta.setUnbreakable(true);

        meta.displayName(
                Component.text(this.getName())
                        .color(NamedTextColor.GOLD)                // couleur spéciale
                        .decorate(TextDecoration.BOLD)             // en gras
                        .decoration(TextDecoration.ITALIC, false)  // désactive l’italique par défaut
        );

        List<Component> lore = new ArrayList<>();
        // ligne vide
        lore.add(Component.text(""));
        // level
        lore.add(Component.text("Level : " + this.getLevel())
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false));

        // xp
        lore.add(Component.text("XP : " + this.getXP() + " / " + this.getXpToNextLevel())
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(lore); // applique la liste de lore

        for (var entry : getEnchantLevels().entrySet()) {
            EnchantType type = entry.getKey();
            int lvl = entry.getValue();
            if (lvl <= 0) continue;
            if (!type.allowedOn(getToolsEnum())) continue;
            if (getLevel() < type.minimumToolLevel) continue;

            type.apply(meta, lvl, true);
        }

        return meta;
    }

    public void upgradeEnchantment(EnchantType enchantment) {

        if(enchantment.allowedOn(getToolsEnum()) && enchantment.pointsCost <= this.getPoints() && enchantment.minimumToolLevel <= this.getLevel() && enchantment.maxLevel > this.getEnchantLevel(enchantment)) {

            if (this.getEnchantLevel(enchantment) > 0) {
                this.setEnchantLevel(enchantment, this.getEnchantLevel(enchantment) + 1);
            } else {
                this.setEnchantLevel(enchantment, 1);
            }

            this.points -= 1;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tool tool = (Tool) o;
        return toolsEnum == tool.toolsEnum && Objects.equals(user, tool.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toolsEnum, user);
    }
}

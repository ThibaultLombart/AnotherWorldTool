package com.thibault_lombart.anotherWorldTool

import com.thibault_lombart.anotherWorldTool.commands.AnotherWorldToolCommand
import com.thibault_lombart.anotherWorldTool.listeners.ListenerBreakBlock
import com.thibault_lombart.anotherWorldTool.listeners.ListenerCustomItemInventory
import com.thibault_lombart.anotherWorldTool.tools.CustomItemTag
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import com.thibault_lombart.anotherWorldTool.listeners.ListenerOnJoinOnLeave
import com.thibault_lombart.anotherWorldTool.listeners.ToolSelectMenuListener


class AnotherWorldTool : JavaPlugin() {

    override fun onEnable() {
        display()
        CustomItemTag.ITEM_ID_KEY = NamespacedKey(this, "item_id")
        CustomItemTag.ITEM_USER_KEY = NamespacedKey(this, "item_user")

        Bukkit.getPluginManager().registerEvents(ListenerOnJoinOnLeave(this), this);
        Bukkit.getPluginManager().registerEvents(ListenerCustomItemInventory(this) ,this);
        Bukkit.getPluginManager().registerEvents(ToolSelectMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(ListenerBreakBlock(this), this);

        val cmd = AnotherWorldToolCommand(this)
        getCommand("anotherworldtool")!!.setExecutor(cmd)
        getCommand("anotherworldtool")!!.tabCompleter = cmd

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    fun display() {
        System.out.println("[AnotherWorldTool] Plugin loaded!");
    }
}

package com.thibault_lombart.anotherWorldTool.commands

import com.thibault_lombart.anotherWorldTool.storage.PlayersInformationsList
import com.thibault_lombart.anotherWorldTool.tools.CustomItemTag
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class AnotherWorldToolCommand(private val plugin: JavaPlugin) : CommandExecutor, TabCompleter {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if (args.isEmpty() || args[0].lowercase() != "get") {
            sender.sendMessage(Component.text("Usage: /$label get"))
            return true
        }

        val p = sender as? Player ?: run {
            sender.sendMessage(Component.text("Commande réservée aux joueurs."))
            return true
        }

        if (args.size != 1) {
            sender.sendMessage(Component.text("Usage: /$label get"))
            return true
        }

        if (!CustomItemTag.hasCustomItem(p)) {
            val item = PlayersInformationsList.getPlayersInformations(p.uniqueId).currentTool.tool
            p.inventory.addItem(item)
            p.sendMessage(Component.text("Tu as reçu ton item."))
        } else {
            p.sendMessage(Component.text("Tu as déjà un item personnalisé, tu ne peux pas en recevoir un autre."));
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return when (args.size) {
            1 -> mutableListOf("get")
            else -> mutableListOf()
        }
    }

}
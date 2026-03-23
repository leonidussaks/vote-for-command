package me.leonidussaks.voteForCommand

import me.leonidussaks.voteForCommand.command.ReloadCommand
import me.leonidussaks.voteForCommand.command.VoteCommand
import me.leonidussaks.voteForCommand.listener.ServerTickListener
import me.leonidussaks.voteForCommand.manager.ConfigManager
import me.leonidussaks.voteForCommand.manager.VoteManager
import me.leonidussaks.voteForCommand.menu.VoteMenu
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class VoteForCommand : JavaPlugin() {

    companion object {
        lateinit var PLUGIN: JavaPlugin
        lateinit var LOGGER: Logger
    }

    override fun onEnable() {
        PLUGIN = this
        LOGGER = this.logger

        // managers

        ConfigManager.start() // must be first
        VoteManager.start()

        // commands

        getCommand("vote")?.setExecutor(VoteCommand())
        getCommand("reload")?.setExecutor(ReloadCommand())

        // listeners
        server.pluginManager.registerEvents(ServerTickListener(), this)
        // listeners.menus
        server.pluginManager.registerEvents(VoteMenu(VoteMenu.inventorySize()), this)

    }

}

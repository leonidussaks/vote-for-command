package me.leonidussaks.voteForCommand.manager

import me.leonidussaks.voteForCommand.VoteForCommand
import me.leonidussaks.voteForCommand.data.LocaleConfig
import me.leonidussaks.voteForCommand.data.Vote
import org.bukkit.Material

object ConfigManager {

    lateinit var LOCALE_CONFIG: LocaleConfig
    var VOTE_TIME: Int = 60

    fun start() {
        VoteForCommand.PLUGIN.saveDefaultConfig()
        load()
    }

    fun load() {
        VoteManager.VOTING_COMMANDS.addAll(loadVotes()) // TwT ikr
        LOCALE_CONFIG = LocaleConfig.fromConfig(VoteForCommand.PLUGIN.config)
        VOTE_TIME = VoteForCommand.PLUGIN.config.getInt("voting-time", 60)
    }

    fun reload() {
        VoteManager.CURRENT_VOTE = null
        VoteForCommand.PLUGIN.reloadConfig()
        VoteManager.VOTING_COMMANDS.clear()
        VoteManager.VOTING_COMMANDS.addAll(loadVotes()) // TwT ikr
        LOCALE_CONFIG = LocaleConfig.fromConfig(VoteForCommand.PLUGIN.config)
        VOTE_TIME = VoteForCommand.PLUGIN.config.getInt("voting-time", 60)
    }

    fun loadVotes(): MutableList<Vote> {
        val config = VoteForCommand.PLUGIN.config
        return config.getConfigurationSection("votingCommands")?.getKeys(false)?.mapNotNull { key ->
            config.getConfigurationSection("votingCommands.$key")?.let { s ->
                Vote(
                    name = s.getString("name") ?: return@mapNotNull null,
                    description = s.getString("description") ?: "",
                    material = Material.valueOf(s.getString("material") ?: "PAPER"),
                    commands = s.getStringList("commands"),
                    cooldown = s.getInt("cooldown"),
                    percentToWin = s.getDouble("percentToWin").toFloat(),
                    minimumOnline = s.getInt("minimumOnline")
                )
            }
        }?.toMutableList() ?: mutableListOf()
    }

}
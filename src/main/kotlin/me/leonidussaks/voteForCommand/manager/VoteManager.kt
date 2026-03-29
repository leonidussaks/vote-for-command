package me.leonidussaks.voteForCommand.manager

import com.destroystokyo.paper.event.server.ServerTickStartEvent
import me.leonidussaks.voteForCommand.VoteForCommand
import me.leonidussaks.voteForCommand.data.Vote
import me.leonidussaks.voteForCommand.data.LocaleConfig
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.Duration


object VoteManager {

    var VOTING_COMMANDS: MutableList<Vote> = mutableListOf()
    var CURRENT_VOTE: Vote? = null
    lateinit var PLUGIN_NAME: Component

    fun start() {
        PLUGIN_NAME = Component.text(VoteForCommand.PLUGIN.config.getString("plugin-name") ?: "[VoteForCommand]: ")
    }

    fun startVote(vote: Vote, initiator: Player) {

        CURRENT_VOTE = vote
        vote.endTime = ConfigManager.VOTE_TIME


        val forClickCallback = ClickEvent.callback(
            { audience: Audience? ->
                if (CURRENT_VOTE == null)
                    return@callback

                val player = (audience as? Player) ?: return@callback

                if (!player.hasPermission("voteforcommand.vote.participant"))
                    return@callback

                if (CURRENT_VOTE!!.playersFor.contains(player)) return@callback

                if (CURRENT_VOTE!!.playersFor.contains(player) || CURRENT_VOTE!!.playersAgainst.contains(player)) {
                    player.sendMessage(Component.text(ConfigManager.LOCALE_CONFIG.alreadyMadeChoice, NamedTextColor.RED))
                    return@callback
                }

                CURRENT_VOTE!!.playersFor.add(player)
                player.sendMessage(Component.text(ConfigManager.LOCALE_CONFIG.youMadeChoice, NamedTextColor.GREEN)
                    .append(Component.text(ConfigManager.LOCALE_CONFIG.voteFor, NamedTextColor.GREEN)))
            },
            { options: ClickCallback.Options.Builder? ->
                options!!
                    .uses(ClickCallback.UNLIMITED_USES)
                    .lifetime(Duration.ofSeconds(CURRENT_VOTE!!.endTime.toLong()))
            }
        )

        val buttonFor: Component = Component.text(ConfigManager.LOCALE_CONFIG.voteFor, NamedTextColor.GREEN).clickEvent(forClickCallback)

        val againstClickCallback = ClickEvent.callback(
            { audience: Audience? ->
                if (CURRENT_VOTE == null)
                    return@callback

                val player = (audience as? Player) ?: return@callback

                if (!player.hasPermission("voteforcommand.vote.participant"))
                    return@callback

                if (CURRENT_VOTE!!.playersFor.contains(player) || CURRENT_VOTE!!.playersAgainst.contains(player)) {
                    player.sendMessage(Component.text(ConfigManager.LOCALE_CONFIG.alreadyMadeChoice, NamedTextColor.RED))
                    return@callback
                }
                CURRENT_VOTE!!.playersAgainst.add(audience)
                player.sendMessage(Component.text(ConfigManager.LOCALE_CONFIG.youMadeChoice, NamedTextColor.GREEN)
                    .append(Component.text(ConfigManager.LOCALE_CONFIG.voteAgainst, NamedTextColor.RED)))
            },
            { options: ClickCallback.Options.Builder? ->
                options!!
                    .uses(ClickCallback.UNLIMITED_USES)
                    .lifetime(Duration.ofSeconds(30))
            }
        )

        val buttonAgainst: Component = Component.text(ConfigManager.LOCALE_CONFIG.voteAgainst, NamedTextColor.RED)
            .clickEvent(againstClickCallback)

        var desc = vote.description
        if (desc.last() == '.' || desc.last() == '!')
            desc = desc.dropLast(1)
        val voteMessage = PLUGIN_NAME
            .append(Component.text(ConfigManager.LOCALE_CONFIG.newVoteStarted + "(${vote.endTime}s) remaining!\n", NamedTextColor.YELLOW))
            .append(Component.text(initiator.name, NamedTextColor.AQUA))
            .append(Component.text(ConfigManager.LOCALE_CONFIG.votesFor, NamedTextColor.YELLOW))
            .append(Component.text(vote.name + " (${desc}).\n", NamedTextColor.YELLOW))
            .append(buttonFor)
            .append(Component.text(" "))
            .append(buttonAgainst)

        Bukkit.getServer().sendMessage(voteMessage);
    }

    fun endCurrentVote() {

        if (CURRENT_VOTE == null)
            return

        var win = false
        val allPlayersCount: Int = CURRENT_VOTE?.playersFor?.count()!! + CURRENT_VOTE?.playersAgainst?.count()!!
        val forPlayersCount: Int = CURRENT_VOTE?.playersFor?.count()!!
        if (allPlayersCount != 0 && forPlayersCount != 0) {
            if ((forPlayersCount / allPlayersCount) * 100 >= CURRENT_VOTE?.percentToWin!!) {
                win = true
            }
        }

        if (win) {
            Bukkit.getServer().sendMessage(PLUGIN_NAME
                .append(Component.text(ConfigManager.LOCALE_CONFIG.voteWon, NamedTextColor.GREEN))
                .append(Component.text("($forPlayersCount/$allPlayersCount)"))
            )
            CURRENT_VOTE?.commands?.forEach { comma ->
                Bukkit.dispatchCommand(Bukkit.getServer().consoleSender, comma)
            }
        }
        else {
            Bukkit.getServer().sendMessage(PLUGIN_NAME
                .append(Component.text(ConfigManager.LOCALE_CONFIG.voteFailed, NamedTextColor.RED))
                .append(Component.text("($forPlayersCount/$allPlayersCount)"))
            )
        }

        CURRENT_VOTE?.currentCooldown = CURRENT_VOTE?.cooldown ?: 0
        CURRENT_VOTE?.playersFor?.clear()
        CURRENT_VOTE?.playersAgainst?.clear()
        CURRENT_VOTE = null
    }

    fun checkCanStartVote(vote: Vote): Boolean {

        if (CURRENT_VOTE != null) {
            Bukkit.getServer().sendMessage(PLUGIN_NAME.append(Component.text(ConfigManager.LOCALE_CONFIG.cannotStartVote, NamedTextColor.GREEN)))
            return false
        }

        if (vote.minimumOnline > Bukkit.getServer().onlinePlayers.count()) {
            Bukkit.getServer().sendMessage(PLUGIN_NAME.append(Component.text(ConfigManager.LOCALE_CONFIG.cannotStartVote, NamedTextColor.GREEN)))
            return false
        }

        if (vote.currentCooldown > 0) {
            Bukkit.getServer().sendMessage(PLUGIN_NAME.append(Component.text(ConfigManager.LOCALE_CONFIG.cannotStartVote, NamedTextColor.GREEN)))
            return false
        }

        return true
    }

    fun onTick(event: ServerTickStartEvent) {

        if ((event.tickNumber % 20) != 0)
            return

        // decrement of all current cooldowns
        val toChange = VOTING_COMMANDS.filter { it.currentCooldown > 0 }
        toChange.forEach { vote ->
            vote.currentCooldown--
        }
        //

        if (CURRENT_VOTE == null) {
            return
        }
        CURRENT_VOTE?.endTime--


        when (CURRENT_VOTE?.endTime) {
            120 -> {
                Bukkit.getServer().sendMessage(PLUGIN_NAME.append(Component.text(ConfigManager.LOCALE_CONFIG.twoMinutesRemaining, NamedTextColor.GREEN)))
            }
            60 -> {
                Bukkit.getServer().sendMessage(PLUGIN_NAME.append(Component.text(ConfigManager.LOCALE_CONFIG.sixtySecondsRemaining, NamedTextColor.GREEN)))
            }
            30 -> {
                Bukkit.getServer().sendMessage(PLUGIN_NAME.append(Component.text(ConfigManager.LOCALE_CONFIG.thirtySecondsRemaining, NamedTextColor.GREEN)))
            }
            10 -> {
                Bukkit.getServer().sendMessage(PLUGIN_NAME.append(Component.text(ConfigManager.LOCALE_CONFIG.tenSecondsRemaining, NamedTextColor.GREEN)))
            }
            0 -> {
                endCurrentVote()
            }
        }

    }

}

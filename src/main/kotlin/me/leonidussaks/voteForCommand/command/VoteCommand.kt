package me.leonidussaks.voteForCommand.command

import me.leonidussaks.voteForCommand.menu.VoteMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VoteCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if (sender !is Player)
            return true

        VoteMenu(VoteMenu.inventorySize()).init(sender)

        return true
    }
}
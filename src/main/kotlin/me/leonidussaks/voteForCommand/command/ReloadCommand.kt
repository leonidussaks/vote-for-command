package me.leonidussaks.voteForCommand.command

import me.leonidussaks.voteForCommand.manager.ConfigManager
import me.leonidussaks.voteForCommand.manager.VoteManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadCommand : CommandExecutor{
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {

        ConfigManager.reload()
        VoteManager.start()

        sender.sendMessage(Component.text("[VoteForCommand]: ")
            .append(Component.text("Reload... ", NamedTextColor.GREEN))
        )
        return true
    }


}
package me.leonidussaks.voteForCommand.menu

import me.leonidussaks.voteForCommand.manager.VoteManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent

class VoteMenu(size: Int) : MenuBase(size) {

    companion object {
        fun inventorySize(): Int {
            return (((VoteManager.VOTING_COMMANDS.count() - 1) / 9) + 1) * 9
                .coerceIn(9, 54)
        }
    }

    fun init(player: Player) {
        open(player)
    }

    override fun open(player: Player) {
        super.open(player)

        VoteManager.VOTING_COMMANDS.forEachIndexed { index, vote ->

            val lore: MutableList<Component> = mutableListOf(
                Component.text(vote.description, NamedTextColor.YELLOW),
                Component.text(""),
                Component.text("Cooldown: ${vote.currentCooldown}s", NamedTextColor.YELLOW),
            )
            val item = makeItem(vote.material, Component.text(vote.name), lore)
            inventory.setItem(index, item)
        }
    }

    @EventHandler
    override fun onClick(e: InventoryClickEvent) {
        super.onClick(e)
        if (e.whoClicked !is Player) return
        if (e.view.topInventory.holder !is VoteMenu) return
        e.isCancelled = true

        if (!VoteManager.checkCanStartVote(VoteManager.VOTING_COMMANDS[e.slot]))
            return

        VoteManager.startVote(VoteManager.VOTING_COMMANDS[e.slot], e.whoClicked as Player) // cuz it's list so it's safe

        e.whoClicked.closeInventory()

    }

}
package me.leonidussaks.voteForCommand.listener

import com.destroystokyo.paper.event.server.ServerTickEndEvent
import com.destroystokyo.paper.event.server.ServerTickStartEvent
import me.leonidussaks.voteForCommand.manager.VoteManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ServerTickListener : Listener {

    @EventHandler
    private fun onTick(event: ServerTickStartEvent) {

        VoteManager.onTick(event)

    }

}
package me.leonidussaks.voteForCommand.data

import org.bukkit.Material
import org.bukkit.entity.Player

data class Vote(
    val name: String,
    val description: String,
    val material: Material = Material.PAPER,
    val commands: List<String>,
    val cooldown: Int,
    val percentToWin: Float,
    val minimumOnline: Int,
    // runtime
    var playersFor: MutableList<Player> = mutableListOf(),
    var playersAgainst: MutableList<Player> = mutableListOf(),
    var currentCooldown: Int = 0,
    var endTime: Int = 35,
    //
    )

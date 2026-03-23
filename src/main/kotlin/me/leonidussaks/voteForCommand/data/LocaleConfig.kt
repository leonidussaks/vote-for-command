package me.leonidussaks.voteForCommand.data


data class LocaleConfig(
    val voteWon: String,
    val voteFailed: String,
    val voteFor: String,
    val voteAgainst: String,
    val alreadyMadeChoice: String,
    val youMadeChoice: String,
    val newVoteStarted: String,
    val votesFor: String,
    val twoMinutesRemaining: String,
    val sixtySecondsRemaining: String,
    val thirtySecondsRemaining: String,
    val tenSecondsRemaining: String,
    val cannotStartVote: String,
    val cannotStartVoteNotEnoughPlayers: String,
) {
    companion object {
        fun fromConfig(config: org.bukkit.configuration.file.FileConfiguration): LocaleConfig {
            return LocaleConfig(
                voteWon = config.getString("vote-won") ?: "Vote has been won! ",
                voteFailed = config.getString("vote-failed") ?: "Vote has been failed! ",
                voteFor = config.getString("vote-for") ?: "[FOR]",
                voteAgainst = config.getString("vote-against") ?: "[AGAINST]",
                alreadyMadeChoice = config.getString("already-made-choice") ?: "You already make your choice!",
                youMadeChoice = config.getString("you-made-choice") ?: "You make your choice! ",
                newVoteStarted = config.getString("new-vote-started") ?: "New vote has been started! ",
                votesFor = config.getString("votes-for") ?: " votes for: ",
                twoMinutesRemaining = config.getString("two-minutes-remaining") ?: "2 minutes remaining!",
                sixtySecondsRemaining = config.getString("sixty-seconds-remaining") ?: "60 seconds remaining!",
                thirtySecondsRemaining = config.getString("thirty-seconds-remaining") ?: "30 seconds remaining!",
                tenSecondsRemaining = config.getString("ten-seconds-remaining") ?: "10 seconds remaining!",
                cannotStartVote = config.getString("cannot-start-vote") ?: "You cannot start a vote right now!",
                cannotStartVoteNotEnoughPlayers = config.getString("cannot-start-vote") ?: "Not enough players to vote!"
            )
        }
    }
}

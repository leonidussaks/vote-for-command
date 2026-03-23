## About

**VoteForCommand** allows players to vote for server commands execution.

This easy-to-use plugin can liven up the atmosphere on your server, as it allows you to add commands of any complexity.

![img](https://i.imgur.com/ZkeoX4h.png)

## Configuration

You can fully customize voting options in the config:

```yaml
votingCommands:
  freeDiamond:
    name: "FREE DIAMOND!!" # Display name in menu and chat
    description: "Give a random player diamond!" # Description shown to players
    material: "DIAMOND" # Item used as icon in menu
    commands: # Commands executed if vote passes
      - "execute as @r run give @s minecraft:diamond 1"
    cooldown: 3600 # Cooldown in seconds before this vote can be started again
    percentToWin: 50.0 # Required % of FOR votes to pass
    minimumOnline: 5 # Minimum players online to start vote
  heal:
    name: "Sacred blessing"
    description: "Sacred blessing of the goddess of health. All players will receive regeneration."
    material: "GOLDEN_APPLE"
    commands:
      - "execute as @a at @s run particle minecraft:heart ~ ~1 ~ 0.5 1 0.5 0.1 10"
      - "effect give @a minecraft:glowing 5 0 true"
      - "effect give @a minecraft:regeneration 120 1 true"
    cooldown: 3600
    percentToWin: 70.0
    minimumOnline: 10
```

## Permissions and commands

```yaml
commands:
  vote:
    description: "Open vote menu."
    usage: "/vote"
    permission: "voteforcommand.vote.use"
  reload:
    description: "Reload plugin config."
    usage: "/reload"
    permission: "voteforcommand.reload.use"
    aliases:
      - votereload
permissions:
  voteforcommand.vote.use:
    description: "Allow player to use /vote command (open menu)."
    default: true
  voteforcommand.vote.participant:
    description: "Allow player to click [FOR] and [AGAINST] buttons in chat."
    default: true
  voteforcommand.reload.use:
    description: "Allow admin to use /reload command for plugin."
```

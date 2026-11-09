# CyberQuests

CyberQuests is a Paper 26.2 quest plugin for the Cyber network.

## Requirements

- Paper 26.2
- Java 25 on the server
- CyberCoins installed (required dependency)

## Menus

- `/quests` opens the hub: **Tutorial** in slot 11 and **Quest Board** in slot 15. The Quest Board is locked (and says so
  in its lore) until the Tutorial is finished; clicking it while locked sends a message instead.
- Tutorial menu (54 slots, also opened directly with `/tutorial`): info book in slot 4, the steps in slots 10-16, 19-25
  and 28-34 with the Quest Board goal straight after the last step, and a Back button in slot 49. Completed steps are
  lime glass, the current step shows its item with progress, later steps are gray glass.
- Active Quests list (Quest Board): a Back button in slot 31 returns to the hub.

## Tutorial (progression quests)

Every player, including existing ones, starts here. Steps are completed one at a time, in order, and each gives a small
themed item reward (never coins). Rewards that do not fit in the inventory are dropped at the player's feet.
Completion messages are only sent to that player, e.g. `QUESTS » You completed quest "Break 16 logs" and received 2 Apples.`

| # | Quest | Reward |
|---|---|---|
| 1 | Do /tutorial | 3 Bread |
| 2 | Do /rtp | 1 Compass |
| 3 | Break your first log (any log) | 3 Oak Logs |
| 4 | Break 16 logs | 2 Apples |
| 5 | Craft a Crafting Table | 8 Sticks |
| 6 | Do /homes | 1 White Bed |
| 7 | Craft a Wooden Pickaxe | 10 Cobblestone |
| 8 | Mine 16 Stone (or Deepslate) | 3 Bread |
| 9 | Craft a Stone Pickaxe | 4 Torches |
| 10 | Mine 8 Coal Ore | 8 Cobblestone |
| 11 | Do /shop | 1 Chest |
| 12 | Craft a Furnace | 4 Coal |
| 13 | Cook 4 food (any cooked meat or fish) | 1 Smoker |
| 14 | Do /sell | 1 Barrel |
| 15 | Kill 5 hostile mobs (zombie/skeleton/spider/creeper variants) | 8 Arrows |
| 16 | Do /coins | 3 Gold Nuggets |
| 17 | Mine 6 Iron Ore | 8 Coal |
| 18 | Smelt 6 Iron Ingots | 1 Shield |
| 19 | Craft an Iron Pickaxe | 16 Torches |
| 20 | Do /spawn | 3 Steak + Quest Board unlocked |

Command quests (`RUN_COMMAND`) detect any typed command by its label, including commands from other plugins
(/rtp, /shop and /sell are not CyberMC commands). Aliases count as their main command: add the server's aliases
to `Aliases.java` (e.g. `/sellall` -> `/sell`). `plugin:command` forms count too, and a command still counts if
another plugin cancels it.

A permanent green boss bar (no `QUESTS »` prefix) shows the current step and its progress from the player's first join
until the Tutorial is finished. After each step, the action bar shows overall progress towards unlocking the Quest Board.

While the Tutorial is unfinished, no Quest Board quests are assigned, progressed or refilled after cooldowns.
Player-placed logs, stone and ores do not count, just like on the Quest Board.

## Quest Board

- Every player has 4 independent quest slots. There are exactly two difficulties: **Normal** and **Easy**.
- New quests are Normal quests and reward 10 coins.
- A Normal quest can be changed to Easy for 2 coins (Easify). Easy quests reward 5 coins and can never be Easified again.
- Easify uses a confirmation menu with Cancel in slot 10, the quest display in slot 13, and Confirm in slot 16.
- **Slot cooldowns:** completing a quest puts only that slot on a 5-minute cooldown. A barrier with the remaining time is
  shown in its place, its Easify button is hidden, and a new Normal quest is assigned automatically (with a chat message)
  when the cooldown ends. Cooldowns are stored per player and per slot in `players.yml` and survive restarts.
- **Mining quests** only count naturally occurring blocks. Blocks placed by players are tracked in chunk persistent data
  (survives restarts) and are ignored when broken. Tracking follows pistons, falling blocks and explosions.
- Ore quests count both the regular and the deepslate variant.
- Farming quests count fully grown crops only (replanting is fine).
- Smelting quests count items taken out of a furnace, smoker or blast furnace.

## Coins

Rewards and the Easify cost (with balance check) use CyberCoins directly.

## Commands

- `/quests` (aliases `/q`, `/missions`, `/tasks`)
- `/tutorial` — opens the Tutorial quests directly
- `/quests reload` (admin)
- `/quests reset <player>` (admin) — wipes all quest data, including the Tutorial
- `/quests give <player> <quest>` (admin) — Quest Board only, so the player must have finished the Tutorial
- `/quests tutorial <player> <next|finish|reset>` (admin) — skip the current step (no reward), finish the whole Tutorial
  (unlocks the Quest Board), or send the player back to step 1

Admin permission: `cyber.quests.admin`

## Configuration

- `Messages.java` — GUI names, lore, colours and messages.
- `Quests.java` — Quest Board quest definitions.
- `Tutorial.java` — Tutorial steps, icons and rewards.
- `Aliases.java` — command aliases that count for command quests (e.g. `/wild` for `/rtp`).
- `players.yml` — generated persistent player quest data (Tutorial step/progress, slots and cooldown timestamps).

## Building

Use Java 25 and Maven:

`mvn clean package`

The compiled plugin will be created as `target/CyberQuests-1.0.jar`.

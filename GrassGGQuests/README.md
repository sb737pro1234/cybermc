# GrassGGQuests

GrassGGQuests is a Paper 26.2 quest plugin for the GrassGG network.

## Requirements

- Paper 26.2
- Java 25 on the server
- GrassGGCoins installed (required dependency)

## Quest system

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
- The Main menu has Active Quests in slot 11 and Coin Shop in slot 15.

## Coins

Rewards, the Easify cost (with balance check) and the Coin Shop use GrassGGCoins directly.

## Commands

- `/quests`
- `/quest`
- `/q`
- `/quests reload` (admin)
- `/quests reset <player>` (admin)
- `/quests give <player> <quest>` (admin)

Admin permission: `grassggquests.admin`

## Configuration

- `Messages.java` — GUI names, lore, colours and messages.
- `Quests.java` — quest definitions.
- `players.yml` — generated persistent player quest data (slots and cooldown timestamps).

## Building

Use Java 25 and Maven:

`mvn clean package`

The compiled plugin will be created as `target/GrassGGQuests-1.0.jar`.

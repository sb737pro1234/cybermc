# CyberMastery

CyberMastery is a progression plugin for Paper 26.2. Every "type" (a block, mob, crop, wood or fish) has its own level from 0 to 45, spread over five categories.

## Current functionality

- Five categories: **Blocks**, **Slaying**, **Farming**, **Foraging** and **Fishing**.
- Every type levels from 0 to 45. Variants fold into their parent type (e.g. stone brick stairs, slabs and walls all count as Stone Bricks; deepslate ores count as the normal ore).
- Blocks types are sorted into groups: Stones, Ores, Dirt & Sand, Nether, End, Building and Wood Blocks.
- Clean level costs: level `n` costs `unit x shape(n)` where shape is 1, 2, 4, 6, 8 ... (+2 per level). Level 45 always totals `1981 x unit`.
  - Blocks unit 20 (39,620 total), Foraging 15 (29,715), Farming 25 (49,525), Slaying and Fishing 5 (9,905).
- Rarer types give more points per action (e.g. Stone 1, Diamond Ore 25, Ancient Debris 100, Warden 1000).
- Points stay behind the scenes. Menus show actions instead, worked out from the points: e.g. `Mine Stone: 5/15` (done this level / needed this level), `Kill Zombies: 3/10`, `Catch Salmon: 1/4`. Each of the 45 levels shows the total needed to reach it (e.g. `Mine Stone: 260/420`). Counts round up, so they always add up.
- Total Mastery: every type level added up (e.g. 44/5,490).
- `/mastery` (alias `/m`) main menu, category menus, Blocks group menus and a type menu showing all 45 levels.
- Level rewards are claimed in the type menu (chest minecart = ready, minecart = claimed, barrier = not reached), or with **Claim All** in the type menu or the main menu. Levelling up never gives rewards automatically, so nothing is lost by claiming later.
- The whole system is explained in-game: How Mastery Works (main menu), How [Category] Works (each category menu), and every type's lore.
- Level-up feedback: action bar every level (`MASTERY » Reached Stone 6!`), title + sound every 5th level, server-wide announcement at level 45. Several level-ups from one action only show the highest.
- `/mastery top` (or `/masterytop`, or the Top Mastery head in `/mastery`) opens a leaderboard laid out like `/coinstop`: the top 45 players by total levels as player heads, and your own head and position in the centre of the bottom row. Offline players are included (from `totals.yml`).
- `/mastery reload` reloads `config.yml` and `mastery.yml` (`cyber.mastery.reload`, default op). Player progress is kept.
- Admin / testing commands (`cyber.mastery.admin`, default op, online players only):
  - `/mastery give <player> <type> <levels>`: gives whole levels (landing at the start of the new level) with action bars, titles, sounds and the level 45 broadcast.
  - `/mastery set <player> <type> <level>`: sets a type to the start of a level, silently. Claims above the new level are forgotten.
  - `/mastery reset <player> [type]`: resets one type, or every type, including claimed rewards.
  - `/mastery check`: shows whether the block you're looking at is tracked, player-placed, and has already given placement points.
  - Types use their ids from `mastery.yml` (e.g. `stone`, `diamond_ore`, `zombie`) and tab-complete.
- All player-facing messages, titles, colours and sounds are in `Messages.java` (`#00AAFF` and white).

## How progress is earned

| Category | Points from |
| --- | --- |
| Blocks | Mining natural blocks. Placing gives 25% once per position, ever, with an hourly cap per type. |
| Slaying | Killing adult mobs (spawner mobs count, babies and players don't). |
| Farming | Breaking fully grown crops. Sugar cane, cactus, bamboo and kelp count every grown piece, never the placed base. Sweet berries count when picked or broken fully grown. |
| Foraging | Mining natural logs, stems, wood and hyphae (trees from saplings count). Oak, spruce, birch, jungle, acacia, dark oak, mangrove, cherry, pale oak, crimson and warped. Wood Blocks (planks) cover the same woods plus bamboo. |
| Fishing | Catching cod, salmon, pufferfish and tropical fish. Treasure and junk don't count. |

### Anti-exploit

- Player-placed block positions are stored in each chunk's PersistentDataContainer (no database). Mining a placed block gives nothing.
- Placement points are given once per position, ever, so placing and breaking in the same spot can't be farmed.
- Flags follow blocks moved by pistons and falling sand/gravel, and are cleaned up when blocks explode, burn, wash away, break from physics or are taken by mobs.
- Blocks made by lava and water (stone and basalt generators) count as natural, so generators give points. Cobblestone isn't a type at all (there's almost none naturally), so cobblestone generators give nothing. Placed concrete powder that hardens stays "placed".
- Creative-mode players earn nothing (configurable).
- Only blocks of tracked types are flagged, so normal building doesn't fill chunk data.

## Files

- `config.yml`: creative mode, autosave interval, level 45 broadcast, placement percent and hourly cap.
- `mastery.yml`: categories, units, groups, types, points per action and the materials / mobs each type includes (`*` wildcards supported, e.g. `*_WOOL`). A summary of types per group is printed in the console on first run.
- `data/<uuid>.yml`: each player's progress (stored in quarter-points) and claimed levels. Loaded before join, autosaved every 5 minutes, saved on quit and on shutdown. Writes happen asynchronously.
- `totals.yml`: every player's total levels and levels per category, so placeholders work offline.

## PlaceholderAPI

PlaceholderAPI is optional. If installed, the internal expansion is registered automatically. All placeholders return plain numbers and work for offline players.

| Placeholder | Returns |
| --- | --- |
| `%cybermastery_total%` | Total levels: every type level added up |
| `%cybermastery_total_max%` | Highest possible total levels (types x 45) |
| `%cybermastery_blocks%`, `_slaying`, `_farming`, `_foraging`, `_fishing` | Levels in that category |
| `%cybermastery_level_<type>%` | A single type's level, e.g. `%cybermastery_level_stone%` |

## Rewards (for later)

Rewards are not added yet ("Coming soon"). To add them, implement `RewardHook` (`onClaim` gives the reward, `describe` sets the "Reward:" line) and call `CyberMastery#setRewardHook`. For level-up reactions, listen to `MasteryLevelUpEvent`.

## Build

Open the project in IntelliJ IDEA and import the Maven project from `pom.xml`.
The project targets Java 25 and Paper 26.2. `mvn test` runs the level table unit tests.

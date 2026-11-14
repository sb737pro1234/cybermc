# CyberKits

CyberKits is a Paper 26.2 rank kit plugin for the Cyber network.

## Kits

| Kit | Permission | Cooldown | Menu slot |
|---|---|---|---|
| Starter (Member) | `cyber.kits.starter` (default: everyone) | 12h | 10 |
| Jester | `cyber.kits.jester` | 24h | 12 |
| Squire | `cyber.kits.squire` | 48h | 14 |
| Bishop | `cyber.kits.bishop` | 72h | 16 |
| Knight | `cyber.kits.knight` | 96h | 28 |
| Lord | `cyber.kits.lord` | 120h | 30 |
| King | `cyber.kits.king` | 144h | 32 |
| Cyber | `cyber.kits.cyber` | 168h | 34 |

- Kits are named with the rank prefix (gradient) followed by " Kit" in the prefix's last hex colour.
- On a player's **first join**, the Starter kit's items go straight into their inventory (no note), and **every** kit is
  locked for its full cooldown. For example, Cyber first unlocks 168h after a player first joins, even if they have the rank.
- Each kit only checks its own permission. To let higher ranks claim lower kits, use LuckPerms group inheritance.

## Menu (`/kits`)

The menu has 45 slots. Each kit is a minecart that shows its state:

- **Chest minecart**: claimable. Left-click claims a Kit Note.
- **Minecart**: claimed. The lore shows the time left, counting down live.
- **Furnace minecart**: locked. The lore says the player doesn't have permission and names the rank needed.

Right-click any kit to preview its items (read-only, with a Back button).

## Kit Notes

Claiming a kit gives a **Kit Note** (paper) instead of the items. Right-click the note to get the kit's items. Notes can
be traded or given to other players, and anyone holding one can open it.

- It works the same way as CyberCoins bank notes: each note has a unique id, and used ids are saved to `kitnotes.yml`
  before the items are given, so duplicated copies of a note only work once.
- Claiming needs one free slot for the note. Opening a note needs room for all its items, otherwise it isn't used up.
- A note gives the kit's items as they are when it's opened, not as they were when it was claimed.
- Kit Notes can't be used in crafting.

## Commands

- `/kits` (alias `/kit`): opens the kits menu
- `/kits editor [kit]` (admin): pick a kit (or go straight to one), place the items, then close the menu to save them to `kititems.yml`
- `/kits reload` (admin): re-reads `kititems.yml` and `cooldowns.yml`
- `/kits cooldown reset <player> [kit]` (admin): clears the cooldown (all kits if no kit is given)
- `/kits cooldown lock <player> [kit]` (admin): restarts a full cooldown (all kits if no kit is given)

Admin permission: `cyber.kits.admin`

## Configuration

- `Kits.java`: kit ids, names, rank prefixes (`&#RRGGBB` / `&` codes), menu slots, cooldowns, menu size and the
  first-join kit. If you add or rename a kit, add its `cyber.kits.<id>` permission to `plugin.yml`.
- `Messages.java`: GUI names, lore, colours, sounds and messages.
- `kititems.yml`: generated. The items in each kit, set with `/kits editor`.
- `cooldowns.yml`: generated. When each player can next claim each kit.
- `kitnotes.yml`: generated. The ids of used Kit Notes.

## Building

Use Java 25 and Maven:

`mvn clean package`

The compiled plugin will be created as `target/CyberKits-1.0.jar`.

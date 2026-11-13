# CyberKits

CyberKits is a Paper 26.2 rank kit plugin for the Cyber network.

## Kits

| Kit | Permission | Cooldown | Menu slot |
|---|---|---|---|
| Starter (Member) | `cyber.kits.starter` (default: everyone) | 12h | 10 |
| Jester | `cyber.kits.jester` | 24h | 12 |
| Squire | `cyber.kits.squire` | 48h | 14 |
| Bishop | `cyber.kits.bishop` | 72h | 16 |
| Lord | `cyber.kits.lord` | 96h | 19 |
| Knight | `cyber.kits.knight` | 120h | 21 |
| King | `cyber.kits.king` | 144h | 23 |
| Cyber | `cyber.kits.cyber` | 168h | 25 |

- **Starter** is given automatically on a player's first join, and its 12h cooldown starts straight away.
- Each kit only checks its own permission. To let higher ranks claim lower kits, use LuckPerms group inheritance.

## Menu (`/kits`)

Each kit is a minecart that shows its state:

- **Chest minecart**: claimable. Left-click claims it.
- **Minecart**: claimed. The lore shows the time left, counting down live.
- **Furnace minecart**: locked. The lore says the player doesn't have permission and names the rank needed.

Right-click any kit to preview its items (read-only, with a Back button). If the kit doesn't fit in the inventory, the claim is
refused and no cooldown starts.

## Commands

- `/kits` (alias `/kit`): opens the kits menu
- `/kits editor [kit]` (admin): pick a kit (or go straight to one), place the items, then close the menu to save them to `kititems.yml`
- `/kits reload` (admin): re-reads `kititems.yml` and `cooldowns.yml`
- `/kits cooldown reset <player> [kit]` (admin): clears the cooldown (all kits if no kit is given)
- `/kits cooldown lock <player> [kit]` (admin): restarts a full cooldown (all kits if no kit is given)

Admin permission: `cyber.kits.admin`

## Configuration

- `Kits.java`: kit ids, display names, colours, menu slots, cooldowns, menu size and the first-join kit. If you add or
  rename a kit, add its `cyber.kits.<id>` permission to `plugin.yml`.
- `Messages.java`: GUI names, lore, colours, sounds and messages.
- `kititems.yml`: generated. The items in each kit, set with `/kits editor`.
- `cooldowns.yml`: generated. When each player can next claim each kit.

## Building

Use Java 25 and Maven:

`mvn clean package`

The compiled plugin will be created as `target/CyberKits-1.0.jar`.

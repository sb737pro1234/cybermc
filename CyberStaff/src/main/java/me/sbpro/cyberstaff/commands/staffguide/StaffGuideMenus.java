package me.sbpro.cyberstaff.commands.staffguide;

import me.sbpro.cyberstaff.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds every inventory used by /staffguide.
 *
 * Colour scheme: CUSTOM = accent colour (commands, step numbers, headers), white = main text.
 * Inline tokens inside text: "<c>" switches to the accent colour, "<r>" switches back to white.
 */
public class StaffGuideMenus {

    // Main menu slots
    public static final int SLOT_PROTOCOL = 11;
    public static final int SLOT_COMMANDS = 13;
    public static final int SLOT_RULES = 15;

    // Back button (bottom row, middle of the 36-slot sub-menus)
    public static final int SLOT_BACK = 31;

    private static final int WIDTH = 64;  // max visible characters per lore line

    // ------------------------------------------------------------------ MAIN

    public static void openMain(Player player) {
        Inventory inv = createMenu(StaffGuideHolder.Type.MAIN, 27, Messages.STAFF_GUIDE_TITLE);

        inv.setItem(SLOT_PROTOCOL, item(Material.BELL, "Protocol",
                wrap("To open the staff protocol, Click here.", Component.empty(), Component.empty(), 0)));
        inv.setItem(SLOT_COMMANDS, item(Material.COMMAND_BLOCK, "Commands",
                wrap("To open the staff commands, Click here.", Component.empty(), Component.empty(), 0)));
        inv.setItem(SLOT_RULES, item(Material.BOOK, "Rules",
                wrap("To open the rules document, Click here.", Component.empty(), Component.empty(), 0)));

        player.openInventory(inv);
    }

    // -------------------------------------------------------------- PROTOCOL

    public static void openProtocol(Player player) {
        Inventory inv = createMenu(StaffGuideHolder.Type.PROTOCOL, 36, Messages.STAFF_PROTOCOL_TITLE);

        // 1) Standard protocol
        List<Component> standard = new ArrayList<>();
        standard.addAll(wrap("<c>If you strongly believe a player is breaking the rules:<r>", Component.empty(), Component.empty(), 0));
        standard.add(Component.text(" "));
        step(standard, 1, "Start recording as soon as possible.");
        step(standard, 2, "Add them to the sus list. (<c>/sus add [PLAYER] [REASON]<r>)");
        step(standard, 3, "Gather as much evidence as you can, screenshots of the area, inventory etc.");
        step(standard, 4, "Punish them with the correct offence. (<c>/offend [PLAYER] [REASON]<r>) "
                + "If there is not an appropriate offence, use a punishment as similar as possible "
                + "(<c>/offence list<r>) and notify a higher member of staff.");
        step(standard, 5, "Go to <c>#create-a-punishment-log<r> in the Discord, fill in the requirements "
                + "and post your evidence there with the duration of the punishment given. "
                + "If only a warning was given, select \"none\" for the duration option.");
        inv.setItem(11, item(Material.WRITABLE_BOOK, "Standard Protocol", standard));

        // 2) Unsure
        List<Component> unsure = new ArrayList<>();
        unsure.addAll(wrap("<c>If you are unsure whether a player is breaking the rules:<r>", Component.empty(), Component.empty(), 0));
        unsure.add(Component.text(" "));
        step(unsure, 1, "Add a suspicion note. (<c>/sus add [PLAYER] [REASON]<r>)");
        step(unsure, 2, "Talk to another member of staff in <c>/staffchat<r> or in <c>#staff-chat<r> on Discord.");
        step(unsure, 3, "If still unsure, talk to a higher ranking/more experienced member of staff.");
        inv.setItem(13, item(Material.ENCHANTED_BOOK, "Unsure Protocol", unsure));

        // 3) Appeals
        List<Component> appeal = new ArrayList<>();
        appeal.addAll(wrap("<c>If a player appeals a punishment:<r>", Component.empty(), Component.empty(), 0));
        appeal.add(Component.text(" "));
        step(appeal, 1, "Try to reason with them, share the evidence, maybe get another staff member "
                + "involved to see from their perspective.");
        step(appeal, 2, "If you/another staff member agree with the player, there is not enough evidence "
                + "or the punishment is not valid for any other reason, you can remove the current "
                + "punishment using <c>/offence remove [PLAYER]<r>.");
        step(appeal, 3, "If you still cannot come to an agreement, ping a higher ranking/more experienced "
                + "member of staff, they will sort from there.");
        inv.setItem(15, item(Material.KNOWLEDGE_BOOK, "Appeal Protocol", appeal));

        inv.setItem(SLOT_BACK, backButton());
        player.openInventory(inv);
    }

    // -------------------------------------------------------------- COMMANDS

    public static void openCommands(Player player) {
        Inventory inv = createMenu(StaffGuideHolder.Type.COMMANDS, 36, Messages.STAFF_COMMANDS_TITLE);

        // Offences
        List<Component> offences = new ArrayList<>();
        cmd(offences, "/offend [PLAYER] [REASON]",
                "Automatically gives the specified player the correct offence/time for the reason given.");
        cmd(offences, "/offence change [PLAYER] [NEW-REASON]",
                "Changes the offence that the specified player currently has active.");
        cmd(offences, "/offence check [PLAYER]",
                "Lists the specified player's current offences, if any.");
        cmd(offences, "/offence list",
                "Lists every punishment and its category.");
        cmd(offences, "/offence remove [PLAYER]",
                "Removes active punishment from specified player.");
        cmd(offences, "/offence reset [PLAYER]",
                "Resets the specified player's offence history.");
        inv.setItem(11, item(Material.BARRIER, "Offences", offences));

        // Sus list
        List<Component> sus = new ArrayList<>();
        cmd(sus, "/sus",
                "Opens a list of people with sus notes (people that staff think may be breaking the rules).");
        cmd(sus, "/sus add [PLAYER] [REASON]",
                "Adds a sus note for the specified player for the specified reason.");
        inv.setItem(12, item(Material.SPYGLASS, "Sus List", sus));

        // Chat
        List<Component> chat = new ArrayList<>();
        cmd(chat, "/staffchat",
                "Toggles the chat which can only be viewed by staff members.");
        cmd(chat, "/clearchat",
                "Clears the global chat.");
        inv.setItem(13, item(Material.PAPER, "Chat", chat));

        // SProtect
        List<Component> sprotect = new ArrayList<>();
        cmd(sprotect, "/sprotect inspect",
                "Enables inspect mode where any block you click/place will display the history of players that have interacted with that block.");
        cmd(sprotect, "/sprotect lookup [ARGS]",
                "Looks up logs within the specified arguments.");
        cmd(sprotect, "/sprotect restore [ARGS]",
                "Restores the area within the specified arguments. The first run is only a preview, "
                        + "use <c>/sprotect restore confirm<r> to apply it.");
        sprotect.add(Messages.guideArguments());
        arg(sprotect, "action:", "a:", "Filter by action type(s).", "a:block_break,block_place");
        arg(sprotect, "time:", "t:", "Time range or duration.", "t:1h, t:30m, t:2d");
        arg(sprotect, "radius:", "r:", "Distance from your location.", "r:10, r:50");
        arg(sprotect, "user:", "u:", "Player name(s) or special entities.", "u:PlayerName, u:=natural");
        arg(sprotect, "page:", "p:", "Page number and results per page.", "p:1, p:2-20");
        arg(sprotect, "include:", "i:", "Include only specific materials.", "i:diamond,emerald");
        arg(sprotect, "exclude:", "e:", "Exclude specific materials.", "e:dirt,stone");
        arg(sprotect, "world:", "w:", "Filter by world name.", "w:world_nether");
        sprotect.addAll(wrap("<c>Example:<r> /sprotect lookup u:Notch t:24h r:10 a:block_break", Component.empty(), INDENT_2, 0));
        inv.setItem(14, item(Material.GRASS_BLOCK, "SProtect", sprotect));

        // Other
        List<Component> other = new ArrayList<>();
        cmd(other, "/inventoryrollback restore [PLAYER]",
                "Opens a menu where you can rollback and view logs of specified player's inventories. "
                        + "Logs are taken on player join, player leave, death and world changes.");
        cmd(other, "/homes view [PLAYER]",
                "Opens a menu where you can teleport to specified player's homes.");
        cmd(other, "/sign",
                "Signs the item in your hand. (Adds lore)");
        inv.setItem(15, item(Material.CHEST, "Other", other));

        inv.setItem(SLOT_BACK, backButton());
        player.openInventory(inv);
    }

    // --------------------------------------------------------------- HELPERS

    private static final Component INDENT_2 = Component.text("  ");
    private static final Component INDENT_3 = Component.text("   ");

    private static Inventory createMenu(StaffGuideHolder.Type type, int size, Component title) {
        StaffGuideHolder holder = new StaffGuideHolder(type);
        Inventory inventory = Bukkit.createInventory(holder, size, title);
        holder.setInventory(inventory);
        return inventory;
    }

    private static ItemStack backButton() {
        return item(Material.ARROW, "Back", wrap("Return to the Staff Guide.", Component.empty(), Component.empty(), 0));
    }

    private static ItemStack item(Material material, String name, List<Component> lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            // Remove trailing blank lines
            while (!lore.isEmpty() && PlainTextComponentSerializer.plainText()
                    .serialize(lore.get(lore.size() - 1)).isBlank()) {
                lore.remove(lore.size() - 1);
            }
            meta.displayName(Messages.guideItemName(name));
            meta.lore(lore.stream().map(line -> line.decoration(TextDecoration.ITALIC, false)).toList());
            stack.setItemMeta(meta);
        }
        return stack;
    }

    /** Numbered step: number in the accent colour, text in white, wrapped lines indented under the text. */
    private static void step(List<Component> lore, int number, String text) {
        lore.addAll(wrap(text, Messages.guideStepNumber(number), INDENT_3, 3));
    }

    /** Command in the accent colour on one line, description in white underneath, then a blank line. */
    private static void cmd(List<Component> lore, String command, String description) {
        lore.add(Messages.guideCommand(command));
        lore.addAll(wrap(description, INDENT_2, INDENT_2, 2));
        lore.add(Component.text(" "));
    }

    /** Argument entry on one wrapped line: name (alias) highlighted, description in white, example highlighted. */
    private static void arg(List<Component> lore, String name, String alias, String description, String example) {
        lore.addAll(wrap("<c>" + name + "<r> (<c>" + alias + "<r>) - " + description
                + " e.g. <c>" + example + "<r>", Component.text(" "), INDENT_3, 1));
    }

    /**
     * Word-wraps text into lore lines. Honors the inline tokens "<c>" (accent) and "<r>" (white),
     * and carries the active colour over to wrapped lines.
     *
     * @param firstPrefix prefix for the first line (not counted in width)
     * @param contPrefix  prefix for following lines (not counted in width)
     * @param indent      visible width of the prefixes, counted towards WIDTH
     */
    private static List<Component> wrap(String text, Component firstPrefix, Component contPrefix, int indent) {
        List<Component> out = new ArrayList<>();
        boolean highlighted = false;
        Component line = firstPrefix;
        int len = indent;

        for (String word : text.split(" ")) {
            int visible = word.replace("<c>", "").replace("<r>", "").length();

            if (len > indent && len + 1 + visible > WIDTH) {
                out.add(line);
                line = contPrefix;
                len = indent;
            }
            if (len > indent) {
                line = line.append(guideText(" ", highlighted));
                len++;
            }

            // A word may switch colour in the middle of itself, e.g. "(<c>/sus add<r>)".
            int position = 0;
            boolean active = highlighted;
            while (position < word.length()) {
                int nextC = word.indexOf("<c>", position);
                int nextR = word.indexOf("<r>", position);
                int next = nextC < 0 ? nextR : (nextR < 0 ? nextC : Math.min(nextC, nextR));
                if (next < 0) {
                    line = line.append(guideText(word.substring(position), active));
                    break;
                }
                if (next > position) {
                    line = line.append(guideText(word.substring(position, next), active));
                }
                active = next == nextC;
                position = next + 3;
            }
            highlighted = active;
            len += visible;
        }
        out.add(line);
        return out;
    }

    private static Component guideText(String text, boolean highlighted) {
        return Component.text(text, highlighted ? Messages.CUSTOM : NamedTextColor.WHITE);
    }
}

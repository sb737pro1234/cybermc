package me.sbpro.cyber.commands.rules;

import me.sbpro.cyber.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.util.List;

/** The three sections of the /rules menu: where they sit, how they look and which document they open. */
public enum RulesCategory {

    DISCORD(11, Material.PURPLE_SHULKER_BOX, "Discord", Messages.DISCORD_RULES_COLOR,
            Messages.DISCORD_RULES_NAME, Messages.DISCORD_RULES_LORE, RulesCategory.DISCORD_RULES_URL),

    CHAT(13, Material.ORANGE_SHULKER_BOX, "Chat", Messages.CHAT_RULES_COLOR,
            Messages.CHAT_RULES_NAME, Messages.CHAT_RULES_LORE, RulesCategory.MINECRAFT_RULES_URL),

    GAME(15, Material.LIGHT_BLUE_SHULKER_BOX, "Game", Messages.GAME_RULES_COLOR,
            Messages.GAME_RULES_NAME, Messages.GAME_RULES_LORE, RulesCategory.MINECRAFT_RULES_URL);

    public static final String DISCORD_RULES_URL =
            "https://docs.google.com/document/d/1TycsTCoTMQZDgXWg3CfOPZVCqr2qLok7d7-bfTKF0bs/edit?usp=sharing";

    public static final String MINECRAFT_RULES_URL =
            "https://docs.google.com/document/d/1wZWo1C1_vQj826vAjiInfvQced7srQfRcTJrb9H-BoQ/edit?usp=sharing";

    private final int slot;
    private final Material material;
    private final Component name;
    private final List<Component> lore;
    private final Component link;

    RulesCategory(int slot, Material material, String document, TextColor color,
                  Component name, List<Component> lore, String url) {
        this.slot = slot;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.link = Messages.rulesLink(document, color).clickEvent(ClickEvent.openUrl(url));
    }

    public int slot() {
        return slot;
    }

    public Material material() {
        return material;
    }

    public Component displayName() {
        return name;
    }

    public List<Component> lore() {
        return lore;
    }

    public Component link() {
        return link;
    }

    public static RulesCategory fromSlot(int slot) {
        for (RulesCategory category : values()) {
            if (category.slot == slot) {
                return category;
            }
        }
        return null;
    }
}

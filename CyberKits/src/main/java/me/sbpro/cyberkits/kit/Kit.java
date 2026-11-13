package me.sbpro.cyberkits.kit;

import java.time.Duration;

import net.kyori.adventure.text.format.TextColor;

/**
 * A kit definition (see Kits.java).
 * @param id identifier, used in commands, kititems.yml, cooldowns.yml and the permission cyber.kits.&lt;id&gt;
 * @param name display name shown in menus and messages
 * @param color colour of the display name
 * @param slot position in the /kits menu
 * @param cooldown time between claims
 */
public record Kit(String id, String name, TextColor color, int slot, Duration cooldown) {

    public String permission() {
        return "cyber.kits." + id;
    }
}

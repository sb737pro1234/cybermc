package me.sbpro.cyberchat.chatcolor;

import net.kyori.adventure.text.format.TextColor;

public enum PlayerChatColor {

    WHITE(
            "White",
            "cyber.chatcolor.white",
            TextColor.color(0xFFFFFF)
    ),

    GRAY(
            "Gray",
            "cyber.chatcolor.gray",
            TextColor.color(0xAAAAAA)
    ),

    BLACK(
            "Black",
            "cyber.chatcolor.black",
            TextColor.color(0x000000)
    ),

    RED(
            "Red",
            "cyber.chatcolor.red",
            TextColor.color(0xFF5555)
    ),

    ORANGE(
            "Orange",
            "cyber.chatcolor.orange",
            TextColor.color(0xFFAA00)
    ),

    YELLOW(
            "Yellow",
            "cyber.chatcolor.yellow",
            TextColor.color(0xFFFF55)
    ),

    LIME(
            "Lime",
            "cyber.chatcolor.lime",
            TextColor.color(0x55FF55)
    ),

    GREEN(
            "Green",
            "cyber.chatcolor.green",
            TextColor.color(0x00AA00)
    ),

    AQUA(
            "Aqua",
            "cyber.chatcolor.aqua",
            TextColor.color(0x55FFFF)
    ),

    BLUE(
            "Blue",
            "cyber.chatcolor.blue",
            TextColor.color(0x5555FF)
    ),

    PURPLE(
            "Purple",
            "cyber.chatcolor.purple",
            TextColor.color(0xAA00AA)
    ),

    PINK(
            "Pink",
            "cyber.chatcolor.pink",
            TextColor.color(0xFF55FF)
    ),

    DARK_GRAY(
            "Dark Gray",
            "cyber.chatcolor.dark_gray",
            TextColor.color(0x555555)
    ),

    DARK_AQUA(
            "Dark Aqua",
            "cyber.chatcolor.dark_aqua",
            TextColor.color(0x00AAAA)
    );

    private final String display;
    private final String permission;
    private final TextColor color;

    PlayerChatColor(
            String display,
            String permission,
            TextColor color
    ) {
        this.display = display;
        this.permission = permission;
        this.color = color;
    }

    public String getDisplay() {
        return display;
    }

    public String getPermission() {
        return permission;
    }

    public TextColor getColor() {
        return color;
    }
}
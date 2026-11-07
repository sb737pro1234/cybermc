package me.sbpro.cybermastery.event;

import me.sbpro.cybermastery.model.MasteryType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called on the main thread when a player levels up a type.
 * If one action gives several levels, this is called once with the highest.
 */
public final class MasteryLevelUpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final MasteryType type;
    private final int oldLevel;
    private final int newLevel;

    public MasteryLevelUpEvent(Player player, MasteryType type, int oldLevel, int newLevel) {
        this.player = player;
        this.type = type;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public Player getPlayer() {
        return player;
    }

    public MasteryType getType() {
        return type;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

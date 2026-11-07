package me.sbpro.cybermastery.gui;

import me.sbpro.cybermastery.model.Group;

/** The types of one Blocks group. */
public final class GroupMenuHolder extends MasteryMenuHolder {

    private final Group group;
    private final int page;

    public GroupMenuHolder(Group group, int page) {
        this.group = group;
        this.page = page;
    }

    public Group group() {
        return group;
    }

    public int page() {
        return page;
    }
}

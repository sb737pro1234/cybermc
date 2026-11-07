package me.sbpro.cybermastery.gui;

import me.sbpro.cybermastery.model.MasteryType;

/** All 45 levels of one type. backPage is the page of the menu the player came from. */
public final class TypeMenuHolder extends MasteryMenuHolder {

    private final MasteryType type;
    private final int backPage;

    public TypeMenuHolder(MasteryType type, int backPage) {
        this.type = type;
        this.backPage = backPage;
    }

    public MasteryType type() {
        return type;
    }

    public int backPage() {
        return backPage;
    }
}

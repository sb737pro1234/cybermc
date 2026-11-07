package me.sbpro.cybermastery.gui;

import me.sbpro.cybermastery.model.Category;

/** A category's groups (Blocks) or types (every other category). */
public final class CategoryMenuHolder extends MasteryMenuHolder {

    private final Category category;
    private final int page;

    public CategoryMenuHolder(Category category, int page) {
        this.category = category;
        this.page = page;
    }

    public Category category() {
        return category;
    }

    public int page() {
        return page;
    }
}

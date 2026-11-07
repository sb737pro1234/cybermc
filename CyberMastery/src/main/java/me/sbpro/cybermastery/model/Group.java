package me.sbpro.cybermastery.model;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A Blocks group, e.g. Stones or Ores. */
public final class Group {

    private final String id;
    private final String name;
    private final Material icon;
    private final List<MasteryType> types = new ArrayList<>();

    public Group(String id, String name, Material icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Material icon() {
        return icon;
    }

    public List<MasteryType> types() {
        return Collections.unmodifiableList(types);
    }

    void addType(MasteryType type) {
        types.add(type);
    }
}

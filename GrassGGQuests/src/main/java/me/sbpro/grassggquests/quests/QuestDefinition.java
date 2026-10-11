package me.sbpro.grassggquests.quests;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class QuestDefinition {
    private final String id;
    private final String title;
    private final String description;
    private final QuestType type;
    private final Set<String> targets;
    private final int requiredAmount;

    public QuestDefinition(String id, String title, String description, QuestType type, String target, int requiredAmount) {
        this(id, title, description, type, List.of(target), requiredAmount);
    }

    /** Creates a quest that can be progressed by any of the given targets (for example normal and deepslate ore). */
    public QuestDefinition(String id, String title, String description, QuestType type, List<String> targets, int requiredAmount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        Set<String> upper = new LinkedHashSet<>();
        for (String target : targets) upper.add(target.toUpperCase(Locale.ROOT));
        this.targets = Set.copyOf(upper);
        this.requiredAmount = requiredAmount;
        this.firstTarget = upper.iterator().next();
    }

    private final String firstTarget;

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public QuestType getType() { return type; }
    /** The primary target (first target). */
    public String getTarget() { return firstTarget; }
    public Set<String> getTargets() { return targets; }
    public boolean matches(String target) { return target != null && targets.contains(target.toUpperCase(Locale.ROOT)); }
    public int getRequiredAmount() { return requiredAmount; }
}

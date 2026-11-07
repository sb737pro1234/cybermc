package me.sbpro.cybermastery.model;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Every category, group and type loaded from mastery.yml, plus the lookups the
 * listeners use (block material / entity type / fish item -> type).
 *
 * A registry is immutable once loaded; /mastery reload builds a new one.
 */
public final class TypeRegistry {

    private final Map<Category, LevelCurve> curves = new EnumMap<>(Category.class);
    private final Map<Category, List<MasteryType>> typesByCategory = new EnumMap<>(Category.class);
    private final List<Group> groups = new ArrayList<>();
    private final Map<String, MasteryType> byId = new LinkedHashMap<>();

    private final Map<Material, MasteryType> blockTypes = new EnumMap<>(Material.class);
    private final Map<EntityType, MasteryType> mobTypes = new HashMap<>();
    private final Map<Material, MasteryType> fishTypes = new EnumMap<>(Material.class);

    private TypeRegistry() {
    }

    // =============================================================
    // Loading
    // =============================================================

    /** Loads mastery.yml's "categories" section. Problems are logged as warnings. */
    public static TypeRegistry load(ConfigurationSection root, Logger logger) {
        TypeRegistry registry = new TypeRegistry();

        // Matchers are resolved after every type is known, so exact names beat wildcards.
        List<PendingMatch> pending = new ArrayList<>();

        for (Category category : Category.values()) {
            registry.typesByCategory.put(category, new ArrayList<>());

            ConfigurationSection section = root == null ? null : root.getConfigurationSection(category.key());

            if (section == null) {
                logger.warning("mastery.yml is missing the category '" + category.key() + "'.");
                registry.curves.put(category, new LevelCurve(1));
                continue;
            }

            long unit = section.getLong("unit", 1);
            if (unit <= 0) {
                logger.warning("Category '" + category.key() + "' has an invalid unit, using 1.");
                unit = 1;
            }

            LevelCurve curve = new LevelCurve(unit);
            if (!curve.isValid()) {
                logger.warning("Category '" + category.key() + "': level 45 total is " + curve.maxTotal()
                        + " but should be " + (LevelCurve.SHAPE_TOTAL * unit) + ".");
            }
            registry.curves.put(category, curve);

            if (category == Category.BLOCKS) {
                ConfigurationSection groupsSection = section.getConfigurationSection("groups");
                if (groupsSection == null) {
                    logger.warning("Category 'blocks' has no groups.");
                    continue;
                }

                for (String groupId : groupsSection.getKeys(false)) {
                    ConfigurationSection groupSection = groupsSection.getConfigurationSection(groupId);
                    if (groupSection == null) {
                        continue;
                    }

                    Group group = new Group(
                            groupId,
                            groupSection.getString("name", groupId),
                            material(groupSection.getString("icon"), Material.STONE, logger, groupId)
                    );

                    registry.groups.add(group);
                    registry.loadTypes(category, group, groupSection.getConfigurationSection("types"), pending, logger);
                }
            } else {
                registry.loadTypes(category, null, section.getConfigurationSection("types"), pending, logger);
            }
        }

        registry.resolveMatches(pending, logger);

        return registry;
    }

    private void loadTypes(
            Category category,
            Group group,
            ConfigurationSection typesSection,
            List<PendingMatch> pending,
            Logger logger
    ) {
        if (typesSection == null) {
            return;
        }

        for (String rawId : typesSection.getKeys(false)) {
            ConfigurationSection typeSection = typesSection.getConfigurationSection(rawId);
            if (typeSection == null) {
                continue;
            }

            String id = rawId.toLowerCase(Locale.ROOT);

            if (byId.containsKey(id)) {
                logger.warning("Duplicate type id '" + id + "' in mastery.yml, skipping the second one.");
                continue;
            }

            long points = typeSection.getLong("points", 1);
            if (points <= 0) {
                logger.warning("Type '" + id + "' has invalid points, using 1.");
                points = 1;
            }

            MasteryType type = new MasteryType(
                    id,
                    category,
                    group,
                    typeSection.getString("name", id),
                    material(typeSection.getString("icon"), Material.PAPER, logger, id),
                    points,
                    typeSection.getString("includes", "")
            );

            byId.put(id, type);
            typesByCategory.get(category).add(type);

            if (group != null) {
                group.addType(type);
            }

            for (String match : typeSection.getStringList("match")) {
                pending.add(new PendingMatch(type, match.trim().toUpperCase(Locale.ROOT)));
            }
        }
    }

    private void resolveMatches(List<PendingMatch> pending, Logger logger) {
        // Pass 1: exact names.
        for (PendingMatch match : pending) {
            if (match.isWildcard()) {
                continue;
            }

            MasteryType type = match.type();

            if (type.category() == Category.SLAYING) {
                EntityType entityType = Registry.ENTITY_TYPE.get(
                        NamespacedKey.minecraft(match.pattern().toLowerCase(Locale.ROOT))
                );
                if (entityType == null) {
                    logger.warning("Unknown entity type '" + match.pattern() + "' in type '" + type.id() + "'.");
                    continue;
                }
                putIfAbsent(mobTypes, entityType, type, match.pattern(), logger);
                continue;
            }

            Material material = Material.matchMaterial(match.pattern());
            if (material == null) {
                logger.warning("Unknown material '" + match.pattern() + "' in type '" + type.id() + "'.");
                continue;
            }

            if (type.category() == Category.FISHING) {
                putIfAbsent(fishTypes, material, type, match.pattern(), logger);
            } else {
                if (!material.isBlock()) {
                    logger.warning("'" + match.pattern() + "' in type '" + type.id() + "' is not a block.");
                    continue;
                }
                putIfAbsent(blockTypes, material, type, match.pattern(), logger);
            }
        }

        // Pass 2: wildcards (block and fish materials only).
        for (PendingMatch match : pending) {
            if (!match.isWildcard()) {
                continue;
            }

            MasteryType type = match.type();

            if (type.category() == Category.SLAYING) {
                logger.warning("Wildcards are not supported for slaying ('" + match.pattern() + "').");
                continue;
            }

            Pattern regex = Pattern.compile(
                    "^" + Pattern.quote(match.pattern()).replace("*", "\\E.*\\Q") + "$"
            );

            int found = 0;

            for (Material material : Material.values()) {
                if (material.name().startsWith("LEGACY_") || !regex.matcher(material.name()).matches()) {
                    continue;
                }

                if (type.category() == Category.FISHING) {
                    if (!fishTypes.containsKey(material)) {
                        fishTypes.put(material, type);
                        found++;
                    }
                } else if (material.isBlock() && !blockTypes.containsKey(material)) {
                    blockTypes.put(material, type);
                    found++;
                }
            }

            if (found == 0) {
                logger.warning("Wildcard '" + match.pattern() + "' in type '" + type.id() + "' matched nothing new.");
            }
        }
    }

    private static <K> void putIfAbsent(
            Map<K, MasteryType> map,
            K key,
            MasteryType type,
            String name,
            Logger logger
    ) {
        MasteryType existing = map.putIfAbsent(key, type);
        if (existing != null && !existing.equals(type)) {
            logger.warning("'" + name + "' is in both '" + existing.id() + "' and '" + type.id()
                    + "'. Keeping '" + existing.id() + "'.");
        }
    }

    private static Material material(String name, Material fallback, Logger logger, String owner) {
        if (name == null) {
            return fallback;
        }
        Material material = Material.matchMaterial(name);
        if (material == null || !material.isItem()) {
            logger.warning("Invalid icon '" + name + "' for '" + owner + "', using " + fallback.name() + ".");
            return fallback;
        }
        return material;
    }

    private record PendingMatch(MasteryType type, String pattern) {
        boolean isWildcard() {
            return pattern.contains("*");
        }
    }

    // =============================================================
    // Lookups
    // =============================================================

    public LevelCurve curve(Category category) {
        return curves.get(category);
    }

    public List<MasteryType> types(Category category) {
        return Collections.unmodifiableList(typesByCategory.get(category));
    }

    public List<MasteryType> allTypes() {
        return List.copyOf(byId.values());
    }

    public List<Group> groups() {
        return Collections.unmodifiableList(groups);
    }

    public Group group(String id) {
        for (Group group : groups) {
            if (group.id().equals(id)) {
                return group;
            }
        }
        return null;
    }

    public MasteryType type(String id) {
        return id == null ? null : byId.get(id.toLowerCase(Locale.ROOT));
    }

    /** Blocks, Farming or Foraging type of a block material, or null. */
    public MasteryType blockType(Material material) {
        return blockTypes.get(material);
    }

    public boolean isTrackedBlock(Material material) {
        return blockTypes.containsKey(material);
    }

    public MasteryType mobType(EntityType entityType) {
        return mobTypes.get(entityType);
    }

    public MasteryType fishType(Material material) {
        return fishTypes.get(material);
    }

    /** Console summary of types per group / category. */
    public List<String> summary() {
        List<String> lines = new ArrayList<>();

        for (Category category : Category.values()) {
            lines.add(category.key() + ": " + typesByCategory.get(category).size() + " types (unit "
                    + curves.get(category).unit() + ", level 45 total " + curves.get(category).maxTotal() + ")");

            if (category == Category.BLOCKS) {
                for (Group group : groups) {
                    lines.add("  - " + group.name() + ": " + group.types().size() + " types");
                }
            }
        }

        lines.add("Matched " + blockTypes.size() + " block materials, " + mobTypes.size()
                + " mobs and " + fishTypes.size() + " fish.");

        return lines;
    }
}

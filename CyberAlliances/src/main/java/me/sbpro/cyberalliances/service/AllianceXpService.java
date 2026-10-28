package me.sbpro.cyberalliances.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.sbpro.cyberalliances.Messages;
import me.sbpro.cyberalliances.data.AllianceStorage;
import me.sbpro.cyberalliances.model.Alliance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public final class AllianceXpService {
    private static final double[] LEVEL_THRESHOLDS = new double[]{0.0, 0.0, 500.0, 1250.0, 2250.0, 3500.0, 5000.0, 6750.0, 8750.0, 11000.0, 14000.0};
    private final AllianceStorage allianceStorage;
    private final AllianceBuffService buffService;
    private final Map<Material, XpEntry> blockEntries = new EnumMap<>(Material.class);
    private final Map<Material, XpEntry> fishingEntries = new EnumMap<>(Material.class);
    private final Map<EntityType, XpEntry> slayingEntries = new EnumMap<>(EntityType.class);
    private final List<XpEntry> displayEntries = new ArrayList<>();

    public AllianceXpService(org.bukkit.plugin.java.JavaPlugin plugin, AllianceStorage allianceStorage, AllianceBuffService buffService) {
        this.allianceStorage = allianceStorage;
        this.buffService = buffService;
        this.load();
        this.syncStoredLevels();
    }

    private void load() {
        this.blockEntries.clear();
        this.fishingEntries.clear();
        this.slayingEntries.clear();
        this.displayEntries.clear();

        // Mining
        addEntry("mining", Material.COAL_ORE, Component.text("Coal Ore", NamedTextColor.DARK_GRAY), 3, null, null);
        addEntry("mining", Material.DEEPSLATE_COAL_ORE, Component.text("Deepslate Coal Ore", NamedTextColor.DARK_GRAY), 4, null, null);
        addEntry("mining", Material.IRON_ORE, Component.text("Iron Ore", NamedTextColor.WHITE), 4, null, null);
        addEntry("mining", Material.DEEPSLATE_IRON_ORE, Component.text("Deepslate Iron Ore", NamedTextColor.WHITE), 5, null, null);
        addEntry("mining", Material.COPPER_ORE, Component.text("Copper Ore", NamedTextColor.GOLD), 4, null, null);
        addEntry("mining", Material.DEEPSLATE_COPPER_ORE, Component.text("Deepslate Copper Ore", NamedTextColor.GOLD), 5, null, null);
        addEntry("mining", Material.GOLD_ORE, Component.text("Gold Ore", NamedTextColor.GOLD), 5, null, null);
        addEntry("mining", Material.DEEPSLATE_GOLD_ORE, Component.text("Deepslate Gold Ore", NamedTextColor.GOLD), 6, null, null);
        addEntry("mining", Material.REDSTONE_ORE, Component.text("Redstone Ore", NamedTextColor.RED), 5, null, null);
        addEntry("mining", Material.DEEPSLATE_REDSTONE_ORE, Component.text("Deepslate Redstone Ore", NamedTextColor.RED), 6, null, null);
        addEntry("mining", Material.LAPIS_ORE, Component.text("Lapis Ore", NamedTextColor.BLUE), 5, null, null);
        addEntry("mining", Material.DEEPSLATE_LAPIS_ORE, Component.text("Deepslate Lapis Ore", NamedTextColor.BLUE), 6, null, null);
        addEntry("mining", Material.DIAMOND_ORE, Component.text("Diamond Ore", NamedTextColor.AQUA), 8, null, null);
        addEntry("mining", Material.DEEPSLATE_DIAMOND_ORE, Component.text("Deepslate Diamond Ore", NamedTextColor.AQUA), 10, null, null);
        addEntry("mining", Material.EMERALD_ORE, Component.text("Emerald Ore", NamedTextColor.GREEN), 8, null, null);
        addEntry("mining", Material.DEEPSLATE_EMERALD_ORE, Component.text("Deepslate Emerald Ore", NamedTextColor.GREEN), 10, null, null);
        addEntry("mining", Material.NETHER_QUARTZ_ORE, Component.text("Nether Quartz Ore", NamedTextColor.WHITE), 4, null, null);
        addEntry("mining", Material.NETHER_GOLD_ORE, Component.text("Nether Gold Ore", NamedTextColor.GOLD), 4, null, null);
        addEntry("mining", Material.ANCIENT_DEBRIS, Component.text("Ancient Debris", NamedTextColor.DARK_RED), 20, null, null);
        // Excavating
        addEntry("excavating", Material.ROOTED_DIRT, Component.text("Rooted Dirt", NamedTextColor.GOLD), 2, null, null);
        addEntry("excavating", Material.RED_SAND, Component.text("Red Sand", NamedTextColor.RED), 1, null, null);
        addEntry("excavating", Material.CLAY, Component.text("Clay", NamedTextColor.BLUE), 2, null, null);
        addEntry("excavating", Material.SNOW_BLOCK, Component.text("Snow Block", NamedTextColor.WHITE), 1, null, null);
        addEntry("excavating", Material.SOUL_SAND, Component.text("Soul Sand", NamedTextColor.GOLD), 2, null, null);
        addEntry("excavating", Material.SOUL_SOIL, Component.text("Soul Soil", NamedTextColor.GOLD), 2, null, null);
        addEntry("excavating", Material.MYCELIUM, Component.text("Mycelium", NamedTextColor.DARK_PURPLE), 3, null, null);
        // Woodcutting
        addEntry("woodcutting", Material.OAK_LOG, Component.text("Oak Log", NamedTextColor.GOLD), 4, null, null);
        addEntry("woodcutting", Material.SPRUCE_LOG, Component.text("Spruce Log", NamedTextColor.GOLD), 4, null, null);
        addEntry("woodcutting", Material.BIRCH_LOG, Component.text("Birch Log", NamedTextColor.GOLD), 4, null, null);
        addEntry("woodcutting", Material.JUNGLE_LOG, Component.text("Jungle Log", NamedTextColor.GOLD), 5, null, null);
        addEntry("woodcutting", Material.ACACIA_LOG, Component.text("Acacia Log", NamedTextColor.GOLD), 5, null, null);
        addEntry("woodcutting", Material.DARK_OAK_LOG, Component.text("Dark Oak Log", NamedTextColor.GOLD), 5, null, null);
        addEntry("woodcutting", Material.MANGROVE_LOG, Component.text("Mangrove Log", NamedTextColor.GOLD), 5, null, null);
        addEntry("woodcutting", Material.CHERRY_LOG, Component.text("Cherry Log", NamedTextColor.LIGHT_PURPLE), 5, null, null);
        addEntry("woodcutting", Material.CRIMSON_STEM, Component.text("Crimson Stem", NamedTextColor.RED), 6, null, null);
        addEntry("woodcutting", Material.WARPED_STEM, Component.text("Warped Stem", NamedTextColor.AQUA), 6, null, null);
        // Farming
        addEntry("farming", Material.WHEAT, Component.text("Wheat", NamedTextColor.GREEN), 3, "WHEAT", null);
        addEntry("farming", Material.CARROT, Component.text("Carrots", NamedTextColor.GREEN), 3, "CARROTS", null);
        addEntry("farming", Material.POTATO, Component.text("Potatoes", NamedTextColor.GREEN), 3, "POTATOES", null);
        addEntry("farming", Material.BEETROOT, Component.text("Beetroots", NamedTextColor.RED), 3, "BEETROOTS", null);
        addEntry("farming", Material.MELON_SLICE, Component.text("Melon", NamedTextColor.GREEN), 2, "MELON", null);
        addEntry("farming", Material.PUMPKIN, Component.text("Pumpkin", NamedTextColor.GOLD), 4, "PUMPKIN", null);
        addEntry("farming", Material.SUGAR_CANE, Component.text("Sugar Cane", NamedTextColor.GREEN), 2, "SUGAR_CANE", null);
        addEntry("farming", Material.NETHER_WART, Component.text("Nether Wart", NamedTextColor.RED), 4, "NETHER_WART", null);
        addEntry("farming", Material.COCOA_BEANS, Component.text("Cocoa", NamedTextColor.GOLD), 3, "COCOA", null);
        addEntry("farming", Material.CACTUS, Component.text("Cactus", NamedTextColor.GREEN), 2, "CACTUS", null);
        addEntry("farming", Material.BAMBOO, Component.text("Bamboo", NamedTextColor.GREEN), 2, "BAMBOO", null);
        addEntry("farming", Material.SWEET_BERRIES, Component.text("Sweet Berry Bush", NamedTextColor.RED), 2, "SWEET_BERRY_BUSH", null);
        addEntry("farming", Material.KELP, Component.text("Kelp", NamedTextColor.DARK_GREEN), 2, "KELP", null);
        addEntry("farming", Material.SEA_PICKLE, Component.text("Sea Pickle", NamedTextColor.GREEN), 2, "SEA_PICKLE", null);
        // Fishing
        addEntry("fishing", Material.COD, Component.text("Cod", NamedTextColor.YELLOW), 3, null, null);
        addEntry("fishing", Material.SALMON, Component.text("Salmon", NamedTextColor.YELLOW), 3, null, null);
        addEntry("fishing", Material.TROPICAL_FISH, Component.text("Tropical Fish", NamedTextColor.YELLOW), 5, null, null);
        addEntry("fishing", Material.PUFFERFISH, Component.text("Pufferfish", NamedTextColor.YELLOW), 5, null, null);
        addEntry("fishing", Material.BOW, Component.text("Treasure Bow", NamedTextColor.GOLD), 8, null, null);
        addEntry("fishing", Material.ENCHANTED_BOOK, Component.text("Enchanted Book", NamedTextColor.LIGHT_PURPLE), 10, null, null);
        addEntry("fishing", Material.FISHING_ROD, Component.text("Fishing Rod", NamedTextColor.GOLD), 8, null, null);
        addEntry("fishing", Material.NAME_TAG, Component.text("Name Tag", NamedTextColor.YELLOW), 8, null, null);
        addEntry("fishing", Material.SADDLE, Component.text("Saddle", NamedTextColor.GOLD), 8, null, null);
        // Slaying
        addEntry("slaying", Material.ROTTEN_FLESH, Component.text("Zombie", NamedTextColor.RED), 4, null, EntityType.ZOMBIE);
        addEntry("slaying", Material.BONE, Component.text("Skeleton", NamedTextColor.WHITE), 4, null, EntityType.SKELETON);
        addEntry("slaying", Material.STRING, Component.text("Spider", NamedTextColor.WHITE), 4, null, EntityType.SPIDER);
        addEntry("slaying", Material.GUNPOWDER, Component.text("Creeper", NamedTextColor.GRAY), 5, null, EntityType.CREEPER);
        addEntry("slaying", Material.ENDER_PEARL, Component.text("Enderman", NamedTextColor.DARK_PURPLE), 8, null, EntityType.ENDERMAN);
        addEntry("slaying", Material.BLAZE_ROD, Component.text("Blaze", NamedTextColor.GOLD), 10, null, EntityType.BLAZE);
        addEntry("slaying", Material.PHANTOM_MEMBRANE, Component.text("Phantom", NamedTextColor.BLUE), 8, null, EntityType.PHANTOM);
        addEntry("slaying", Material.SLIME_BALL, Component.text("Slime", NamedTextColor.GREEN), 5, null, EntityType.SLIME);
        addEntry("slaying", Material.MAGMA_CREAM, Component.text("Magma Cube", NamedTextColor.GOLD), 6, null, EntityType.MAGMA_CUBE);
        addEntry("slaying", Material.GHAST_TEAR, Component.text("Ghast", NamedTextColor.WHITE), 12, null, EntityType.GHAST);
        addEntry("slaying", Material.PRISMARINE_SHARD, Component.text("Guardian", NamedTextColor.DARK_AQUA), 8, null, EntityType.GUARDIAN);
        addEntry("slaying", Material.WITHER_SKELETON_SKULL, Component.text("Wither Skeleton", NamedTextColor.DARK_GRAY), 12, null, EntityType.WITHER_SKELETON);
    }

    private void addEntry(String category, Material iconMaterial, Component displayName, double xp, String keyName, EntityType entityType) {
        XpEntry entry = new XpEntry(category, iconMaterial, displayName, xp, keyName, entityType);
        this.displayEntries.add(entry);

        switch (category.toLowerCase(java.util.Locale.ROOT)) {
            case "fishing" -> this.fishingEntries.put(iconMaterial, entry);
            case "slaying" -> {
                if (entityType != null) {
                    this.slayingEntries.put(entityType, entry);
                }
            }
            default -> {
                Material triggerMaterial = keyName == null ? iconMaterial : Material.matchMaterial(keyName);
                if (triggerMaterial != null) {
                    this.blockEntries.put(triggerMaterial, entry);
                }
            }
        }
    }

    public Optional<XpEntry> getBlockEntry(Material material) {
        return Optional.ofNullable(this.blockEntries.get(material));
    }

    public Optional<XpEntry> getFishingEntry(Material material) {
        return Optional.ofNullable(this.fishingEntries.get(material));
    }

    public Optional<XpEntry> getSlayingEntry(EntityType entityType) {
        return Optional.ofNullable(this.slayingEntries.get(entityType));
    }

    public List<XpEntry> getDisplayEntries() {
        return Collections.unmodifiableList(this.displayEntries);
    }

    public int getLevelForXp(double xp) {
        int level = 1;
        for (int current = 2; current < LEVEL_THRESHOLDS.length; ++current) {
            if (!(xp >= LEVEL_THRESHOLDS[current])) continue;
            level = current;
        }
        return Math.min(10, level);
    }

    public double getCurrentLevelBaseXp(int level) {
        return LEVEL_THRESHOLDS[Math.max(1, Math.min(level, 10))];
    }

    public double getNextLevelXp(int level) {
        if (level >= 10) {
            return LEVEL_THRESHOLDS[10];
        }
        return LEVEL_THRESHOLDS[level + 1];
    }

    public double getXpMultiplier(int level) {
        return switch (level) {
            case 2, 3, 4, 5 -> 1.05;
            case 6 -> 1.1;
            case 7 -> 1.15;
            case 8, 9 -> 1.175;
            case 10 -> 1.2;
            default -> 1.0;
        };
    }

    public int getProgressPercent(Alliance alliance) {
        if (alliance.getLevel() >= 10) {
            return 100;
        }
        double currentBase = this.getCurrentLevelBaseXp(alliance.getLevel());
        double nextBase = this.getNextLevelXp(alliance.getLevel());
        double progressed = Math.max(0.0, alliance.getXp() - currentBase);
        double needed = Math.max(1.0, nextBase - currentBase);
        return (int)Math.max(0L, Math.min(100L, Math.round(progressed / needed * 100.0)));
    }

    public Component getProgressBar(Alliance alliance) {
        int percent = this.getProgressPercent(alliance);
        int filled = Math.max(0, Math.min(20, (int) Math.round((double) percent / 5.0)));
        return Messages.progressBar(filled);
    }

    public void awardXp(Player player, double baseXp) {
        Optional<Alliance> allianceOptional = this.allianceStorage.getAllianceByPlayer(player.getUniqueId());
        if (allianceOptional.isEmpty()) {
            return;
        }
        Alliance alliance = allianceOptional.get();
        int oldLevel = alliance.getLevel();
        double awarded = baseXp * this.getXpMultiplier(oldLevel);
        alliance.addXp(awarded);
        int newLevel = this.getLevelForXp(alliance.getXp());
        alliance.setLevel(newLevel);
        this.allianceStorage.save();
        if (newLevel != oldLevel) {
            this.buffService.refreshAlliance(alliance);
            for (UUID memberId : alliance.getMembers()) {
                Player member = Bukkit.getPlayer((UUID)memberId);
                if (member == null) continue;
                member.sendMessage(Messages.levelUp(newLevel));
            }
        }
    }

    public List<Alliance> getTopAlliances() {
        return this.allianceStorage.getAlliances().stream().sorted((left, right) -> Double.compare(right.getXp(), left.getXp())).toList();
    }

    private void syncStoredLevels() {
        boolean changed = false;
        for (Alliance alliance : this.allianceStorage.getAlliances()) {
            int level = this.getLevelForXp(alliance.getXp());
            if (alliance.getLevel() == level) continue;
            alliance.setLevel(level);
            changed = true;
        }
        if (changed) {
            this.allianceStorage.save();
        }
    }

    public record XpEntry(String category, Material iconMaterial, Component displayName, double xp, String key, EntityType entityType) {
        public String prettyCategory() {
            return switch (this.category.toLowerCase(Locale.ROOT)) {
                case "woodcutting" -> "Woodcutting";
                case "slaying" -> "Slaying";
                case "fishing" -> "Fishing";
                case "excavating" -> "Excavating";
                case "farming" -> "Farming";
                default -> "Mining";
            };
        }
    }
}


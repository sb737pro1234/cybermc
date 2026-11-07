package me.sbpro.cybermastery.model;

/**
 * The level table of one category.
 *
 * Level n costs unit x shape(n), where shape is 1, 2, 4, 6, 8, 10 ... (+2 per level).
 * All 45 levels together cost 1981 x unit.
 *
 * Everything here works in whole points. Player progress is stored in
 * quarter-points (units), see {@link #UNITS_PER_POINT}.
 */
public final class LevelCurve {

    public static final int MAX_LEVEL = 45;

    /** The sum of shape(1..45). */
    public static final long SHAPE_TOTAL = 1981L;

    /** Progress is stored in quarter-points so placing can give 25%. */
    public static final long UNITS_PER_POINT = 4L;

    private final long unit;
    private final long[] cumulative;

    public LevelCurve(long unit) {
        if (unit <= 0) {
            throw new IllegalArgumentException("unit must be positive");
        }
        this.unit = unit;
        this.cumulative = buildTable(unit);
    }

    /** Shape of level n (1..45): 1, 2, then 2 x (n - 1). */
    public static long shape(int n) {
        return n == 1 ? 1 : n == 2 ? 2 : 2L * (n - 1);
    }

    /** Cumulative points needed to reach levels 1..45 (index 0 = level 1). */
    public static long[] buildTable(long unit) {
        long[] cumulative = new long[MAX_LEVEL];
        long running = 0;
        for (int n = 1; n <= MAX_LEVEL; n++) {
            running += unit * shape(n);
            cumulative[n - 1] = running;
        }
        return cumulative;
    }

    public long unit() {
        return unit;
    }

    /** True when level 45 costs exactly 1981 x unit in total. */
    public boolean isValid() {
        return cumulative[MAX_LEVEL - 1] == SHAPE_TOTAL * unit;
    }

    /** Points the given level costs on its own (level 1..45). */
    public long costOf(int level) {
        return unit * shape(level);
    }

    /** Total points needed to reach the given level (0 for level 0). */
    public long totalFor(int level) {
        if (level <= 0) {
            return 0;
        }
        return cumulative[Math.min(level, MAX_LEVEL) - 1];
    }

    /** Total points needed to reach level 45. */
    public long maxTotal() {
        return cumulative[MAX_LEVEL - 1];
    }

    /** Level reached with the given whole points (0..45). */
    public int levelFor(long points) {
        int low = 0;
        int high = MAX_LEVEL;
        // Highest level whose cumulative total is <= points.
        while (low < high) {
            int mid = (low + high + 1) >>> 1;
            if (cumulative[mid - 1] <= points) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    /** Level reached with the given stored units (quarter-points). */
    public int levelForUnits(long units) {
        return levelFor(units / UNITS_PER_POINT);
    }

    /** Units still needed to reach the next level, or 0 at max level. */
    public long unitsToNext(long units) {
        int level = levelForUnits(units);
        if (level >= MAX_LEVEL) {
            return 0;
        }
        return totalFor(level + 1) * UNITS_PER_POINT - units;
    }

    /** Units still needed to reach the given level, or 0 if already reached. */
    public long unitsToLevel(long units, int level) {
        return Math.max(0, totalFor(level) * UNITS_PER_POINT - units);
    }

    /** How far through the current level the player is (0.0 to 1.0, 1.0 at max). */
    public double progressInLevel(long units) {
        int level = levelForUnits(units);
        if (level >= MAX_LEVEL) {
            return 1.0;
        }
        long start = totalFor(level) * UNITS_PER_POINT;
        long cost = costOf(level + 1) * UNITS_PER_POINT;
        return Math.max(0.0, Math.min(1.0, (double) (units - start) / cost));
    }
}

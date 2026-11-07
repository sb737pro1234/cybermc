package me.sbpro.cybermastery.model;

/**
 * Turns stored points into action counts for display ("Mine Stone: 5/15").
 *
 * Points still do all the work behind the scenes; this only converts them into
 * how many blocks / kills / harvests / catches a player needs, using the type's
 * points per action. Totals are rounded up per level and the per-level counts are
 * the difference between them, so the numbers always add up.
 */
public final class ActionMath {

    private ActionMath() {
    }

    /** Progress inside the current level, e.g. done = 5, need = 15. */
    public record LevelProgress(long done, long need) {

        public double fraction() {
            return need <= 0 ? 0.0 : Math.min(1.0, (double) done / need);
        }
    }

    /** Actions done in total (placements count as fractions, so this rounds down). */
    public static long actionsDone(long units, long pointsPerAction) {
        if (pointsPerAction <= 0) {
            return 0;
        }
        return units / (pointsPerAction * LevelCurve.UNITS_PER_POINT);
    }

    /** Actions needed in total to reach a level (0 for level 0). */
    public static long actionsFor(LevelCurve curve, long pointsPerAction, int level) {
        if (level <= 0 || pointsPerAction <= 0) {
            return 0;
        }
        return Math.ceilDiv(curve.totalFor(level), pointsPerAction);
    }

    /**
     * Actions done inside the current level and actions that level needs.
     * At max level both are 0.
     */
    public static LevelProgress current(LevelCurve curve, long pointsPerAction, long units) {
        int level = curve.levelForUnits(units);

        if (level >= LevelCurve.MAX_LEVEL) {
            return new LevelProgress(0, 0);
        }

        long start = actionsFor(curve, pointsPerAction, level);
        long end = actionsFor(curve, pointsPerAction, level + 1);
        long need = Math.max(1, end - start);
        long done = actionsDone(units, pointsPerAction) - start;

        return new LevelProgress(Math.max(0, Math.min(done, need - 1)), need);
    }
}

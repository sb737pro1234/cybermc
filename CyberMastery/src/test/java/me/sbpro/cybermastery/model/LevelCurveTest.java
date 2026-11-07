package me.sbpro.cybermastery.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelCurveTest {

    /** The "Units" column of the reference table in the build prompt. */
    private static final long[] REFERENCE_UNITS = {
            1, 2, 4, 6, 8, 10, 12, 14, 16, 18,
            20, 22, 24, 26, 28, 30, 32, 34, 36, 38,
            40, 42, 44, 46, 48, 50, 52, 54, 56, 58,
            60, 62, 64, 66, 68, 70, 72, 74, 76, 78,
            80, 82, 84, 86, 88
    };

    @Test
    void shapeMatchesReferenceTable() {
        long sum = 0;
        for (int level = 1; level <= LevelCurve.MAX_LEVEL; level++) {
            assertEquals(REFERENCE_UNITS[level - 1], LevelCurve.shape(level), "shape(" + level + ")");
            sum += LevelCurve.shape(level);
        }
        assertEquals(1981, sum);
        assertEquals(88, LevelCurve.shape(45));
    }

    @ParameterizedTest
    @ValueSource(longs = {20, 15, 25, 5})
    void tableMatchesReferenceForEachUnit(long unit) {
        long[] table = LevelCurve.buildTable(unit);
        long running = 0;

        for (int level = 1; level <= LevelCurve.MAX_LEVEL; level++) {
            long cost = REFERENCE_UNITS[level - 1] * unit;
            running += cost;

            assertEquals(running, table[level - 1], "cumulative level " + level + " (unit " + unit + ")");
            assertEquals(0, cost % unit, "cost is a multiple of the unit");
            assertEquals(0, cost % 5, "cost is a multiple of 5");
        }

        assertTrue(new LevelCurve(unit).isValid());
    }

    @Test
    void levelFortyFiveTotals() {
        assertEquals(39_620, new LevelCurve(20).maxTotal()); // Blocks
        assertEquals(29_715, new LevelCurve(15).maxTotal()); // Foraging
        assertEquals(49_525, new LevelCurve(25).maxTotal()); // Farming
        assertEquals(9_905, new LevelCurve(5).maxTotal());   // Slaying and Fishing
    }

    @Test
    void farmingCostsAreCleanRoundNumbers() {
        LevelCurve farming = new LevelCurve(25);
        long[] expected = {25, 50, 100, 150, 200, 250};
        for (int level = 1; level <= expected.length; level++) {
            assertEquals(expected[level - 1], farming.costOf(level));
        }
        assertEquals(2_200, farming.costOf(45));
    }

    @Test
    void levelForPoints() {
        LevelCurve blocks = new LevelCurve(20);

        assertEquals(0, blocks.levelFor(0));
        assertEquals(0, blocks.levelFor(19));
        assertEquals(1, blocks.levelFor(20));
        assertEquals(1, blocks.levelFor(59));
        assertEquals(2, blocks.levelFor(60));
        assertEquals(44, blocks.levelFor(39_619));
        assertEquals(45, blocks.levelFor(39_620));
        assertEquals(45, blocks.levelFor(Long.MAX_VALUE / 8));
    }

    @Test
    void unitsAreQuarterPoints() {
        LevelCurve blocks = new LevelCurve(20);

        // 20 points = 80 units reaches level 1; 79 units does not.
        assertEquals(0, blocks.levelForUnits(79));
        assertEquals(1, blocks.levelForUnits(80));
        assertEquals(1, blocks.unitsToNext(79));
        assertEquals(160, blocks.unitsToNext(80)); // level 2 costs 40 points
        assertEquals(0, blocks.unitsToNext(39_620 * 4));
        assertEquals(1.0, blocks.progressInLevel(39_620 * 4));
    }
}

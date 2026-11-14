package me.sbpro.cybermastery.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionMathTest {

    private static final long U = LevelCurve.UNITS_PER_POINT;

    @Test
    void stoneCountsMatchPoints() {
        LevelCurve blocks = new LevelCurve(20);

        // 1 point per block: actions equal points.
        assertEquals(20, ActionMath.actionsFor(blocks, 1, 1));
        assertEquals(420, ActionMath.actionsFor(blocks, 1, 5));
        assertEquals(39_620, ActionMath.actionsFor(blocks, 1, 45));

        // 25 blocks mined: level 1, 5 of the 40 needed for level 2.
        ActionMath.LevelProgress progress = ActionMath.current(blocks, 1, 25 * U);
        assertEquals(5, progress.done());
        assertEquals(40, progress.need());
    }

    @Test
    void rareTypesRoundUp() {
        LevelCurve blocks = new LevelCurve(20);

        // Diamond ore gives 25: level 1 (20 points) needs 1 ore, level 45 needs 1,585.
        assertEquals(1, ActionMath.actionsFor(blocks, 25, 1));
        assertEquals(1_585, ActionMath.actionsFor(blocks, 25, 45));

        // Per-level counts always add up to the total.
        long sum = 0;
        for (int level = 1; level <= LevelCurve.MAX_LEVEL; level++) {
            sum += ActionMath.actionsFor(blocks, 25, level) - ActionMath.actionsFor(blocks, 25, level - 1);
        }
        assertEquals(1_585, sum);
    }

    @Test
    void bossNeverShowsZeroNeeded() {
        LevelCurve slaying = new LevelCurve(5);

        // One warden (1000 points) jumps several levels; the next level still needs at least 1 kill.
        ActionMath.LevelProgress progress = ActionMath.current(slaying, 1000, 1000 * U);
        assertEquals(0, progress.done());
        assertEquals(1, progress.need());
    }

    @Test
    void maxLevelHasNothingLeft() {
        LevelCurve blocks = new LevelCurve(20);
        ActionMath.LevelProgress progress = ActionMath.current(blocks, 1, 39_620 * U);
        assertEquals(0, progress.need());
    }

    @Test
    void placementQuartersRoundDown() {
        // 3 placements of stone = 0.75 points: still 0 whole blocks.
        assertEquals(0, ActionMath.actionsDone(3, 1));
        assertEquals(1, ActionMath.actionsDone(4, 1));
    }
}

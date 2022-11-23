package au.edu.jcu.stopwatch;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class StopwatchTests {
    @Test
    public void testConstructor() {
        Stopwatch stopwatch = new Stopwatch();
        assertEquals("00:00:00", stopwatch.toString());
    }

    @Test
    public void testTicker() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.tick();
        assertEquals("00:00:01", stopwatch.toString());

        for (int i = 0; i < 59; i++) {
            stopwatch.tick();
        }
        assertEquals("00:01:00", stopwatch.toString());

        for (int i = 0; i < 59 * 60; i++) {
            stopwatch.tick();
        }
        assertEquals("01:00:00", stopwatch.toString());

        for (int i = 0; i < 60 * 60 * 95; i++) {
            stopwatch.tick();
        }
        assertEquals("96:00:00", stopwatch.toString());
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}
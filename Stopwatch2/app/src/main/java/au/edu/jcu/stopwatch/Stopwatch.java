package au.edu.jcu.stopwatch;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Stopwatch {
    private int hours;
    private int minutes;
    private int seconds;

    public Stopwatch() {
        hours = minutes = seconds = 0;
    }

    public Stopwatch(String value) {
        String[] values = value.split(":");
        hours = Integer.parseInt(values[0]);
        minutes = Integer.parseInt(values[1]);
        seconds = Integer.parseInt(values[2]);
    }

    public void tick() {
        if (seconds < 59) {
            seconds ++;
            return;
        }
        if (minutes < 59) {
            minutes ++;
            seconds = 0;
            return;
        }
        // maximum time allow to display 99:59:59
        if (hours < 99) {
            hours ++;
            minutes = 0;
            seconds = 0;
            return;
        }
        hours = minutes = seconds = 0;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
}

package au.edu.jcu.spiritlevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    SpiritLevelView spiritLevelView;
    private float x;
    private float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get all device sensors, log and check availabilities
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Log.i("gravity", gravitySensor.toString());

        spiritLevelView = findViewById(R.id.spiritLevelView);
        spiritLevelView.setBubble(x, y);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final String[] axes = new String[] {"x", "y", "z"};
        final String format = "%s %.2f ";

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < axes.length; i++) {
            message.append(String.format(Locale.getDefault(), format,
                    axes[i], sensorEvent.values[i]));
        }
        Log.i("gravity", message.toString());

        x = sensorEvent.values[0];
        y = sensorEvent.values[1];

        spiritLevelView.setBubble(x, y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        String message = String.format(Locale.getDefault(), "%s: %d", sensor.getName(), i);
        Log.i("gravity", message);
    }
}
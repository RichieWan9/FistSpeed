package com.iyato.fistspeed;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;

public class SpeedDetector implements SensorEventListener {
    
    private static final String TAG = "SpeedDetector";
    private static final boolean LOGV = true;
    
    // private static final float SPEED_THRESHOLD = 13;
    
    private ArrayList<SpeedListener> mSpeedListeners;
    
    public SpeedDetector(Context context) {
        mSpeedListeners = new ArrayList<SpeedListener>();
    }
    
    public void addSpeedListener(SpeedListener listener) {
        if (!mSpeedListeners.contains(listener)) {
            mSpeedListeners.add(listener);
        }
    }
    
    public void removeSpeedListener(SpeedListener listener) {
        mSpeedListeners.remove(listener);
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (LOGV) {
                StringBuilder sb = new StringBuilder("Event values:\n");
                sb.append("time: ").append(event.timestamp).append("\n")
                        .append("X: ").append(event.values[0]).append("\n")
                        .append("Y: ").append(event.values[1]).append("\n")
                        .append("Z: ").append(event.values[2]);
                Log.v(TAG, sb.toString());
            }
            
        }
    }   
}

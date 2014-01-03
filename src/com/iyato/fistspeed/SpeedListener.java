package com.iyato.fistspeed;

public interface SpeedListener {
    
    /**
     * Called when the min detected speed is detected.
     */
    public void onDetectedSpeed();
    
    /**
     * Called when the max speed is detected.
     * @param speed the current speed.
     */
    public void onMaxSpeed(float speed);

}

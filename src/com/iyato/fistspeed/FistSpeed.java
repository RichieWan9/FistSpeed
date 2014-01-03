
package com.iyato.fistspeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.IOException;

public class FistSpeed extends Activity implements SpeedListener {
    
    private static final String TAG = "FistSpeed";
    private static final boolean LOGV = true;
    
    private View mUpView;
    private View mUpLineView;
    private View mDownView;
    private View mDownLineView;
    private View mSplashView;
    private TextView mTitleView;
    private TextView mSpeedView;
    
    private Animation mAnimUpDown;
    private Animation mAnimDownUp;
    private Animation mTitleTranslation;
    private Animation mAnimGrowOut;
    private Animation mSpeedInfoIn;
    private Animation mSpeedInfoOut;
    
    private SoundPool mSoundPool;
    private int mSoundId;
    
    private SensorManager mSensorManager;
    private SpeedDetector mSpeedDetector;
    private boolean mDetected;
    
    private AudioManager mAudioManager;
    
    private Handler mHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Typeface hyiXft = Typeface.createFromAsset(getAssets(), "fonts/hyi_xft.ttf");
        
        mUpView = findViewById(R.id.up);
        mUpLineView = findViewById(R.id.up_line);
        mDownView = findViewById(R.id.down);
        mDownLineView = findViewById(R.id.down_line);
        mSplashView = findViewById(R.id.splash);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setTypeface(hyiXft);
        mSpeedView = (TextView) findViewById(R.id.speed_text);
        mSpeedView.setTypeface(hyiXft);
        
        TextView logoView = (TextView) findViewById(R.id.logo);
        logoView.setTypeface(hyiXft);
        
        // Test code
        TextView infoView = (TextView) findViewById(R.id.info);
        infoView.setTypeface(hyiXft);
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*onDetectedSpeed();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onMaxSpeed(15);
                    }
                }, 2000);*/
                Intent intent = new Intent(FistSpeed.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        
        loadAnimations();
        
        mHandler = new Handler();
        // Hide the splash view.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSplashView.getVisibility() == View.VISIBLE) {
                    mSplashView.setVisibility(View.GONE);
                    mSplashView.startAnimation(mAnimGrowOut);
                }
            }
        }, 3 * 1000);
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSpeedDetector = new SpeedDetector(this);
        mSpeedDetector.addSpeedListener(this);
        
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        try {
            mSoundId = mSoundPool.load(getAssets().openFd("sounds/speed_detected.mp3"), 1);
        } catch (IOException e) {
            e.printStackTrace();
            mSoundId = 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerDetector();
        mTitleView.startAnimation(mTitleTranslation);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        unregisterDetector();
        mTitleTranslation.cancel();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
    }
    
    @Override
    public void onDetectedSpeed() {
        mAnimUpDown.cancel();
        mAnimDownUp.cancel();
        mSpeedInfoOut.cancel();        
        
        mSpeedView.setVisibility(View.GONE);
        mSpeedView.startAnimation(mSpeedInfoOut);
        mUpView.startAnimation(mAnimUpDown);
        mDownView.startAnimation(mAnimDownUp);
        
        mUpLineView.setVisibility(View.GONE);
        mDownLineView.setVisibility(View.GONE);
    }

    @Override
    public void onMaxSpeed(float speed) {
        // mVibrator.vibrate(300);
        playSound();
        mSpeedInfoIn.cancel();
        mSpeedView.setText("" + (int) speed); 
        mSpeedView.setVisibility(View.VISIBLE);
        mSpeedView.startAnimation(mSpeedInfoIn);
        
        mUpLineView.setVisibility(View.GONE);
        mDownLineView.setVisibility(View.GONE);
    }
    
    public void registerDetector() {
        if (!mDetected) {
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(mSpeedDetector, sensor, SensorManager.SENSOR_DELAY_GAME);
            mDetected = true;
        }
    }
    
    public void unregisterDetector() {
        if (mDetected) {
            mSensorManager.unregisterListener(mSpeedDetector);
            mDetected = false;
        }
    }
    
    private void playSound() {
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolume * 1.0f / maxVolume;
        if (LOGV) Log.v(TAG, "playSound() " +  volume);
        mSoundPool.play(mSoundId, volume, volume, 0, 0, 1);
    }
    
    private void loadAnimations() {
        mAnimUpDown = AnimationUtils.loadAnimation(this, R.anim.push_up_pull_down);
        mAnimDownUp = AnimationUtils.loadAnimation(this, R.anim.push_down_pull_up);
        mAnimGrowOut = AnimationUtils.loadAnimation(this, R.anim.grow_out);
        mTitleTranslation = AnimationUtils.loadAnimation(this, R.anim.back_and_forth);
        mSpeedInfoIn = AnimationUtils.loadAnimation(this, R.anim.speed_text_translate_in);
        mSpeedInfoOut = AnimationUtils.loadAnimation(this, R.anim.speed_text_translate_out);
    }
}

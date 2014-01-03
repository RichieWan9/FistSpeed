package com.iyato.fistspeed;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class AboutActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.about);
        
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        
        Typeface hyiXft = Typeface.createFromAsset(getAssets(), "fonts/hyi_xft.ttf");
        
        TextView appInfoView = (TextView) findViewById(R.id.app_info);
        appInfoView.setTypeface(hyiXft);
        String versionName = "v1.0";
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = "v" + pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            versionName = "v1.0";
        }
        appInfoView.setText(versionName);
        
        TextView corpNameView = (TextView) findViewById(R.id.corp_name);
        corpNameView.setTypeface(hyiXft);
        
        TextView copyRightView = (TextView) findViewById(R.id.copyright);
        copyRightView.setTypeface(hyiXft);
        
        Animation animScroll = AnimationUtils.loadAnimation(this, R.anim.vertical_scroll);
        View aboutView = findViewById(R.id.about_view);
        aboutView.startAnimation(animScroll);
    }
}

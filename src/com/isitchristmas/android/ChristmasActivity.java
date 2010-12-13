package com.isitchristmas.android;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class ChristmasActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        isItChristmas(); // set the answer now
        setAlarm(); // so it updates while the user is watching on Christmas
        
        // launch notification now (debug)
        // this.sendBroadcast(new Intent(this, ChristmasNotificationReceiver.class));
        
        if (!ChristmasUtils.alarmsInitialized(this))
        	ChristmasUtils.initializeAlarms(this);
    }
    
    public void isItChristmas() {
    	((TextView) findViewById(R.id.answer)).setText(Christmas.answer(Christmas.isIt(), Locale.getDefault()));
    }
    
    final Handler handler = new Handler();
    final Runnable updater = new Runnable() {
    	public void run() {
    		isItChristmas();
    	}
    };
    
    public void setAlarm() {
    	Thread alarm = new Thread() {
    		public void run() {
    			long untilChristmas = Christmas.time() - System.currentTimeMillis();
    			
    			try {
    				sleep(untilChristmas);
    			} catch(InterruptedException e) {
    				// well, I never
    			}
    			handler.post(updater);
    		};
    	};
    	alarm.start();
    }
}
package com.example.hyperion;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.os.Looper;

public class GameThread extends Thread
{
	private final Activity activity;
	private boolean isRunning;
	
	private Playfield playfield = new Playfield ();
	
	public GameThread (Activity activity) {
       this.activity = activity;
       
       activity.runOnUiThread(this);
	}
	
	public void startRunning () {
		isRunning = true;
	}
	
	public void stopRunning () {
		isRunning = false;
	}
	
	@Override
	public void run () {
	
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
		ft.add(android.R.id.content, playfield);
		ft.commit();
		
		Handler refresh = new Handler(Looper.getMainLooper());
		refresh.post(new Runnable() {
		    public void run()
		    {
		    	while (isRunning) {
			    	playfield.getBus().getPowerComponent().drainPower();
			    	try {
						sleep (1000);
					} catch (InterruptedException e) {
					}
		    	}
		    }
		});
	}
}

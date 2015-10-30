package com.example.hyperion.Core;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.util.Log;

/**
 * Main Game Thread for Project Hyperion. Currently initializes the playfield.
 *
 * @author 		Mattias Benngard
 * @version		2.0
 * @since		2015-09-30
 */

public class GameThread extends Thread
{
	private final Activity activity;
	private boolean running;

	private static final Playfield playfield = new Playfield ();

	/**
	 * Initializes a game thread to perform executions in.
	 * @param activity - current activity to execute game thread in.
	 */
	public GameThread (Activity activity) {
		this.activity = activity;
	}

	/**
	 * Public setter for running.
	 *
	 * @param value - if main thread should be executing or not.
	 */
	public void setRunning (boolean value) {
		running = value;
	}

	/**
	 * Public getter for running.
	 *
	 * @return if main thread is running or not.
	 */
	public boolean isRunning () {
		return running;
	}

	@Override
	public void run () {
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
		ft.add(android.R.id.content, playfield);
		ft.commit();

		while (running) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if( !playfield.isPaused() ) {
						playfield.getBackground().backgroundMovement();
						playfield.getObstacles().obstaclesMovement();
						playfield.spawnBattery();
						playfield.getBus().update();
						playfield.updateBussReader();
						playfield.spawnNewObstacles();
						if ( !playfield.getBus().getPowerComponent().drainPower()) {
							playfield.gameOver();
						}
						if (playfield.getCollisionDetector().checkCollsion(playfield.getBus().getLightingBolts(), playfield.getBus(), playfield.getObstacles())) {
							playfield.gameOver();
						}
					}
				}
			});

			try {
				sleep (33);
			} catch (InterruptedException e) {
				running = false;
				Log.e(getName(), "Main loop could not sleep.");
			}
		}
	}
}
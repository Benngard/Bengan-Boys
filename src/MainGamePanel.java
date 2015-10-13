package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Main Game View for Project Hyperion. Initializes game thread.
 * 
 * @author 		Mattias Benngard
 * @version		1.0
 * @since		2015-09-30
 */

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback
{
	private static GameThread gameThread;
	
	/**
	 * Initializes the Main Game View.
	 * @param context - which context to initialize game thread in.
	 */
	public MainGamePanel(Context context) {
		super(context);

		getHolder().addCallback(this);
		
		gameThread = new GameThread ((Activity)context);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (gameThread.isRunning() == false) {
			gameThread.setRunning(true);
			gameThread.start();
		} else {
			gameThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameThread.setRunning(false);
	}
}

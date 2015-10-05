package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback
{
	private static GameThread gameThread;
	
	public MainGamePanel(Context context) {
		super(context);

		getHolder().addCallback(this);
		
		gameThread = new GameThread ((Activity)context);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		gameThread.startRunning();
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameThread.stopRunning();
	}
}

package com.example.hyperion;

import com.example.hyprion.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Playfield Fragment, initializes GUI for Playfield; Left- Right, Fire Button.
 *
 * @author 		Mattias Benngard, Daniel Edsinger, Ola Andersson
 * @version		1.4
 * @since		2015-10-06
 */

public class Playfield extends Fragment
{
	private Bus bus;
	private Background background;
	private Obstacles obstacles;
	private CollisionDetector collisionDetector;
	private ObstacleSpawner obstacleSpawner;
	private int counter;
	private int obstacleSpawnFreq;
	private int signalUpdateFreq;
	private boolean paused;
	private final Random rand = new Random();
	private final LinkedList<SignalType> eventQueue = new LinkedList<>();

	private List<Button> playButtons;
	private List<Button> gameOverButtons;

	private static final String TAG = "MyMessage";

	private static final int BUTTON_LEFT = R.id.button_left;
	private static Button buttonLeft;

	private static final int BUTTON_RIGHT = R.id.button_right;
	private static Button buttonRight;

	private final static int BUTTON_FIRE = R.id.button_fire;
	private static Button buttonFire;

	private final static  int BUTTON_NEW_GAME = R.id.buttonNewGame;
	private static  Button buttonNewGame;

	private final static  int BUTTON_QUIT = R.id.buttonQuit;
	private static  Button buttonQuit;

	private final static int TEXT_GAME_OVER = R.id.gameover;
	private static TextView textGameOver;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		DisplayMetrics metrics = new DisplayMetrics();
		this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

		background = new Background(metrics);
		bus = new Bus(metrics);
		obstacles = new Obstacles(metrics);

		collisionDetector = new CollisionDetector();
		obstacleSpawner = new ObstacleSpawner(obstacles);
		counter = 40;
		obstacleSpawnFreq = 0;


		//Add views to container
		container.addView(background.getView(getActivity()));
		container.addView(obstacles.getView(getActivity()));
		container.addView(bus.getView(getActivity()));
		container.addView(bus.getPowerComponent().getView(getActivity()));

		final View view = inflater.inflate(R.layout.fragment_playfield, container, false);
		playButtons = new ArrayList<>();
		gameOverButtons = new ArrayList<>();

		buttonLeft = (Button) view.findViewById(BUTTON_LEFT);
		buttonLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				left();
			}
		});
		playButtons.add(buttonLeft);

		buttonRight = (Button) view.findViewById(BUTTON_RIGHT);
		buttonRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				right();
			}
		});
		playButtons.add(buttonRight);

		buttonFire = (Button) view.findViewById(BUTTON_FIRE);
		buttonFire.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fire();
			}
		});
		playButtons.add(buttonFire);

		buttonNewGame = (Button) view.findViewById(BUTTON_NEW_GAME);
		buttonNewGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startNewGame();
			}
		});
		buttonNewGame.setVisibility(View.GONE);
		gameOverButtons.add(buttonNewGame);

		buttonQuit = (Button) view.findViewById(BUTTON_QUIT);
		buttonQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				quitGame();
			}
		});
		buttonQuit.setVisibility(View.GONE);
		gameOverButtons.add(buttonQuit);

		textGameOver = (TextView) view.findViewById(TEXT_GAME_OVER);
		textGameOver.setVisibility(View.GONE);


		paused = false;
		return view;
	}

	/**
	 * Public getter for the collisionDetector
	 *
	 * @return - instance of the collisionDetector
	 */
	public CollisionDetector getCollisionDetector () {return collisionDetector;}

	/**
	 * Public getter for bus.
	 *
	 * @return - instance of bus.
	 */
	public Bus getBus() {
		return bus;
	}

	/**
	 * Invokes the AsyncTask in the BussReader to check all the signals from the server.
	 */
	public void updateBussReader () {
		if (signalUpdateFreq == 33) {
			new BussReader().execute(eventQueue);
			signalUpdateFreq = 0;
		}
		signalUpdateFreq++;
	}

	/**
	 * Runs spawnObstacle in obstacleSpawner
	 */
	public void spawnNewObstacles(){
		if (obstacleSpawnFreq == 160) {
			obstacleSpawnFreq = 0;
			if (!eventQueue.isEmpty()) {
				obstacleSpawner.spawnObstacle(eventQueue.poll());
			}
			obstacleSpawner.spawnObstacle(SignalType.EMPTY);
		}
		obstacleSpawnFreq += 1;
	}

	/**
	 * Action Listener for Fire Button. Tries to fire lightning bolts, if the power is enough for the bus.
	 */
	private void fire () {
		bus.spawnLightning();
	}

	/**
	 * Spawns a battery on a random lane so the bus can get more energy
	 */
	public void spawnBattery(){
		if (counter == 160 ) {
			int randomInt = rand.nextInt(5);
			obstacles.spawnPower(randomInt + 1);
			counter = 0;
		}
		counter++;
	}

	/**
	 * Action Listener for Left Button. Tries to move left, if there is a lane there.
	 */
	private void left () {
		bus.moveLeft();
	}

	/**
	 * Action Listener for Right Button. Tries to move right, if there is a lane there.
	 */
	private void right () {
		bus.moveRight();
	}

	/**
	 * Background graphics for the game.
	 * Internal class BackgroundView to update background scrolling
	 *
	 * @author 		Daniel Edsinger
	 * @version		0.3
	 * @since		2015-10-06
	 */
	public class Background {

		/**
		 * View holder
		 */
		private BackgroundView view;

		/**
		 * Variables for screen
		 */
		private int height;
		private int width;
		private float yPos;
		private final float scrollSpeed;

		/**
		 * Initializes the position of background image and screen size
		 *
		 * @param metrics - resolution of screen
		 */
		public Background (DisplayMetrics metrics) {

			height = metrics.heightPixels;
			width = metrics.widthPixels;
			yPos -=height;
			scrollSpeed = height/80.0f;
		}

		/**
		 * Public getter for backgound view, also initializes it.
		 *
		 * @param activity - current active activity.
		 * @return view of background.
		 */
		public View getView(Activity activity) {
			view = new BackgroundView(activity);
			return view;
		}

		/**
		 * Move the background down on screen looping forever
		 */
		public void backgroundMovement() {
			if(yPos + scrollSpeed >= 0.0f) {
				float tempPos = yPos - scrollSpeed;
				yPos = -(height + tempPos);
			}else{
				yPos += scrollSpeed;
			}

			view.invalidate();
		}

		/**
		 * Inner class for drawing the background
		 *
		 * @author 		Daniel Edsinger
		 * @version		0.3
		 * @since		2015-10-06
		 */
		private class BackgroundView extends View {

			private Bitmap b;
			private int offset;

			/**
			 * Initializes a view for the background.
			 *
			 * @param context - current context to render in.
			 */
			public BackgroundView(Context context) {
				super(context);

				BitmapDrawable backgroundImage = (BitmapDrawable)getResources().getDrawable(R.drawable.backgroundsmall);
				float bmapHeight = backgroundImage.getBitmap().getHeight();
				float bmapWidth = backgroundImage.getBitmap().getWidth();

				float ratioMultiplier = height / (bmapHeight/2);
				int newBmapWidth = (int) (bmapWidth*ratioMultiplier);
				int newBmapHeight = (int) (bmapHeight*ratioMultiplier);
				offset = (width - newBmapWidth) / 2;

				b = Bitmap.createScaledBitmap(backgroundImage.getBitmap(), newBmapWidth, newBmapHeight, false);
			}

			@Override
			protected  void onDraw(Canvas canvas) {
				super.onDraw(canvas);

				canvas.drawBitmap(b, offset, yPos, null);
			}
		}
	}
	/**
	 * Public getter for background.
	 *
	 * @return - instance of background.
	 */
	public Background getBackground() { return background;}

	public Obstacles getObstacles() {return obstacles;}

	public boolean isPaused() {
		return paused;
	}

	/**
	 * The game stops and the game over screen pops up.
	 */
	public void gameOver(){
		paused = true;
		for(Button button : playButtons) {
			button.setVisibility(View.GONE);
		}
		for(Button button : gameOverButtons) {
			button.setVisibility(View.VISIBLE);
		}
		textGameOver.setVisibility(View.VISIBLE);
	}

	/**
	 * Resets the game, for another playthrough.
	 */
	public void startNewGame() {
		bus.reset();
		obstacles.reset();
		eventQueue.clear();

		for(Button button : gameOverButtons) {
			button.setVisibility(View.GONE);
		}
		for(Button button : playButtons) {
			button.setVisibility(View.VISIBLE);
		}

		textGameOver.setVisibility(View.GONE);
		paused = false;
	}

	/**
	 * Finishes the activity and quits the application.
	 */
	public void quitGame() {
		getActivity().finish();
		System.exit(0);
	}
}

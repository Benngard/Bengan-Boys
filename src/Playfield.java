package com.example.hyperion;

import com.example.hyprion.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Playfield Fragment, initializes GUI for Playfield; Left- Right, Fire Button.
 * 
 * @author 		Mattias Benngard, Daniel Edsinger, Ola Andersson
 * @version		1.1
 * @since		2015-10-06
 */

public class Playfield extends Fragment
{
	private Bus bus;
	private Background background;
	private Obstacles obstacles;
	private int counter;

	private final List<LightingBolt> lightingBolts = new ArrayList<>();
	
	private static final int BUTTON_LEFT = R.id.button_left;
	private static Button buttonLeft;
	
	private static final int BUTTON_RIGHT = R.id.button_right;
	private static Button buttonRight;
	
	private final static int BUTTON_FIRE = R.id.button_fire;
	private static Button buttonFire;	
	
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
		
		container.addView(background.getView(getActivity()));
		container.addView(bus.getView(getActivity()));
		container.addView(obstacles.getView(getActivity()));
		container.addView(bus.getPowerComponent().getView(getActivity()));
		
		final View view = inflater.inflate(R.layout.fragment_playfield, container, false);

		buttonLeft = (Button) view.findViewById(BUTTON_LEFT);
		buttonLeft.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				left ();
			}
		});
		
		buttonRight = (Button) view.findViewById(BUTTON_RIGHT);
		buttonRight.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				right ();
			}
		});
		
		buttonFire = (Button) view.findViewById(BUTTON_FIRE);
		buttonFire.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fire(container);
			}
		});


		return view;
	}
	
	/**
	 * Public getter for bus.
	 * 
	 * @return - instance of bus.
	 */
	public Bus getBus() {
		return bus;
	}
	
	/**
	 * Action Listener for Fire Button. Tries to fire lightning bolts, if the power is enough for the bus.
	 */
	private void fire (ViewGroup v) {
	  	if (bus.getPowerComponent().firePower()) {
        		LightingBolt lb = new LightingBolt();
            		lightingBolts.add(lb);
        	 	v.addView(lb.getView(getActivity()));
        	}
	}
	
	/**
     	* Updates the lightingBolts on the screen.
     	*/
	public void updateLightingBolts(){
        	for(LightingBolt lb: lightingBolts){
            		if(lb.bottom < 200){
        			 lb.gone(lb.getView(getActivity()));
            		}
            		else { lb.update(); }
        	}
    	}
    	
    	/**
     	* Spawns a battery on a random lane so the bus can get more energy
	*/
    	public void spawnBattery(){
        	if (counter == 200) {
	            Random rand = new Random();
	            int randomInt = rand.nextInt(5);
	            Energy energy = new Energy(randomInt);
	            batteryCells.add(energy);
	            getViewGroup().addView(energy.getView(getActivity()));
	            counter = 0;
        	}
		counter++;
    	}
    	
    	/**
     	* Updates the batteryCells on the screen.
     	*/
    	public void updateBatteryCells(){
        	for(Energy e: batteryCells){
            		if(e.bottom < -100){
                		e.gone(e.getView(getActivity()));
            		}
            		else { e.update(); }
        	}
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
            scrollSpeed = height/100.0f;
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
                float tempPos = yPos + scrollSpeed;
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
			public void onDraw(Canvas canvas) {
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
}

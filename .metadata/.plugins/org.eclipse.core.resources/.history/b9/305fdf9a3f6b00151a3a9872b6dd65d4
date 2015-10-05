package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Power Component for Buss, handles all display and handling of Power for the Buss
 * Internal class PowerComponentView to update PowerBar
 * 
 * @author 		Mattias Benngård
 * @version		1.0
 * @since		2015-09-30
 */
public class PowerComponent
{
	/**
	 * View Holder, keeps reference to invoke `.invalidate()` for new data
	 */
	private PowerComponentView view;
	
	/**
	 * Power Modifiers
	 */
	private static final int POWER_GLOBE_VALUE 		= 25;
	private static final int POWER_FIRE_COST 		= 5;
	private static final int POWER_DRAIN 			= 10;
	
	/**
	 * Power Value
	 */
	private static final int POWER_MAX 				= 100;
	private static int power 						= POWER_MAX;

	/**
	 * Initializes the view for power bar and returns it. If it already has been initialized then just returns it.
	 * 
	 * @param activity - current active activity to render in
	 * @return view object of power bar
	 */
	public View getView (Activity activity) {
		if (view == null) {
			view = new PowerComponentView(activity);
		}
		return view;
	}
		
	/**
	 * Adds power from a power globe. Makes sure to never go over POWER_MAX constant.
	 */
	public void addPower () {
		power += POWER_GLOBE_VALUE;
		power = (power > POWER_MAX) ? POWER_MAX : power;
		view.invalidate();
	}
	
	/**
	 * Returns if the buss has enough power to fire a shot. If it has enough it will also consume the
	 * amount of power specified in POWER_FIRE_COST constant.
	 * 
	 * @return 	if current power level was more or equal to then the value of POWER_FIRE_COST constant.
	 */
	public boolean firePower () {
		if (power < POWER_FIRE_COST) {
			return false;
		}
		
		power -= POWER_FIRE_COST;
		view.invalidate();
		return true;
	}
	
	/**
	 * Drains power from the buss, uses POWER_DRAIN constant to subtract from power.
	 * 
	 * @return	if the power has been completely drained or not
	 */
	public boolean drainPower () {
		if (power < POWER_DRAIN) {
			return false;
		}
		
		power -= POWER_DRAIN;
		view.invalidate();
		return true;
	}
	
	/**
	 * Inner class responsible for drawing the PowerBar
	 * 
	 * @author 		Mattias Benngård
	 * @version		1.0
	 * @since		2015-09-30
	 */
	private class PowerComponentView extends View
	{	
		/**
		 * Power GUI
		 */
		private final Paint paint 				= new Paint();
		private final int GUI_BAR_X 			= 100;
		private final int GUI_BAR_Y 			= 10;
		private final int GUI_BAR_HEIGHT 		= 30;
		private final int GUI_BAR_WIDTH_MAX 	= 200;		
		
		private final Rect powerFrame = new Rect (GUI_BAR_X-1, GUI_BAR_Y-1, GUI_BAR_X + GUI_BAR_WIDTH_MAX+1, GUI_BAR_HEIGHT+1);
		private final Rect powerBar = new Rect (GUI_BAR_X, GUI_BAR_Y, GUI_BAR_X + GUI_BAR_WIDTH_MAX, GUI_BAR_HEIGHT);
		
		/**
		 * Initializes a new PowerComponentView
		 * @param context - in which context to render in
		 */
		public PowerComponentView (Context context) {
			super (context);
		}
		
		@Override
		public void onDraw (Canvas canvas) {
			super.onDraw(canvas);
			
			// Frame
			paint.setColor (Color.BLACK);
			canvas.drawRect (powerFrame, paint);
			
			// Bar
			paint.setColor (Color.rgb (255, 255, 0));
			powerBar.right = GUI_BAR_X + (int)(((double)power/(double)POWER_MAX) * (double)GUI_BAR_WIDTH_MAX);
			canvas.drawRect (powerBar, paint);
		}
	}
}
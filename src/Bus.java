package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.hyprion.R;

/**
 * Initializes a bus on the background.
 *
 * @author 		Ola Andersson, Daniel Edsinger
 * @version		0.3
 * @since		2015-10-13
 */

public class Bus
{
    private BusView view;

    private final PowerComponent powerComponent = new PowerComponent ();
    private Rect busRect;

    /**
     * Public getter for power component.
     * @return - instance of power component.
     */
    public PowerComponent getPowerComponent() {
        return powerComponent;
    }

    public static int height;
    public static int width;
    public static int lane;

    private int laneLength;
    private int pixelLength;

    public static final double MOVE_SCALE_FACTOR = 0.15;

    public Bus(DisplayMetrics metrics){
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        double roadRatio = 28.0f/ 320.0f;
        laneLength = (int)(roadRatio * height);
        pixelLength = laneLength/28;
        lane = 3;
    }


    /**
     * Is being called after moving the bus to update graphics.
     */
    public void update() {
        view.invalidate();
    }

    /**
     *
     * @param activity
     * @return creates a new BusView object if view is null.
     */
    public View getView(Activity activity){
        if(view == null){
            view = new BusView(activity);
        }
        return view;
    }

    /**
     * Moves the bus one step to the left if canMoveLeft returns true.
     */
    public void moveLeft(){
        if(canMoveLeft()){
            busRect.set(busRect.left - laneLength, busRect.top, busRect.right - laneLength, busRect.bottom);
            lane -= 1;
            update();
        }
    }

    /**
     * Moves the bus one step to the right if canMoveRight returns true.
     */
    public void moveRight(){
        if(canMoveRight()){
            busRect.set(busRect.left + laneLength, busRect.top, busRect.right + laneLength, busRect.bottom);
            lane += 1;
            update();
        }
    }

    /**
     * Checks if the bus can move left.
     * @return true if it can, otherwise false.
     */
    private boolean canMoveLeft(){
        return lane == 1 ? false : true;
    }

    /**
     * Checks if the bus can move right.
     * @return true if it can, otherwise false.
     */
    private boolean canMoveRight(){
        return lane == 5 ? false : true;
    }

    private class BusView extends View {

        private Drawable drawableBus;

        /**
         * Initializes a lightingbolt on the background.
         *
         * @param context - current context to render in.
         */

        private BusView(Context context) {
            super(context);
            drawableBus = getResources().getDrawable(R.drawable.bus);

            float drawableHeight = drawableBus.getIntrinsicHeight();
            double expectedRatio = 50.0f/ 320.0f;
            double realRatio = drawableHeight / height;
            double ratioMultiplier = expectedRatio/realRatio;

            // New size for rocks to match screen size
            int newDrawablesHeight = (int) (drawableHeight*ratioMultiplier);

            // Placing busRect to right position
            int left = (int)((width/2) - (0.5 * laneLength));
            int right = (int)((width/2) + (0.5 * laneLength));
            busRect = new Rect(left, (height-laneLength) - newDrawablesHeight, right, height-laneLength);
        }

        /**
         * Draws the bus with the given params.
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawableBus.setBounds(busRect);
            drawableBus.draw(canvas);
        }
    }
}

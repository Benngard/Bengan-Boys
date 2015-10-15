package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Initializes a bus on the background.
 *
 * @author 		Ola Andersson
 * @version		1.0
 * @since		2015-09-29
 */

public class Bus
{
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

    private double left;
    private final double top;
    private double right;
    private final double bottom;

    private final double LEFT_SCALE_FACTOR = 0.4629629;
    private final double TOP_SCALE_FACTOR = 0.7291666;
    private final double RIGHT_SCALE_FACTOR = 0.5555556;
    private final double BOTTOM_SCALE_FACTOR = 0.8333333;

    public static final double MOVE_SCALE_FACTOR = 0.15;


    private BusView view;

    public Bus(DisplayMetrics metrics){
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        left = width * LEFT_SCALE_FACTOR;
        top = height * TOP_SCALE_FACTOR;
        right = width * RIGHT_SCALE_FACTOR;
        bottom = height * BOTTOM_SCALE_FACTOR;
        busRect = new Rect((int)left,(int)top,(int)right,(int)bottom);
        lane = 3;
    }
    
    public Rect getRect(){
        return busRect;
    }

    /**
     * Is being called after moving the bus to update graphics.
     */
    public void update() {
        view.invalidate();
    }

    /**
     *
     * @return returns the lane.
     */
    public int getLane(){
        return lane;
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
            left -= width * MOVE_SCALE_FACTOR;
            right -= width * MOVE_SCALE_FACTOR;
            lane -= 1;
            update();
        }
    }

    /**
     * Moves the bus one step to the right if canMoveRight returns true.
     */
    public void moveRight(){
        if(canMoveRight()){
            right += width * MOVE_SCALE_FACTOR;
            left += width * MOVE_SCALE_FACTOR;
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
    
    /**
     * Inner class for drawing Bus.
     *
     * @author Ola Andersson
     * @version 1.0
     * @since 2015-09-29
     */

    private class BusView extends View {

        private Drawable drawableBus;

        /**
         * Initializes a lightingbolt on the background.
         *
         * @param context - current context to render in.
         */

        private BusView(Context context) {
            super(context);
            drawableBus = getResources().getDrawable(R.drawable.bussstor);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        /**
         * Draws the bus with the given params.
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            busRect.set((int) left, (int) top, (int) right, (int) bottom);
            drawableBus.setBounds(busRect);
            drawableBus.draw(canvas);
        }
    }
}

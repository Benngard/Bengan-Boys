package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Spawns a rect on the background filled with energy
 *
 * @author 		Ola Andersson
 * @version		1.0
 * @since		2015-10-13
 */
public class Energy {

    private EnergyView view;

    private final double left;
    private double top;
    private final double right;
    public double bottom;

    private Rect energyRect;

    private final double LEFT_SCALE_FACTOR = 0.176592;
    private final double TOP_SCALE_FACTOR = 0.6510417;
    private final double RIGHT_SCALE_FACTOR = 0.242592;
    private final double BOTTOM_SCALE_FACTOR = 0.703125;
    private final double UPDATE_RATIO = 20;

    /**
     *Initializes an energy animation.
     */

    public Energy(int lane){
        int width = Bus.width;
        int height = Bus.height;
        double moveScaleFactor = Bus.MOVE_SCALE_FACTOR;

        left = ((width * moveScaleFactor) * lane) + (width * LEFT_SCALE_FACTOR);
        top = -100;
        right = ((width * moveScaleFactor) * lane) + (width * RIGHT_SCALE_FACTOR);
        bottom = 0;
        energyRect = new Rect((int)left,(int)top,(int)right,(int)bottom);
    }

    /**
     * Sets the giving view to gone.
     * @param lbv - make the param lbv gone.
     */
    public void gone(View lbv){
        lbv.setVisibility(View.GONE);
    }

    /**
     * Public updater for the animation.
     */
    public void update() {
        top = top + UPDATE_RATIO;
        bottom = bottom + UPDATE_RATIO;
        view.invalidate();
    }

    /**
     *
     * @param activity - current activity to spawn new energy.
     * @return - a new instance of a battery.
     */
    public View getView(Activity activity){
        if(view == null){
            view = new EnergyView(activity);
        }
        return view;
    }

    /**
     * Public method to get a Rect.
     *
     * @return - a battery.
     */
    public Rect getRect(){
        return energyRect;
    }

    /**
     * Inner class for drawing energy.
     *
     * @author Ola Andersson
     * @version 1.0
     * @since 2015-09-29
     */
    private class EnergyView extends View {

        private Drawable drawableLB;

        /**
         * Initializes a lightingbolt on the background.
         *
         * @param context - current context to render in.
         */
        public EnergyView(Context context){
            super(context);
            drawableLB = getResources().getDrawable(R.drawable.battery);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        /**
         * Draws the lightingbolt with the given params.
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            energyRect.set((int) left, (int) top, (int) right, (int) bottom);
            drawableLB.setBounds(energyRect);
            drawableLB.draw(canvas);
        }
    }
}

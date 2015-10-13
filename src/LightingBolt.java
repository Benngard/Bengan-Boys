package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Spawns a lightingbolt on a specific lane
 *
 * @author 		Ola Andersson
 * @version		0.3
 * @since		2015-09-29
 */
public class LightingBolt {

    private LightingBoltView view;

    private final double left;
    private double top;
    private final double right;
    public double bottom;

    private Rect lightingRect;

    private final double LEFT_SCALE_FACTOR = 0.176592;
    private final double TOP_SCALE_FACTOR = 0.6510417;
    private final double RIGHT_SCALE_FACTOR = 0.242592;
    private final double BOTTOM_SCALE_FACTOR = 0.703125;
    private final double UPDATE_RATIO = 20;

    /**
     *Initializes a lightingbolt animation.
     */

    public LightingBolt(){
        int lane = Bus.lane-1;
        int width = Bus.width;
        int height = Bus.height;
        double moveScaleFactor = Bus.MOVE_SCALE_FACTOR;

        left = ((width * moveScaleFactor) * lane) + (width * LEFT_SCALE_FACTOR);
        top = height * TOP_SCALE_FACTOR;
        right = ((width * moveScaleFactor) * lane) + (width * RIGHT_SCALE_FACTOR);
        bottom = height * BOTTOM_SCALE_FACTOR;
        lightingRect = new Rect((int)left,(int)top,(int)right,(int)bottom);
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
        top = top - UPDATE_RATIO;
        bottom = bottom - UPDATE_RATIO;
        view.invalidate();
    }

    /**
     *
     * @param activity - current activity to spawn a new lightingbolt.
     * @return - a new instance of a lightigbolt.
     */
    public View getView(Activity activity){
        if(view == null){
            view = new LightingBoltView(activity);
        }
        return view;
    }

    /**
     * Public method to get a Rect.
     *
     * @return - a lightingRect.
     */
    public Rect getRect(){
        return lightingRect;
    }

    /**
     * Inner class for drawing lightingbolts.
     *
     * @author Ola Andersson
     * @version 0.3
     * @since 2015-09-29
     */
    private class LightingBoltView extends View {

        private Drawable drawableLB;

        /**
         * Initializes a lightingbolt on the background.
         *
         * @param context - current context to render in.
         */
        public LightingBoltView(Context context){
            super(context);
            drawableLB = getResources().getDrawable(R.drawable.lighting);
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
            lightingRect.set((int)left,(int)top,(int)right,(int)bottom);
            drawableLB.setBounds(lightingRect);
            drawableLB.draw(canvas);
        }
    }
}

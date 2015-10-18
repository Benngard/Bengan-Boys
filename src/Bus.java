package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.hyprion.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Initializes a bus on the background.
 *
 * @author 		Ola Andersson, Daniel Edsinger
 * @version		0.4
 * @since		2015-10-13
 */

public class Bus
{
    private BusView view;

    private final PowerComponent powerComponent = new PowerComponent ();
    private ArrayList<LightingBolt> lightingBolts;
    private Rect busRect;
    private Rect lightningRect;

    /**
     * Public getter for power component.
     * @return - instance of power component.
     */
    public PowerComponent getPowerComponent() {
        return powerComponent;
    }

    /**
     * Public getter for lists of lightnings on screen.
     * @return - list of lightnings.
     */
    public ArrayList<LightingBolt> getLightingBolts() { return  lightingBolts; }

    private int height;
    private int width;
    private int lane;
    private double laneLength;
    private double pixelLength;
    private double scrollSpeed;

    // Lane positions
    private int laneOnePos;
    private int laneOneEndPos;
    private int laneTwoPos;
    private int laneTwoEndPos;
    private int laneThreePos;
    private int laneThreeEndPos;
    private int laneFourPos;
    private int laneFourEndPos;
    private int laneFivePos;
    private int laneFiveEndPos;

    /**
     * Calculate where the positions of the lanes are.
     *
     * @param metrics - screen resolution.
     */
    public Bus(DisplayMetrics metrics){
        lightingBolts = new ArrayList<>();
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        scrollSpeed = height/50.0f;
        lane = 3;

        // Calculate where spawnposition is for each lane
        double roadRatio = 28.0f/ 320.0f;
        laneLength = roadRatio * height;
        double pixelLength = (laneLength/28);

        laneOnePos = (int) ((width/2) - (2.5*laneLength) + pixelLength);
        laneOneEndPos = (int) ((width/2) - (1.5*laneLength) - pixelLength);

        laneTwoPos = (int) ((width/2) - (1.5*laneLength) + pixelLength);
        laneTwoEndPos = (int) ((width/2) - (0.5*laneLength)- pixelLength);

        laneThreePos = (int) ((width/2) - (0.5*laneLength)+ pixelLength);
        laneThreeEndPos = (int) ((width/2) + (0.5*laneLength) - pixelLength);

        laneFourPos = (int) ((width/2) + (0.5*laneLength) + pixelLength);
        laneFourEndPos = (int) ((width/2) + (1.5*laneLength)- pixelLength);

        laneFivePos = (int) ((width/2) + (1.5*laneLength) + pixelLength);
        laneFiveEndPos = (int) ((width/2) + (2.5*laneLength) - pixelLength);

    }


    /**
     * Is being called after moving the bus to update graphics.
     */
    public void update() {
        //Iterator for invulnerable
        for (Iterator iterator = lightingBolts.iterator(); iterator.hasNext();)
        {
            LightingBolt obstacle = (LightingBolt) iterator.next();
            obstacle.moveObstacle(scrollSpeed);

            if(obstacle.getRect().bottom < 0) {
                iterator.remove();
            }
        }
        view.invalidate();
    }

    /**
     * Public getter for bus view, also initializes it.
     *
     * @param activity - current active activity.
     * @return view of bus.
     */
    public View getView(Activity activity){
        if(view == null){
            view = new BusView(activity);
        }
        return view;
    }

    /**
     * Get the Rect of the bus
     * @return - The rect on the bus
     */
    public Rect getRect() {
        return busRect;
    }

    /**
     * Spawns a lightning on the lane the bus is on if there is enough power.
     */
    public void spawnLightning() {
        if(powerComponent.firePower()){
            switch(lane) {
                case 1:
                    lightningRect.set(laneOnePos, lightningRect.top, laneOneEndPos, lightningRect.bottom);
                    lightingBolts.add(new LightingBolt( new Rect(lightningRect)) );
                    break;

                case 2:
                    lightningRect.set(laneTwoPos, lightningRect.top, laneTwoEndPos, lightningRect.bottom);
                    lightingBolts.add(new LightingBolt( new Rect(lightningRect)) );
                    break;

                case 3:
                    lightningRect.set(laneThreePos, lightningRect.top, laneThreeEndPos, lightningRect.bottom);
                    lightingBolts.add(new LightingBolt( new Rect(lightningRect)) );
                    break;

                case 4:
                    lightningRect.set(laneFourPos, lightningRect.top, laneFourEndPos, lightningRect.bottom);
                    lightingBolts.add(new LightingBolt( new Rect(lightningRect)) );
                    break;

                case 5:
                    lightningRect.set(laneFivePos, lightningRect.top, laneFiveEndPos, lightningRect.bottom);
                    lightingBolts.add(new LightingBolt( new Rect(lightningRect)) );
                    break;
            }
        }
    }

    /**
     * Moves the bus one step to the left if canMoveLeft returns true.
     */
    public void moveLeft(){
        if(canMoveLeft()){
            busRect.set((int) (busRect.left - laneLength), busRect.top, (int)(busRect.right - laneLength), busRect.bottom);
            lane -= 1;
        }
    }

    /**
     * Moves the bus one step to the right if canMoveRight returns true.
     */
    public void moveRight(){
        if(canMoveRight()){
            busRect.set((int) (busRect.left + laneLength), busRect.top, (int)(busRect.right + laneLength), busRect.bottom);
            lane += 1;
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
        private Drawable drawableLightning;

        /**
         * Initializes a lightingbolt on the background.
         *
         * @param context - current context to render in.
         */
        private BusView(Context context) {
            super(context);

            createBusRect();
            createLightningRect();
        }

        /**
         * Create a Rect for the bus to fit in.
         */
        private void createBusRect() {
            drawableBus = getResources().getDrawable(R.drawable.bus);

            float drawableHeight = drawableBus.getIntrinsicHeight();
            double expectedRatio = 50.0f/ 320.0f;
            double realRatio = drawableHeight / height;
            double ratioMultiplier = expectedRatio/realRatio;

            // New size for bus to match screen size
            int newDrawablesHeight = (int) (drawableHeight*ratioMultiplier);

            // Placing busRect to right position
            busRect = new Rect(laneThreePos,(int) ((height-laneLength) - newDrawablesHeight), laneThreeEndPos, (int)(height-laneLength));
        }

        /**
         * Creates the Rect to fit lightning graphics in.
         */
        public void createLightningRect() {
            drawableLightning = getResources().getDrawable(R.drawable.lightning);

            float drawableHeight = drawableLightning.getIntrinsicHeight();
            double expectedRatio = 19.0f/ 320.0f;
            double realRatio = drawableHeight / height;
            double ratioMultiplier = expectedRatio/realRatio;

            // New size for lightnings to match screen size
            int newDrawablesHeight = (int) (drawableHeight*ratioMultiplier);

            // Placing lightningsRect to right position
            lightningRect = new Rect(0,(int) ((height-laneLength*2) - newDrawablesHeight), 0, (int)(height-laneLength*2));
        }

        /**
         * Draws the bus and the lightningbolts.
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawableBus.setBounds(busRect);
            drawableBus.draw(canvas);

            for(LightingBolt lightning : lightingBolts) {
                drawableLightning.setBounds(lightning.getRect());
                drawableLightning.draw(canvas);
            }
        }
    }
}

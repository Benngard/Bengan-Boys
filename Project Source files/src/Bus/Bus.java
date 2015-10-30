package com.example.hyperion.Bus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.hyperion.Obstacle.LanePosition;
import com.example.hyperion.Obstacle.LightingBolt;
import com.example.hyprion.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Initializes a bus on the background.
 *
 * @author 		Ola Andersson, Daniel Edsinger
 * @version		2.1
 * @since		2015-10-13
 */

public class Bus
{
    private BusView view;

    private final PowerComponent powerComponent         = new PowerComponent ();
    private final ArrayList<LightingBolt> lightingBolts = new ArrayList<>();
    private Rect busRect;
    private Rect lightningRect;
    private Drawable drawableLightning;

    private int height;
    private int lane                                    = 3;
    private double laneLength;
    private int scrollSpeed;

    private ArrayList<LanePosition> lanePositions;

    /**
     * Calculate where the positions of the lanes are.
     *
     * @param metrics - screen resolution.
     */
    public Bus (DisplayMetrics metrics, ArrayList<LanePosition> lanePositions){
        this.lanePositions = lanePositions;
        height = metrics.heightPixels;
        scrollSpeed = (int) (height/50.0f);
        double roadRatio = 28.0f/ 320.0f;
        laneLength = roadRatio * height;
    }

    /**
     * Public getter for power component.
     *
     * @return - instance of power component.
     */
    public PowerComponent getPowerComponent() {
        return powerComponent;
    }

    /**
     * Public getter for lists of lightnings on screen.
     *
     * @return - list of lightning bolts.
     */
    public ArrayList<LightingBolt> getLightingBolts() {
        return lightingBolts;
    }

    /**
     * Is being called after moving the bus to update graphics.
     */
    public void update() {
        //Iterator for invulnerable
        for (Iterator iterator = lightingBolts.iterator(); iterator.hasNext();)
        {
            LightingBolt obstacle = (LightingBolt) iterator.next();
            obstacle.moveObstacle(-scrollSpeed);

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
            setRectToLane(lightningRect);
            lightingBolts.add(new LightingBolt( new Rect(lightningRect), drawableLightning) );
        }
    }

    /**
     * Moves the bus one step to the left if canMoveLeft returns true.
     */
    public void moveLeft(){
        if(canMoveLeft()){
            lane -= 1;
            setRectToLane(busRect);
        }
    }

    /**
     * Moves the bus one step to the right if canMoveRight returns true.
     */
    public void moveRight(){
        if(canMoveRight()){
            lane += 1;
            setRectToLane(busRect);
        }
    }

    /**
     * Reposition rect to the position of the lane which the bus is on.
     * @param rect - The rect to reposition
     */
    private void setRectToLane(Rect rect) {
        rect.set(lanePositions.get(lane-1).getStart(), rect.top, lanePositions.get(lane-1).getEnd(), rect.bottom);
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
     * Reset all the components in this class to start position
     */
    public void reset() {
        lightingBolts.clear();
        powerComponent.fullPower();
        lane = 3;
        setRectToLane(busRect);
    }

    /**
     * Inner class responsible for drawing the Bus and Lightning Bolts
     *
     * @author 		Ola Andersson, Daniel Edsinger
     * @version		2.0
     * @since		2015-10-13
     */
    private class BusView extends View
    {
        private Drawable drawableBus;

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
            busRect = new Rect(lanePositions.get(2).getStart(), (int) ((height-(laneLength*2)) - newDrawablesHeight), lanePositions.get(2).getEnd(), (int)(height-(laneLength*2)));
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
            lightningRect = new Rect(0,(int) ((height-laneLength*3) - newDrawablesHeight), 0, (int)(height-laneLength*3));
        }

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
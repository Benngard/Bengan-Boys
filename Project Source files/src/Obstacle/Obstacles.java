package com.example.hyperion.Obstacle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.hyperion.ObstacleObjects.CollectibleObject;
import com.example.hyperion.ObstacleObjects.InvulnerableObject;

import com.example.hyperion.ObstacleObjects.Power;
import com.example.hyprion.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Initializes all the logic for different obstacles, placement, speed and the graphics
 *
 * @author 		Daniel Edsinger
 * @version		2.1
 * @since		2015-10-14
 */
 
public class Obstacles {

    // View Holder
    private View view;

    /**
     * Different Rects for different obstacles
     */
    private Rect rocksRectL;
    private Rect rocksRectR;

    private Rect fenceRectL;
    private Rect fenceRectR;

    private Rect powerRect;

    /**
     * Drawables for every obstacle.
     */
    Drawable drawableRocks;
    Drawable drawableFenceLeft;
    Drawable drawableFenceRight;
    Drawable drawablePower;

    /**
     * List of invulnerable objects
     */
    private ArrayList<InvulnerableObject> invulnerables = new ArrayList<>();
    private ArrayList<CollectibleObject> collectibles = new ArrayList<>();

    private int height; // Screen height in pixels
    private int width;  // Screen width in pixels
    private int obstacleMoveSpeed;
    private ArrayList<LanePosition> lanePositions;

    /**
     * Get the resolution of the screen and initialize the scrollspeed
     *
     * @param metrics - resolution of screen
     */
    public Obstacles (DisplayMetrics metrics, ArrayList<LanePosition> lanePositions) {
        this.lanePositions = lanePositions;

        height = metrics.heightPixels;
        width = metrics.widthPixels;
        obstacleMoveSpeed = (int) (height/80.0f);
    }

    /**
     * Public getter for obstacles view, also initializes it.
     *
     * @param activity - current active activity.
     * @return view of obstacles.
     */
    public View getView(Activity activity) {
        if(view == null){
            view = new ObstaclesView(activity);
        }
        return view;
    }

    /**
     * Public getter for list of InvulnerableObjects on screen
     *
     * @return - the list of invulnerable
     */
    public ArrayList<InvulnerableObject> getInvulnerables(){
        return invulnerables;
    }

    /**
     * Public getter for list of CollectiblesObjects on screen
     *
     * @return - the list of collectibles
     */
    public ArrayList<CollectibleObject> getCollectibles(){
        return collectibles;
    }

    /**
     * Spawn a rock on the left side of the road.
     */
    public void spawnRockLeft() {
        invulnerables.add(new InvulnerableObject(new Rect(rocksRectL), drawableRocks));
    }

    /**
     * Spawn a rock one the right side of the road.
     */
    public void spawnRockRight() {
        invulnerables.add(new InvulnerableObject(new Rect(rocksRectR), drawableRocks));
    }

    /**
     * Spawn a fence on the left side of the road.
     */
    public void spawnFenceLeft() {
        invulnerables.add(new InvulnerableObject(new Rect(fenceRectL), drawableFenceLeft));
    }

    /**
     * Spawn a fence on the right size of the road.
     */
    public void spawnFenceRight() {
        invulnerables.add(new InvulnerableObject(new Rect(fenceRectR), drawableFenceRight));
    }

    /**
     * Spawn a power on a lane.
     *
     * @param lane - the lane to spawn power
     */
    public void spawnPower(int lane) {
        if (lane >= 1 && lane <= 5 ) {
            Rect tmp = new Rect(powerRect);
            spawnOnLane(lane, tmp);

            collectibles.add(new Power(tmp, drawablePower));
        }
    }

    /**
     * Mutator to set which lane to spawn object on.
     *
     * @param lane - on which lane to spawn
     * @param rect - mutate to set this Rect's bounds to follow a specific lane
     */
    private void spawnOnLane(int lane, Rect rect) {
        rect.set(lanePositions.get(lane-1).getStart(), rect.top, lanePositions.get(lane-1).getEnd(), 0);
    }

    /**
     * Move all obstacles down the road towards the player. Removes obstacles if not on screen anymore.
     */
    public void obstaclesMovement() {
        //Iterator for invulnerable
        for (Iterator iterator = invulnerables.iterator(); iterator.hasNext();)
        {
            InvulnerableObject obstacle = (InvulnerableObject) iterator.next();
            obstacle.moveObstacle(obstacleMoveSpeed);

            if(obstacle.getRect().top > height) {
                iterator.remove();
            }
        }

        //Iterator for collectibles
        for (Iterator iterator = collectibles.iterator(); iterator.hasNext();)
        {
            CollectibleObject obstacle = (CollectibleObject) iterator.next();
            obstacle.moveObstacle(obstacleMoveSpeed);

            if(obstacle.getRect().top > height) {
                iterator.remove();
            }
        }

        view.invalidate();
    }
    
    /**
     * Reset all the components in this class to start position
     */
    public void reset() {
        invulnerables.clear();
        collectibles.clear();
    }

    /**
     * Inner class for drawing the obstacles.
     *
     * @author 		Daniel Edsinger
     * @version		0.3
     * @since		2015-10-13
     */
    private class ObstaclesView extends View
    {
        /**
         * Initializes a view for the obstacles.
         *
         * @param context - current context to render in.
         */
        public ObstaclesView(Context context) {
            super(context);

            createRocksRect();
            createFenceRect();
            createPowerRect();
        }

        /**
         * Create and calculate the size of the rocks to match screen and initialize the Rect to be placed
         * on the right or left side of the road.
         */
        private void createRocksRect(){
            drawableRocks = getResources().getDrawable(R.drawable.rocksbig);

            float drawableHeight = drawableRocks.getIntrinsicHeight();
            float drawableWidth = drawableRocks.getIntrinsicWidth();
            double expectedRatio = 38.0f/ 320.0f;
            double realRatio = drawableHeight / height;
            double ratioMultiplier = expectedRatio/realRatio;

            // New size for rocks to match screen size
            int newDrawablesHeight = (int) (drawableHeight*ratioMultiplier);
            int newDrawablesWidth = (int) (drawableWidth*ratioMultiplier);

            // Placing rocksRect to right position
            rocksRectL = new Rect( lanePositions.get(0).getStart(), - newDrawablesHeight, lanePositions.get(0).getStart() + newDrawablesWidth, 0);
            rocksRectR = new Rect( lanePositions.get(1).getStart(), - newDrawablesHeight, lanePositions.get(1).getStart() + newDrawablesWidth, 0);
        }

        /**
         * Create and calculate the size of the fence to match screen and initialize the Rect to be placed
         * on the right or left side of the road.
         */
        private void createFenceRect(){
            drawableFenceLeft = getResources().getDrawable(R.drawable.fenceleft);
            drawableFenceRight = getResources().getDrawable(R.drawable.fenceright);

            float drawableHeight = drawableRocks.getIntrinsicHeight();
            float drawableWidth = drawableRocks.getIntrinsicWidth();
            double expectedRatio = 35.0f/ 320.0f;
            double realRatio = drawableHeight / height;
            double ratioMultiplier = expectedRatio/realRatio;

            // New size for rocks to match screen size
            int newDrawablesHeight = (int) (drawableHeight*ratioMultiplier);
            int newDrawablesWidth = (int) (drawableWidth*ratioMultiplier);

            // Placing rocksRect to right position
            fenceRectL = new Rect( lanePositions.get(0).getEnd() - newDrawablesWidth, - newDrawablesHeight, lanePositions.get(0).getEnd(), 0);
            fenceRectR = new Rect( lanePositions.get(4).getStart(), - newDrawablesHeight, lanePositions.get(4).getStart() + newDrawablesWidth, 0);
        }

        /**
         * Create and calculate the size of the power to match screen and initialize the Rect to be placed
         * above the screen. The lane to spawn on is decided later.
         */
        public void createPowerRect() {
            drawablePower = getResources().getDrawable(R.drawable.power);

            float drawableHeight = drawableRocks.getIntrinsicHeight();
            double expectedRatio = 22.0f/ 320.0f;
            double realRatio = drawableHeight / height;
            double ratioMultiplier = expectedRatio/realRatio;

            // New size for power to match screen size
            int newDrawablesHeight = (int) (drawableHeight*ratioMultiplier);

            // Placing powerRect to right position
            powerRect = new Rect( 0, - newDrawablesHeight, 0, 0);
        }

        @Override
        protected  void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Draw all the invulnerable objects
            for(InvulnerableObject obstacle : invulnerables){

                obstacle.getDrawable().setBounds(obstacle.getRect());
                obstacle.getDrawable().draw(canvas);

            }
            // Draw all the collectibles objects
            for(CollectibleObject obstacle : collectibles) {

                obstacle.getDrawable().setBounds(obstacle.getRect());
                obstacle.getDrawable().draw(canvas);
            }
        }
    }
}
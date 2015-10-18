package com.example.hyperion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.hyperion.ObstacleObjects.Invulnerable;
import com.example.hyperion.ObstacleObjects.FenceLeft;
import com.example.hyperion.ObstacleObjects.FenceRight;
import com.example.hyperion.ObstacleObjects.Rocks;

import com.example.hyperion.ObstacleObjects.Collectible;
import com.example.hyperion.ObstacleObjects.Power;
import com.example.hyprion.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Initializes all the logic for different obstacles, placement, speed and the graphics
 *
 * @author 		Daniel Edsinger
 * @version		0.3
 * @since		2015-10-14
 */
public class Obstacles {

    /**
     * View holder
     */
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
     * List of invulnerable objects
     */
    private ArrayList<Invulnerable> invulnerables;
    private ArrayList<Collectible> collectibles;

    private int height; // Screen height in pixels
    private int width;  // Screen width in pixels
    private float obstacleMoveSpeed;

    /**
     * Position for each lane on screen. Used for spawning obstacles.
     */
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

    private static final String TAG = "MyMessage";

    /**
     * Get the resolution of the screen and initialize the scrollspeed
     *
     * @param metrics - resolution of screen
     */
    public Obstacles (DisplayMetrics metrics) {

        invulnerables = new ArrayList<>();
        collectibles = new ArrayList<>();

        height = metrics.heightPixels;
        width = metrics.widthPixels;
        obstacleMoveSpeed = height/100.0f;


        // Calculate where spawnposition is for each lane
        double roadRatio = 28.0f/ 320.0f;
        double laneLength = roadRatio * height;

        laneOnePos = (int) ((width/2) - (2.5*laneLength) + (laneLength/28));
        laneOneEndPos = (int) ((width/2) - (1.5*laneLength) - (laneLength/28));

        laneTwoPos = (int) ((width/2) - (1.5*laneLength) + (laneLength/28));
        laneTwoEndPos = (int) ((width/2) - (0.5*laneLength) - (laneLength/28));

        laneThreePos = (int) ((width/2) - (0.5*laneLength) + (laneLength/28));
        laneThreeEndPos = (int) ((width/2) + (0.5*laneLength) - (laneLength/28));

        laneFourPos = (int) ((width/2) + (0.5*laneLength) + (laneLength/28));
        laneFourEndPos = (int) ((width/2) + (1.5*laneLength) - (laneLength/28));

        laneFivePos = (int) ((width/2) + (1.5*laneLength) + (laneLength/28));
        laneFiveEndPos = (int) ((width/2) + (2.5*laneLength) - (laneLength/28));
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
     * Public getter for list of Invulnerable on screen
     *
     * @return - the list of invulnerable
     */
    public ArrayList<Invulnerable> getInvulnerables(){
        return invulnerables;
    }

    /**
     * Public getter for list of Collectibles on screen
     *
     * @return - the list of collectibles
     */
    public ArrayList<Collectible> getCollectibles(){
        return collectibles;
    }

    /**
     * Functions to spawn obstacles-----------------------------------------------------------------
     *
     *----------------------------------------------------------------------------------------------
     */

    /**
     * Spawn a rock on the left side of the road.
     */
    public void spawnRockLeft() {
        invulnerables.add( new Rocks(new Rect(rocksRectL)) );
        Log.i(TAG, "Spawning rock");
    }

    /**
     * Spawn a rock one the right side of the road.
     */
    public void spawnRockRight() {
        invulnerables.add( new Rocks(new Rect(rocksRectR)) );
        Log.i(TAG, "Spawning rock");
    }

    /**
     * Spawn a fence on the left side of the road.
     */
    public void spawnFenceLeft() {
        invulnerables.add( new FenceLeft(new Rect(fenceRectL)) );
        Log.i(TAG, "Spawning fence");
    }

    /**
     * Spawn a fence on the right size of the road.
     */
    public void spawnFenceRight() {
        invulnerables.add( new FenceRight(new Rect(fenceRectR)) );
        Log.i(TAG, "Spawning fence");
    }

    /**
     * Spawn a power on a lane.
     *
     * @param lane - the lane to spawn power
     */
    public void spawnPower(int lane) {

        if(! (lane < 1 || lane > 5) ) {
            Rect tmp = new Rect(powerRect);
            spawnOnLane(lane, tmp);

            collectibles.add(new Power((tmp)));
            Log.i(TAG, "Spawning power");
        }
    }

    /**
     * Mutator to set which lane to spawn object on.
     *
     * @param lane - on which lane to spawn
     * @param rect - mutate to set this Rect's bounds to follow a specific lane
     */
    private void spawnOnLane(int lane, Rect rect) {
        switch(lane) {
            case 1: rect.set(laneOnePos, rect.top, laneOneEndPos, 0);
                    break;

            case 2: rect.set(laneTwoPos, rect.top, laneTwoEndPos, 0);
                    break;

            case 3: rect.set(laneThreePos, rect.top, laneThreeEndPos, 0);
                    break;

            case 4: rect.set(laneFourPos, rect.top, laneFourEndPos, 0);
                    break;

            case 5: rect.set(laneFivePos, rect.top, laneFiveEndPos, 0);
                    break;
        }
    }

    /**
     *----------------------------------------------------------------------------------------------
     *
     * End of spawn functions-----------------------------------------------------------------------
     */


    /**
     * Move all obstacles down the road towards the player. Removes obstacles if not on screen anymore.
     */
    public void obstaclesMovement() {
        //Iterator for invulnerable
        for (Iterator iterator = invulnerables.iterator(); iterator.hasNext();)
        {
            Invulnerable obstacle = (Invulnerable) iterator.next();
            obstacle.moveObstacle(obstacleMoveSpeed);

            if(obstacle.getRect().top > height) {
                iterator.remove();
                Log.i(TAG, "Removed invulnerable. Invulnerables left in array: " + invulnerables.size());
            }
        }

        //Iterator for collectibles
        for (Iterator iterator = collectibles.iterator(); iterator.hasNext();)
        {
            Collectible obstacle = (Collectible) iterator.next();
            obstacle.moveObstacle(obstacleMoveSpeed);

            if(obstacle.getRect().top > height) {
                iterator.remove();
                Log.i(TAG, "Removed collectible. Collectibles left in array: " + collectibles.size());
            }
        }

        view.invalidate();
    }

    /**
     * Inner class for drawing the obstacles.
     *
     * @author 		Daniel Edsinger
     * @version		0.3
     * @since		2015-10-13
     */
    private class ObstaclesView extends View {

        /**
         * Drawables for every obstacle.
         */
        Drawable drawableRocks;
        Drawable drawableFenceLeft;
        Drawable drawableFenceRight;
        Drawable drawablePower;
        //Add moore

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
            rocksRectL = new Rect( laneOnePos, - newDrawablesHeight, laneOnePos + newDrawablesWidth, 0);
            rocksRectR = new Rect( laneTwoPos, - newDrawablesHeight, laneTwoPos + newDrawablesWidth, 0);
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
            fenceRectL = new Rect( laneOneEndPos - newDrawablesWidth, - newDrawablesHeight, laneOneEndPos, 0);
            fenceRectR = new Rect( laneFivePos, - newDrawablesHeight, laneFivePos + newDrawablesWidth, 0);
        }

        /**
         * Create and calculate the size of the power to match screen and initialize the Rect to be placed
         * above the screen. The lane to spawn on is decided later.
         */
        public void createPowerRect() {
            drawablePower = getResources().getDrawable(R.drawable.power);

            float drawableHeight = drawableRocks.getIntrinsicHeight();
            float drawableWidth = drawableRocks.getIntrinsicWidth();
            double expectedRatio = 24.0f/ 320.0f;
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
            for(Invulnerable obstacle : invulnerables){
                if(obstacle instanceof Rocks) {
                    drawableRocks.setBounds(obstacle.getRect());
                    drawableRocks.draw(canvas);

                }else if(obstacle instanceof FenceLeft){
                    drawableFenceLeft.setBounds(obstacle.getRect());
                    drawableFenceLeft.draw(canvas);

                }else if(obstacle instanceof FenceRight) {
                    drawableFenceRight.setBounds(obstacle.getRect());
                    drawableFenceRight.draw(canvas);

                }
            }
            // Draw all the collectibles objects
            for(Collectible obstacle : collectibles) {
                if(obstacle instanceof Power) {
                    drawablePower.setBounds(obstacle.getRect());
                    drawablePower.draw(canvas);
                }
            }
        }
    }

}

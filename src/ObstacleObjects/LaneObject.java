package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Abstract Class for an object in the lane.
 *
 * @author 		Daniel Edsinger
 * @version		1.0
 * @since		2015-10-15
 */
 
public abstract class LaneObject {
    private final Rect rect;
    private final Drawable image;

    /**
     * Constructs a lane object
     *
     * @param rect  - size of the object
     * @param image - of the object  
     */
    public LaneObject(Rect rect, Drawable image) {
        this.rect = rect;
        this.image = image;
    }

    /**
     * Public getter for rect
     *
     * @return the size of the object
     */
    public Rect getRect() {
        return rect;
    }

    /**
     * Public getter for image
     *
     * @return the drawable of the object
     */
    public Drawable getDrawable() {
        return image;
    }

    /**
     * Moves the rect down.
     *
     * @param scrollSpeed   - How far it moves down.
     */
    public void moveObstacle(int scrollSpeed){
        rect.set(rect.left, rect.top + scrollSpeed, rect.right, rect.bottom + scrollSpeed);
    }
}

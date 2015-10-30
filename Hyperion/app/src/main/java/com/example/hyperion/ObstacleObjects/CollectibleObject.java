package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Class holding position of collectable object and scrolling it down the road.
 *
 * @author 		Daniel Edsinger
 * @version		1.0
 * @since		2015-10-15
 */
 
public class CollectibleObject extends LaneObject {

    /**
     * Creates a collectable object
     *
     * @param rect  - size of the collectable object
     * @param image - of the collectable object
     */
    public CollectibleObject(Rect rect, Drawable image) {
        super(rect, image);
    }
}

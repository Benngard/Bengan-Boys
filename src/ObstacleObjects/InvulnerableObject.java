package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Class holding position an invulernable object and scrolling it down the road.
 *
 * @author 		Daniel Edsinger
 * @version		1.0
 * @since		2015-10-15
 */
 
public class InvulnerableObject extends LaneObject {

    /**
     * Creates an invunerable object
     *
     * @param rect  - size of the invunerable object
     * @param image - of the invunerable object
     */
    public InvulnerableObject(Rect rect, Drawable image) {
        super(rect, image);
    }
}

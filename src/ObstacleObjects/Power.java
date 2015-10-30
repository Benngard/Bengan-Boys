package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Class holding position of power-pickup and scrolling it down the road.
 *
 * @author 		Daniel Edsinger
 * @version		1.0
 * @since		2015-10-15
 */
 
public class Power extends CollectibleObject
{
    /**
     * Creates a power collectable
     *
     * @param rect  - size of the power collectable
     * @param image - of the power collectable
     */
    public Power(Rect rect, Drawable image) {
        super(rect, image);
    }
}

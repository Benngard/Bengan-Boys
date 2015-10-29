package com.example.hyperion.Obstacle;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.hyperion.ObstacleObjects.LaneObject;

/**
 * Keeps the Rect for the lightning-attack and moves it up the screen
 *
 * @author 		Ola Andersson, Daniel Edsinger
 * @version		2.1
 * @since		2015-09-29
 */

public class LightingBolt extends LaneObject {

    /**
     *  asd
     *
     *  @param rect
     */
    public LightingBolt(Rect rect, Drawable image) {
        super(rect, image);
    }

}
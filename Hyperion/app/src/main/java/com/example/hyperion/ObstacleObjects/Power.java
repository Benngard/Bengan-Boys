package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;

/**
 * Class holding position of power-pickup and scrolling it down the road.
 *
 * @author 		Daniel Edsinger
 * @version		0.3
 * @since		2015-10-15
 */
public class Power implements Collectible{

    private Rect rect;

    public Power(Rect rect){
        this.rect = rect;
    }

    public Rect getRect(){
        return rect;
    }

    public void moveObstacle(double scrollSpeed){
        rect.set(rect.left,(int) (rect.top + scrollSpeed), rect.right,(int) (rect.bottom + scrollSpeed));
    }
}

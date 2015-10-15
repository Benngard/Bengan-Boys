package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;

/**
 * Class holding position of fence and scrolling it down the road.
 *
 * @author 		Daniel Edsinger
 * @version		0.3
 * @since		2015-10-15
 */
public class FenceLeft implements Invulnerable{

    private Rect rect;

    public FenceLeft(Rect rect){
        this.rect = rect;
    }

    public Rect getRect(){
        return rect;
    }

    public void moveObstacle(double scrollSpeed){
        rect.set(rect.left,(int) (rect.top + scrollSpeed), rect.right,(int) (rect.bottom + scrollSpeed));
    }
}

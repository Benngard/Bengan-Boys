package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * @author 		Daniel Edsinger
 * @version		1.0
 * @since		2015-10-29
 */
public abstract class LaneObject {
    private Rect rect;
    private Drawable image;

    public LaneObject(Rect rect, Drawable image) {
        this.rect = rect;
        this.image = image;
    }

    public Rect getRect() {
        return rect;
    }

    public Drawable getDrawable() {
        return image;
    }

    public void moveObstacle(int scrollSpeed){
        rect.set(rect.left, rect.top + scrollSpeed, rect.right, rect.bottom + scrollSpeed);
    }
}

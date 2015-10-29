package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;

/**
 * Created by Daniel on 2015-10-15.
 */
public interface Collectible {
    Rect getRect();
    void moveObstacle(double scrollSpeed);
}

package com.example.hyperion.ObstacleObjects;

import android.graphics.Rect;

/**
 * Created by Daniel on 2015-10-04.
 */
public interface Invulnerable {
    Rect getRect();
    void moveObstacle(double scrollSpeed);
}

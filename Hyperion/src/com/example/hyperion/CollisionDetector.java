package com.example.hyperion;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by Anton on 2015-10-12.
 * Basic outline for the class CollisionDetector...
 * @version  0.0
 */
public class CollisionDetector {

    public void checkCollsion (View first, View second) {
        //There might be a need to check so that top < bottom in the case where a View just enters the screen.
        Rect firstRect = new Rect ();
        first.getHitRect(firstRect);

        Rect secondRect = new Rect();
        second.getHitRect(secondRect);

        /* What class is responsible for handeling collisions? update pending. */
        if(Rect.intersects (firstRect, secondRect)){
            //Do something
        }
    }

}

package com.example.hyperion;


import android.graphics.Rect;


import com.example.hyprion.R;

/**
 * Keeps the Rect for the lightning-attack and moves it up the screen
 *
 * @author 		Ola Andersson, Daniel Edsinger
 * @version		0.4
 * @since		2015-09-29
 */
public class LightingBolt {

    private Rect rect;

    /**
     *Initializes a lightingbolt animation.
     */

    public LightingBolt(Rect rect){
        this.rect = rect;
    }

    public void moveObstacle(double scrollSpeed){
        rect.set(rect.left, (int) (rect.top - scrollSpeed), rect.right, (int) (rect.bottom - scrollSpeed));
    }

    /**
     * Public method to get a Rect.
     *
     * @return - a rect for this lightning.
     */
    public Rect getRect(){
        return rect;
    }

}

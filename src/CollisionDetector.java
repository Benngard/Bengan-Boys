package com.example.hyperion;

import android.graphics.Rect;

import com.example.hyperion.ObstacleObjects.Collectible;
import com.example.hyperion.ObstacleObjects.Invulnerable;
import com.example.hyperion.ObstacleObjects.Rocks;

import java.util.List;

/**
 * @author Anton Andrén
 * @since 2015-10-10
 * @version  0.5
 *
 * Class for detecting and handeling collisions between objects in Playfield.
 */
public class CollisionDetector {

    public void checkCollsion (List<LightingBolt>lightingBolts, Bus bus, Obstacles obstacles) {
        Rect busRect = bus.getRect();

        for (LightingBolt lb : lightingBolts){
            for (Invulnerable inv : obstacles.getInvulnerables()){
                if(lb.getRect().intersect(inv.getRect())){
                    // despawn lightning
                }
            }
        }

        for (Invulnerable invulnerable : obstacles.getInvulnerables()){
            if(busRect.intersect(invulnerable.getRect())){
                // Trigger game over.
            }
        }

        for (Collectible collectible : obstacles.getCollectibles()){
            if(busRect.intersect(collectible.getRect())){
                bus.getPowerComponent().addPower();
            }
        }
    }

}

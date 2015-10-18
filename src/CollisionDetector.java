package com.example.hyperion;

import android.graphics.Rect;

import com.example.hyperion.ObstacleObjects.Collectible;
import com.example.hyperion.ObstacleObjects.Invulnerable;
import com.example.hyperion.ObstacleObjects.Rocks;

import java.util.List;

/**
 * @author Anton Andrén
 * @since 2015-10-10
 * @version  0.6
 *
 * Class for detecting and handling collisions between objects in Playfield.
 */
public class CollisionDetector {

    /**
     * CheckCollisions checks for collisions between objects in the PlayField and handles them
     * @param lightingBolts - List of all the LightinBolt objects in playField
     * @param bus           - The bus object in PlayField
     * @param obstacles     - Contains lists of all the invulnerable and Collectible objects in the PlayField
     */
    public void checkCollsion (List<LightingBolt>lightingBolts, Bus bus, Obstacles obstacles) {
        Rect busRect = bus.getRect();

        for (LightingBolt lb : lightingBolts){
            for (Invulnerable inv : obstacles.getInvulnerables()){
                if(lb.getRect().intersect(inv.getRect())){
                    lightingBolts.remove(lb);
                }
            }
        }

        for (Invulnerable invulnerable : obstacles.getInvulnerables()){
            if(busRect.intersect(invulnerable.getRect())){
                // run game over
            }
        }

        for (Collectible collectible : obstacles.getCollectibles()){
            if(busRect.intersect(collectible.getRect())){
                bus.getPowerComponent().addPower();
                obstacles.getCollectibles().remove(collectible);
            }
        }
    }

}

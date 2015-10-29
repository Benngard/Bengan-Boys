package com.example.hyperion.Obstacle;

import android.graphics.Rect;

import com.example.hyperion.Bus.*;
import com.example.hyperion.ObstacleObjects.CollectibleObject;
import com.example.hyperion.ObstacleObjects.InvulnerableObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for detecting and handling collisions between objects in Playfield.
 *
 * @author      Anton Andrén
 * @since       2015-10-10
 * @version     2.1
 */
 
public class CollisionDetector {

    private final LinkedList<LightingBolt> lbRemove = new LinkedList<>();
    private final LinkedList<CollectibleObject> collectiblesRemove = new LinkedList<>();

    /**
     * CheckCollisions checks for collisions between objects in the PlayField and handles them
     *
     * @param lightingBolts - List of all the LightinBolt objects in playField
     * @param bus           - The bus object in PlayField
     * @param obstacles     - Contains lists of all the invulnerable and CollectibleObjects in the PlayField
     * @return if the busds collided with an obstacle or not.
     */
    public boolean checkCollsion (List<LightingBolt>lightingBolts, Bus bus, Obstacles obstacles) {
        Rect busRect = new Rect(bus.getRect());

        for (LightingBolt lb : lightingBolts){
            for (InvulnerableObject inv : obstacles.getInvulnerables()){
                if(lb.getRect().intersect(inv.getRect())){
                    lbRemove.add(lb);
                }
            }
        }
        while(!lbRemove.isEmpty()){
            lightingBolts.remove(lbRemove.poll());
        }


        for (CollectibleObject collectible : obstacles.getCollectibles()){
            if(busRect.intersect(collectible.getRect())){
                bus.getPowerComponent().addPower();
                collectiblesRemove.add(collectible);
            }
        }
        while(!collectiblesRemove.isEmpty()){
            obstacles.getCollectibles().remove(collectiblesRemove.poll());
        }

        for (InvulnerableObject invulnerable : obstacles.getInvulnerables()){
            if(busRect.intersect(invulnerable.getRect())){
                return true;
            }
        }

        return false;
    }
}
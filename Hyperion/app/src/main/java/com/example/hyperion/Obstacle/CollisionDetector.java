package com.example.hyperion.Obstacle;

import android.graphics.Rect;

import com.example.hyperion.Bus.*;
import com.example.hyperion.ObstacleObjects.Collectible;
import com.example.hyperion.ObstacleObjects.Invulnerable;
import com.example.hyperion.ObstacleObjects.Rocks;
import com.example.hyperion.Obstacle.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Anton Andrén
 * @since 2015-10-10
 * @version  2.0
 *
 * Class for detecting and handling collisions between objects in Playfield.
 */
public class CollisionDetector {

    private final LinkedList<LightingBolt> lbRemove = new LinkedList<>();
    private final LinkedList<Collectible> collectiblesRemove = new LinkedList<>();

    /**
     * CheckCollisions checks for collisions between objects in the PlayField and handles them
     * @param lightingBolts - List of all the LightinBolt objects in playField
     * @param bus           - The bus object in PlayField
     * @param obstacles     - Contains lists of all the invulnerable and Collectible objects in the PlayField
     */
    public boolean checkCollsion (List<LightingBolt>lightingBolts, Bus bus, Obstacles obstacles) {
        Rect busRect = new Rect(bus.getRect());


        for (LightingBolt lb : lightingBolts){
            for (Invulnerable inv : obstacles.getInvulnerables()){
                if(lb.getRect().intersect(inv.getRect())){
                    lbRemove.add(lb);
                }
            }
        }
        while(!lbRemove.isEmpty()){
            lightingBolts.remove(lbRemove.poll());
        }


        for (Collectible collectible : obstacles.getCollectibles()){
            if(busRect.intersect(collectible.getRect())){
                bus.getPowerComponent().addPower();
                collectiblesRemove.add(collectible);
            }
        }
        while(!collectiblesRemove.isEmpty()){
            obstacles.getCollectibles().remove(collectiblesRemove.poll());
        }

        for (Invulnerable invulnerable : obstacles.getInvulnerables()){
            if(busRect.intersect(invulnerable.getRect())){
                return true;
            }
        }

        return false;
    }

}

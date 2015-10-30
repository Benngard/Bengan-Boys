package com.example.hyperion.Obstacle;

import android.util.Log;

import com.example.hyperion.Signal.SignalType;

/**
 * ObstacleSpawner is responsible for spawning all the objects spawning on the top of the screen.
 *
 * @author  Anton Andrén
 * @version 2.0
 * @since   2015-10-15
 */

public class ObstacleSpawner
{
    private final Obstacles obstacles;
    private static final String TAG = "ObstacleSpawner";

    /**
     * Constructor for the ObstacleSpawner, saves a reference of Onstacles and bussReader
     * 
     * @param obstacles - container for obstacles
     */
    public ObstacleSpawner (Obstacles obstacles) {
        this.obstacles = obstacles;
    }

    /**
     * Spawns objects on the top of the screen based on the eventQueue in bussReader and also spawns energy refills.
     *
     * @param signalType - which signal to execute
     */
    public void spawnObstacle (SignalType signalType) {
        Log.i(TAG, "ObstacleSpawner current signal: " + signalType.toString());

        switch (signalType) {
            case DOOR:
                obstacles.spawnFenceLeft();
                obstacles.spawnFenceRight();
                break;
            case INDICATOR:
                obstacles.spawnRockLeft();
                break;
            case STOP:
                obstacles.spawnRockRight();
                break;
            case EMPTY:
                break;
        }
    }
}
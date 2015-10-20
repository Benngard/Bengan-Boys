package com.example.hyperion;


import android.util.Log;

/**
 * @author Anton Andrén
 * @version 1.0
 * @since 2015-10-15
 *
 * ObstacleSpawner is responsible for spawning all the objects spawning on the top of the screen.
 */

public class ObstacleSpawner {

    Obstacles obstacles;
    private static final String TAG = "MyMessage";

    /**
     * Constructor for the ObstacleSpawner, saves a reference of Onstacles and bussReader
     * @param obstacles
     */
    public ObstacleSpawner (Obstacles obstacles) {
        this.obstacles = obstacles;
    }

    /**
     * Spawns objects on the top of the screen based on the eventQueue in bussReader and also spawns energy refills.
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
            default:
                break;
        }
    }
}

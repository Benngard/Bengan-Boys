package com.example.hyperion;


/**
 * @author Anton Andrén
 * @version 0.75
 * @since 2015-10-15
 *
 * ObstacleSpawner is responsible for spawning all the objects spawning on the top of the screen.
 */

public class ObstacleSpawner {

    Obstacles obstacles;
    BussReader bussReader;
    int frequencyTimer = 0;

    /**
     * Constructor for the ObstacleSpawner, saves a reference of Onstacles and bussReader
     * @param obstacles
     * @param bussReader
     */
    public ObstacleSpawner (Obstacles obstacles, BussReader bussReader) {
        this.obstacles = obstacles;
        this.bussReader = bussReader;
    }

    /**
     * Spawns objects on the top of the screen based on the eventQueue in bussReader and also spawns energy refills.
     */
    public void spawnObstacle () {
        frequencyTimer++;
        if (frequencyTimer % 15 == 0) {
            switch (bussReader.getEvent()) {
                case DOOR:
                    obstacles.spawnFenceLeft();
                    obstacles.spawnFenceRight();
                    obstacles.spawnPower(3);
                    break;
                case INDICATOR:
                    obstacles.spawnRockLeft();
                    obstacles.spawnPower(5);
                    break;
                case STOP:
                    obstacles.spawnRockRight();
                    obstacles.spawnPower(1);
                    break;
                case EMPTY:
                    obstacles.spawnPower(2);
                    break;
                default:
                    break;
            }
        }
    }
}

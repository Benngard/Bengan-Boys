package com.example.hyperion.Obstacle;

/**
 * Created by Daniel on 2015-10-29.
 */
public class LanePosition {
    private int start;
    private int end;

    public LanePosition(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}

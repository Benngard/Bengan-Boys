package com.example.hyperion.Obstacle;

/**
 * Class for detecting and handling collisions between objects in Playfield.
 *
 * @author      Daniel Edsinger
 * @since       2015-10-29
 * @version     2.0
 */
 
public class LanePosition {
    private final int start;
    private final int end;

    /**
     * Constructs a Lane
     *
     * @param start - start position of the lane in x
     * @param end   - end position of the lane in x
     */
    public LanePosition(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Public getter for start
     *
     * @return start position of the lane in x
     */
    public int getStart() {
        return start;
    }

    /**
     * Public getter for end
     *
     * @return end position of the lane in x
     */
    public int getEnd() {
        return end;
    }
}

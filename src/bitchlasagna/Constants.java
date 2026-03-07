package bitchlasagna;

import battlecode.common.*;

public class Constants {

    public static final Direction[] DIRECTIONS = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST
    };

    public static final int ADJACENT_RADIUS_SQUARED = 2;
    public static final int RUIN_PATTERN_RADIUS_SQUARED = 8;
    public static final int GREEDY_BLOCKED_PENALTY = 100000;
    public static final int GREEDY_ENEMY_PAINT_PENALTY = 8;
    public static final int GREEDY_EMPTY_PAINT_PENALTY = 2;
    public static final int GREEDY_ALLY_PAINT_BONUS = -2;

    public static final int UNSET_DISTANCE = -1;
    public static final int GLOBAL_SENSE_RADIUS = -1;
    public static final int MIN_REMAINING_PAINT_AFTER_MOVE = 2;
    public static final int ENEMY_PAINT_MOVE_LOSS = 2;
    public static final int EMPTY_PAINT_MOVE_LOSS = 1;

}
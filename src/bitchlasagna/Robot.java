package bitchlasagna;

import battlecode.common.*;

/**
 * Methods fir general robot behavior.
 */
public class Robot {
    // low paint = penalty cooldown; tolerate 40% for further robots
    public static boolean hasLowPaint(RobotController rc, int percentage) {
        if (rc.getPaint() < (rc.getType().paintCapacity)*percentage) {
            return true;
        } else {
            return false;
        }
    }

    

}

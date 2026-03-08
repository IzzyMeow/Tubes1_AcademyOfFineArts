package bitchlasagna;

import battlecode.common.*;

/**
 * Methods for general robot behavior.
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

    public static void markTower(RobotController rc, UnitType towerType, MapLocation targetLoc) throws GameActionException {
        MapLocation shouldBeMarked = targetLoc.subtract(rc.getLocation().directionTo(targetLoc));
        if (rc.senseMapInfo(shouldBeMarked).getMark() == PaintType.EMPTY &&
                rc.canMarkTowerPattern(towerType, targetLoc)){
            rc.markTowerPattern(towerType, targetLoc);
        }
    }

    public static void completeTower(RobotController rc, MapLocation towerLocation) throws GameActionException {
        if (rc.canCompleteTowerPattern(UnitType.LEVEL_ONE_MONEY_TOWER, towerLocation)) {
            rc.completeTowerPattern(UnitType.LEVEL_ONE_MONEY_TOWER, towerLocation);
        }
        if (rc.canCompleteTowerPattern(UnitType.LEVEL_ONE_PAINT_TOWER, towerLocation)) {
            rc.completeTowerPattern(UnitType.LEVEL_ONE_PAINT_TOWER, towerLocation);
        }
        if (rc.canCompleteTowerPattern(UnitType.LEVEL_ONE_DEFENSE_TOWER, towerLocation)) {
            rc.completeTowerPattern(UnitType.LEVEL_ONE_DEFENSE_TOWER, towerLocation);
        }
    }

    

}

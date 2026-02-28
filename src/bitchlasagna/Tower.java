package bitchlasagna;

import battlecode.common.*;

public class Tower {
    public static void createSoldier(RobotController rc) throws GameActionException {
        if (rc.canBuildRobot(UnitType.SOLDIER, location)) {
            rc.buildRobot(UnitType.SOLDIER, location);
        }
    }

    public static void createMopper(RobotController rc) throws GameActionException {
        if (rc.canBuildRobot(UnitType.MOPPER, location)) {
            rc.buildRobot(UnitType.MOPPER, location);
        }
    }

    public static void createSplasher(RobotController rc) throws GameActionException {
        if (rc.canBuildRobot(UnitType.MOPPER, location)) {
            rc.buildRobot(UnitType.MOPPER, location);
        }
    }
}
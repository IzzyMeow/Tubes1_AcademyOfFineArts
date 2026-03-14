package bitchlasagna;

import battlecode.common.*;

public class Mopper extends Robot {

    /**
     * transfer paint to the lowest-paint ally nearby, return true if transferred
     */
    public static boolean transferPaintToAlly(RobotController rc) throws GameActionException {
        if (rc.getPaint() <= Constants.MOPPER_MIN_PAINT_TO_TRANSFER) return false;

        RobotInfo ally = Sensing.findLowestPaintAlly(rc, Constants.MOPPER_SENSE_RADIUS);
        if (ally != null && rc.canTransferPaint(ally.getLocation(), Constants.MOPPER_TRANSFER_AMOUNT)) {
            rc.transferPaint(ally.getLocation(), Constants.MOPPER_TRANSFER_AMOUNT);
            return true;
        }
        return false;
    }

    /**
     * mop the enemy robot with highest paint nearby, return true if mopped
     */
    public static boolean mopEnemy(RobotController rc) throws GameActionException {
        RobotInfo enemy = Sensing.findHighestPaintEnemy(rc, Constants.MOPPER_SENSE_RADIUS);
        if (enemy != null && rc.canAttack(enemy.getLocation())) {
            rc.attack(enemy.getLocation());
            return true;
        }
        return false;
    }

    /**
     * mopper action: transfer paint to allies first, then mop enemies
     */
    public static void mopperAction(RobotController rc) throws GameActionException {
        if (transferPaintToAlly(rc)) return;
        mopEnemy(rc);
    }

    /**
     * score a direction for perimeter movement
     */
    private static int scorePerimeterDirection(RobotController rc, MapLocation nextLoc, MapInfo nextLocInfo,
            RobotInfo[] allies, MapInfo[] mapInfos) {
        if (nextLocInfo.getPaint().isEnemy()) return Integer.MIN_VALUE;

        int score = 0;

        if (nextLocInfo.getPaint().isAlly()) {
            score += Constants.MOPPER_ALLY_PAINT_SCORE;
        }

        for (RobotInfo ally : allies) {
            if (nextLoc.distanceSquaredTo(ally.getLocation()) <= Constants.MOPPER_SENSE_RADIUS) {
                if (ally.getType() != UnitType.MOPPER
                        && ally.getPaintAmount() < Constants.MOPPER_LOW_ALLY_PAINT) {
                    score += Constants.MOPPER_NEAR_LOW_ALLY_SCORE;
                }
            }
        }

        for (MapInfo adjInfo : mapInfos) {
            if (nextLoc.distanceSquaredTo(adjInfo.getMapLocation()) <= Constants.MOPPER_SENSE_RADIUS) {
                if (adjInfo.getPaint() == PaintType.EMPTY || adjInfo.getPaint().isEnemy()) {
                    score += Constants.MOPPER_BORDER_TILE_SCORE;
                }
            }
        }

        return score;
    }

    /**
     * move toward the border between ally and enemy/empty territory
     */
    public static void moveToPerimeter(RobotController rc) throws GameActionException {
        MapLocation myLoc = rc.getLocation();
        Direction bestDir = null;
        int maxScore = Integer.MIN_VALUE;
        RobotInfo[] allAllies = rc.senseNearbyRobots(Constants.GLOBAL_SENSE_RADIUS, rc.getTeam());
        MapInfo[] allMapInfos = rc.senseNearbyMapInfos(Constants.GLOBAL_SENSE_RADIUS);

        for (Direction dir : Constants.DIRECTIONS) {
            if (!rc.canMove(dir)) continue;

            MapLocation nextLoc = myLoc.add(dir);
            MapInfo nextLocInfo = rc.senseMapInfo(nextLoc);

            int score = scorePerimeterDirection(rc, nextLoc, nextLocInfo, allAllies, allMapInfos);
            if (score > maxScore) {
                maxScore = score;
                bestDir = dir;
            }
        }

        if (bestDir != null) {
            rc.move(bestDir);
        }
    }
}
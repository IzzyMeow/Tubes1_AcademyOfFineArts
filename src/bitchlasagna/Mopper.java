package bitchlasagna;

import battlecode.common.*;

public class Mopper extends Robot {
    // Fungsi Utama
    public static void run(RobotController rc) throws GameActionException {
        while (true) {
            try {
                if (rc.isActionReady()) {
                    mopperAction(rc);
                }
                
                if (rc.isMovementReady()) {
                    moveToPerimeter(rc);
                }
            } catch (GameActionException e) {
                System.out.println("Mopper is error!");
                e.printStackTrace();
            }

            Clock.yield();
        }
    }

    // Penggunaan Skill Mopper
    private static void mopperAction(RobotController rc) throws GameActionException {
        MapLocation myLoc = rc.getLocation();
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots(2);
        RobotInfo healTarget = null;
        RobotInfo enemyTarget = null;
        int minPaintAlly = 999;
        int maxPaintEnemy = -1;

        // Pencarian robot teman dengan paint terdikit & robot musuh dengan paint terbanyak
        for (RobotInfo robot : nearbyRobots) {
            if (robot.getTeam() == rc.getTeam()) {
                if (robot.getType() != UnitType.MOPPER && robot.getPaintAmount() < minPaintAlly) {
                    minPaintAlly = robot.getPaintAmount();
                    healTarget = robot;
                }
            } else {
                if (robot.getPaintAmount() > maxPaintEnemy) {
                    maxPaintEnemy = robot.getPaintAmount();
                    enemyTarget = robot;
                }
            }
        }

        // Heal teman sebisa mungkin
        // TODO: canTransferPaint(location, paintUsed) plssss
        if (healTarget != null && rc.getPaint() > 30 && rc.canTransferPaint(healTarget.getLocation(), -20)) {
            // TODO: transferPaint(location, paintUsed)
            rc.transferPaint(healTarget.getLocation(), 20);
            return;
        }

        // TODO: canMop(location)
        if (enemyTarget != null) {
            MapLocation enemyLoc = enemyTarget.getLocation();
            if (rc.canAttack(enemyLoc)) {
                rc.attack(enemyLoc);
            }
        }
    }

    // Fungsi Menentukan Pergerakan (Target: menjaga perbatasan)
    private static void moveToPerimeter(RobotController rc) throws GameActionException {
        MapLocation myLoc = rc.getLocation();
        Direction bestDir = null;
        int maxScore = -9999;
        RobotInfo[] allAllies = rc.senseNearbyRobots(20, rc.getTeam());
        MapInfo[] allMapInfos = rc.senseNearbyMapInfos(20);

        //  TODO: allDirections()
        for (Direction dir : Constants.DIRECTIONS) {
            if (dir == Direction.CENTER || !rc.canMove(dir)) continue;

            MapLocation nextLoc = myLoc.add(dir);
            MapInfo nextLocInfo = rc.senseMapInfo(nextLoc);

            if (nextLocInfo.getPaint().isEnemy() && nextLocInfo.getPaint() != PaintType.EMPTY) continue;

            int currScore = 0;

            if (nextLocInfo.getPaint().isAlly()) {
                currScore += 10;
            }

            for (RobotInfo ally : allAllies) {
                if (nextLoc.distanceSquaredTo(ally.getLocation()) <= 2) {
                    if (ally.getType() != UnitType.MOPPER && ally.getPaintAmount() < 50) {
                        currScore += 50;
                    }
                }
            }

            // TODO: senseNearbyLocations(location, radius)
            for (MapInfo adjInfo : allMapInfos) {
                if (nextLoc.distanceSquaredTo(adjInfo.getMapLocation()) <= 2) {
                    if (adjInfo.getPaint() == PaintType.EMPTY || adjInfo.getPaint().isEnemy()) {
                        currScore += 5;
                    }
                }
            }

            if (currScore > maxScore) {
                maxScore = currScore;
                bestDir = dir;
            }
        }

        if (bestDir != null) {
            rc.move(bestDir);
        }
    }
}
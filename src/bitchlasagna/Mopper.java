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
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots(2);
        RobotInfo healTarget = null;
        RobotInfo enemyTarget = null;
        int minPaintAlly = 999;
        int maxPaintEnemy = -1;

        // Pencarian robot teman dengan paint terdikit & robot musuh dengan paint terbanyak
        for (RobotInfo robot : nearbyRobots) {
            if (robot.getTeam() == rc.getTeam()) {
                if (robot.getType() != RobotType.Mopper && robot.getPaintAmount() < minPaintAlly) {
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
        if (healTarget != null && rc.getPaintAmount() > 30 && rc.canTransferPaint(healTarget.getLocation(), -20)) {
            // TODO: transferPaint(location, paintUsed)
            rc.transferPaint(healTarget.getLocation(), 20);
            return;
        }

        // TODO: canMop(location)
        if (enemyTarget != null && rc.canMop(enemyTarget.getLocation())) {
            rc.mop(enemyTarget.getLocation());
        }
    }

    // Fungsi Menentukan Pergerakan (Target: menjaga perbatasan)
    private static void moveToPerimeter(RobotController rc) throws GameActionException {
        MapLocation myLoc = rc.getLocation();
        Direction bestDirection = null;
        int maxScore = -9999;

        //  TODO: allDirections()
        for (Direction dir : Direction.allDirections()) {
            if (dir == Direction.center || !rc.canMove(dir)) continue;

            MapLocation nextLoc = myLoc.add(dir);
            MapInfo nextLocInfo = rc.senseMapInfo(nextLoc);

            if (nextLocInfo.getPaint() != rc.getTeam() && nextLocInfo.getPaint() != PaintType.empty) continue;

            int currScore = 0;

            if (nextLocInfo.getPaint() == rc.getTeam()) {
                currScore += 10;
            }

            RobotInfo[] robotsNearNextLoc = rc.senseNearbyRobots(nextLoc, 2, rc.getTeam());
            for (RobotInfo ally : robotsNearNextLoc) {
                if (ally.getType() != RobotType.Mopper && ally.getPaintAmount() < 50) {
                    currScore += 50;
                }
            }

            // TODO: senseNearbyLocations(location, radius)
            MapLocation[] adjTiles = rc.senseNearbyLocations(nextLoc, 2);
            for (MapLocation adj : adjTiles) {
                MapInfo adjInfo = rc.senseMapInfo(adj);
                if (adjInfo.getPaint() == PaintType.EMPTY || adjInfo.getPaint() != rc.getTeam()) {
                    currScore += 5;
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
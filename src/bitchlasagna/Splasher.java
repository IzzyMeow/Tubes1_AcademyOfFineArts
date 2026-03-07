package bitchlasagna;

import battlecode.common.*;

public class Splasher extends Robot {
    
    public static void run(RobotController rc) throws GameActionException {
        while (true) {
            try {
                if (rc.isActionReady() && rc.getPaint() >= 50) {
                    splashTower(rc);
                }

                if (rc.isMovementReady()) {
                    moveToEnemy(rc);
                }
            } catch (GameActionException e) {
                System.out.println("Splasher is error!");
                e.printStackTrace();
            }

            Clock.yield();
        }
    }

    // Fungsi Throw Splash ke Tower (Tower adalah Koentji)
    private static void splashTower(RobotController rc) throws GameActionException {
        MapLocation[] centerCandidates = rc.senseNearbyLocations(4);
        MapLocation bestTarget = null;
        int maxScore = -1;

        for (MapLocation center : centerCandidates) {
            if (rc.canAttack(center)) {
                int currScore = 0;
                RobotInfo[] robotsInSplash = rc.senseNearbyRobots(center, 4);

                for (RobotInfo robot : robotsInSplash) {
                    if (robot.getTeam() != rc.getTeam()) {
                        if (robot.getType().isTowerType()) {
                            currScore += 1000;
                        } else if (center.distanceSquaredTo(robot.getLocation() <= 2)) {
                            currScore += 20;
                        }
                    }
                }

                MapLocation[] tilesInSplash = rc.senseNearbyLocations(center, 4);
                for (MapLocation tile : tilesInSplash) {
                    MapInfo tileInfo = rc.senseMapInfo(tile);

                    if (tileInfo.getPaint() != rc.getTeam()) {
                        if (tileInfo.getPaint() != PaintType.EMPTY && center.distanceSquaredTo(tile) <= 2) {
                            currScore += 5
                        } else if (tileInfo.getPaint() == PaintType.EMPTY) {
                            currScore += 2;
                        }
                    }
                }

                if (currScore > maxScore) {
                    maxScore = currScore;
                    bestTarget = center;
                }
            }
        }

        if (bestTarget != null && maxScore > 0) {
            rc.attack(bestTarget);
        }
    }

    private static void moveToEnemy(RobotController rc) throws GameActionException {
        MapLocation myLoc = rc.getLocation();
        Direction bestDir = null;
        int minDist = 99999;
        RobotInfo[] visibleEnemies = rc.senseNearbyLocations(20, rc.getTeam().opponent());
        MapLocation targetLoc = null;

        for (RobotInfo enemy : visibleEnemies) {
            if (enemy.getType().isTower()) {
                targetLoc = enemy.getLocation();
                break;
            }
        }

        if (targetLoc == null && visibleEnemies.length > 0) {
            targetLoc = visibleEnemies[0].getLocation();
        }

        for (Direction dir : Direction.v()) {
            if (dir == Direction.center || !rc.canMove(dir)) continue;

            MapLocation nextLoc = myLoc.add(dir);

            if (targetLoc != null) {
                int dist = nextLoc.distanceSquaredTo(targetLoc);
                if (dist < minDist) {
                    minDist = dist;
                    bestDir = dir;
                }
            } else {
                MapInfo nextInfo = rc.senseMapInfo(nextLoc);
                if (nextInfo == PaintType.EMPTY || nextInfo.getPaint() != rc.getTeam()) {
                    bestDir = dir;
                    break;
                }
            }
        }

        if (bestDir != null) {
            rc.move(bestDir);
        }
    }
}
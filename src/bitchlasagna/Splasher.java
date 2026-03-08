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
        MapInfo[] centerCandidates = rc.senseNearbyMapInfos(4);
        MapLocation bestTarget = null;
        int maxScore = -1;
        RobotInfo[] allNearbyRobots = rc.senseNearbyRobots(20);
        MapInfo[] allMapInfos = rc.senseNearbyMapInfos(20);

        for (MapInfo centerInfo : centerCandidates) {
            MapLocation center = centerInfo.getMapLocation();

            if (rc.canAttack(center)) {
                int currScore = 0;

                for (RobotInfo unit : allNearbyRobots) {
                    if (center.distanceSquaredTo(unit.getLocation()) <= 4) {
                        if (unit.getTeam() != rc.getTeam()) {
                            if (unit.getType().isTowerType()) {
                                currScore += 1000;
                            } else if (center.distanceSquaredTo(unit.getLocation()) <= 2) {
                                currScore += 20;
                            }
                        }
                    }
                }

                for (MapInfo tileInfo : allMapInfos) {
                    MapLocation tileLoc = tileInfo.getMapLocation();

                    if (center.distanceSquaredTo(tileLoc) <= 4) {
                        if (tileInfo.getPaint().isEnemy()) {
                            if (center.distanceSquaredTo(tileLoc) <= 2) {
                                currScore += 5;
                            }
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
        RobotInfo[] visibleEnemies = rc.senseNearbyRobots(20, rc.getTeam().opponent());
        MapLocation targetLoc = null;

        for (RobotInfo enemy : visibleEnemies) {
            if (enemy.getType().isTowerType()) {
                targetLoc = enemy.getLocation();
                break;
            }
        }

        if (targetLoc == null && visibleEnemies.length > 0) {
            targetLoc = visibleEnemies[0].getLocation();
        }

        for (Direction dir : Constants.DIRECTIONS) {
            if (dir == Direction.CENTER || !rc.canMove(dir)) continue;

            MapLocation nextLoc = myLoc.add(dir);

            if (targetLoc != null) {
                int dist = nextLoc.distanceSquaredTo(targetLoc);
                if (dist < minDist) {
                    minDist = dist;
                    bestDir = dir;
                }
            } else {
                MapInfo nextInfo = rc.senseMapInfo(nextLoc);
                if (nextInfo.getPaint() == PaintType.EMPTY || nextInfo.getPaint().isEnemy()) {
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
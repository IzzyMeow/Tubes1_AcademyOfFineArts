package bitchlasagna;

import battlecode.common.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RobotPlayer {
    static int turnCount = 0;

    public static void run(RobotController rc) throws GameActionException {
        System.out.println("runn");  // delete this

        while (true) {
            turnCount++;

            try {
                switch (rc.getType()) {
                    case SOLDIER:
                        runSoldier(rc);  // not implemented
                        break;
                    case MOPPER:
                        runMopper(rc);  // not implemented
                        break;
                    case SPLASHER:
                        runSplasher(rc);  // not implemented
                        break;
                    default:
                        runTower(rc); // not implemented
                        break;
                }
            } catch (GameActionException e) {
                System.out.println(rc.getType() + " GameActionException");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            } finally {
                Clock.yield();  // end turn
            }

        }
    }
    
    public static void runSoldier(RobotController rc) throws GameActionException {
        System.out.println("runSoldier");  // delete this
    }

    public static void runMopper(RobotController rc) throws GameActionException {
        System.out.println("runMopper");  // delete this
    }

    public static void runSplasher(RobotController rc) throws GameActionException {
        System.out.println("runSplasher");  // delete this
    }

    public static void runTower(RobotController rc) throws GameActionException {
        System.out.println("runTower");  // delete this

        rc.getRoundNum();
    }

}
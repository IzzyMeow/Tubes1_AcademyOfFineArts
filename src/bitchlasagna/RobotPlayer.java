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
                        Soldier.run(rc);  // not implemented
                        break;
                    case MOPPER:
                        Mopper.run(rc);  // not implemented
                        break;
                    case SPLASHER:
                        Splasher.run(rc);  // not implemented
                        break;
                    default:
                        Tower.run(rc); // not implemented
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
    

}
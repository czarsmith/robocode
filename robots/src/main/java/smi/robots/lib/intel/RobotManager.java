package smi.robots.lib.intel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import smi.robots.lib.MyUtils;

public class RobotManager {
    /**
     * Intel about our current target.
     */
    public static Intel TARGET = null;
    
    /**
     * Intel about ourselves.
     */
    private static SelfIntel ROBOT = null;
    
    /**
     * All the enemy intel we've collected.
     */
    private static Map<String, Intel> ENEMIES = new HashMap<String, Intel>();

    public static void reset() {
        ENEMIES.clear();
        TARGET = null;
        ROBOT = null;
    }

    public static SelfIntel getMyRobot() {
        if (ROBOT == null) {
            ROBOT = new SelfIntel();
        }
        return ROBOT;
    }

    public static Intel getTarget() {
        return TARGET;
    }

    public static void setTarget(Intel enemy) {
        TARGET = enemy;
    }

    public static boolean isTarget(String name) {
        return isTarget(getEnemy(name));
    }

    public static boolean isTarget(Intel enemy) {
        return TARGET == enemy;
    }
    
    public static Intel getEnemy(String name) {
        Intel ret = ENEMIES.get(name);
        if (ret == null) {
            ret = new Intel();
            ret.setName(name);
            ENEMIES.put(name, ret);
        }

        return ret;
    }

    public static Collection<Intel> getAllEnemies() {
        return ENEMIES.values();
    }

    public static SelfIntel doTurn(AdvancedRobot robot) {
        SelfIntel data = getMyRobot();

        double angularVelocity = 0;
        double acceleration = 0;
        // Calculate the angular velocity and acceleration
        SelfObservation lastObs = (SelfObservation)data.getLatestObservation();
        if (lastObs != null) {
            angularVelocity = MyUtils.getAngularVelocity(
                    lastObs.getHeadingRadians(), robot.getHeadingRadians(),
                    lastObs.getTime(), robot.getTime());
            acceleration = Math.abs(robot.getVelocity()) - Math.abs(lastObs.getVelocity());
        }

        SelfObservation obs = new SelfObservation(robot.getTime(), robot.getVelocity(),
            angularVelocity, acceleration, robot.getHeadingRadians(), 0,
            0, robot.getEnergy(), robot.getX(), robot.getY(), robot.getOthers());

        return (SelfIntel)data.addObservation(obs);        
    }

    public static Intel onScannedRobot(AdvancedRobot robot, ScannedRobotEvent e) {
        Intel data = getEnemy(e.getName());

        double absBearing = robot.getHeadingRadians() + e.getBearingRadians();
        double angularVelocity = 0;
        double acceleration = 0;
        // Calculate the angular velocity and acceleration
        Observation lastObs = data.getLatestObservation();
        if (lastObs != null) {
            angularVelocity = MyUtils.getAngularVelocity(
                    lastObs.getHeadingRadians(), e.getHeadingRadians(),
                    lastObs.getTime(), e.getTime());
            acceleration = Math.abs(e.getVelocity()) - Math.abs(lastObs.getVelocity());
        }

        Observation obs = new Observation(e.getTime(), e.getVelocity(),
            angularVelocity, acceleration, e.getHeadingRadians(), e.getBearingRadians(),
            e.getDistance(), e.getEnergy(),
            robot.getX() + e.getDistance() * Math.sin(absBearing),
            robot.getY() + e.getDistance() * Math.cos(absBearing));

        return data.addObservation(obs);
    }

    public static Intel onRobotDeath(RobotDeathEvent e) {
        return getEnemy(e.getName()).setAlive(false);
    }
}

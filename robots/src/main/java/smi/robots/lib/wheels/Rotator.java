package smi.robots.lib.wheels;

import static robocode.util.Utils.getRandom;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.ScannedRobotEvent;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;

public class Rotator extends Wheel {
    /**
     * Indicates how far we'll travel forward/backward before changing direction. 
     */
    private double travel;

    /**
     * Indicates our preferred distance from the target.
     */
    private double buffer;

    /**
     * Indicates whether we're going forward or backward.
     */
    private double direction = 1;

    public Rotator(AdvancedRobot robot) {
        this(robot, 200, 300);
    }
    
    public Rotator(AdvancedRobot robot, double buffer, double travel) {
        super(robot);
        this.buffer = buffer;
        this.travel = travel;
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public void onEvent(Event e, boolean takeAction) {
        if (e instanceof ScannedRobotEvent && takeAction) {
            onScannedRobot((ScannedRobotEvent)e);
        }
    }
    
    private void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());
        if (RobotManager.isTarget(enemy)) {
            // Forward/Backward
            if (Math.abs(robot.getDistanceRemaining()) <= 10) {
                direction *= -1;
                robot.setAhead(direction * getRandom().nextDouble() * travel);
            }
    
            // Turn
            double turnRad = Math.abs(enemy.getBearingRadians()) - Math.PI/2 + getOvershoot(robot, enemy);
            robot.setTurnRightRadians(enemy.getBearingRadians() >= 0 ? turnRad : -turnRad);
        }
    }

    /**
     * An approximation of wall smoothing is also employed to keep us off the walls
     * as well as some overturn to close in on the target.  This is used when moving
     * in an oscillating rotator pattern.
     */
    private double getOvershoot(AdvancedRobot robot, Intel enemyData) {
        double xbw = robot.getBattleFieldWidth() - robot.getX();
        double ybh = robot.getBattleFieldHeight() - robot.getY();
        double minxy = Math.min(Math.min(xbw, ybh), Math.min(robot.getX(), robot.getY()));
        double deg = ((enemyData.getDistance() - buffer) / 5 + Math.max(0, 110 - minxy));
        return direction * deg / 180 * Math.PI; // Convert to radians    
    }
}

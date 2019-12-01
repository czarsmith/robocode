package smi.robots.dad.lib.wheels;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.RobotManager;

public class StopAndGo extends Wheel {
    /**
     * Indicates our preferred distance from the target.
     */
    private double buffer;

    /**
     * Indicates whether we're going forward or backward.
     */
    private double direction = 1;

    public StopAndGo(AdvancedRobot robot) {
        this(robot, 200);
    }
    
    public StopAndGo(AdvancedRobot robot, double buffer) {
        super(robot);
        this.buffer = buffer;
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
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
            double de = enemy.getPreviousEnergy() - enemy.getEnergy();
            if (de > 0 && de <= 3 && !hitWall(robot, enemy, de) &&
                enemy.getPreviousTime() >= robot.getTime() - 1) {

                // Forward/Backward
                if (Math.abs(robot.getDistanceRemaining()) == 0) {
                    robot.setAhead(direction * robot.getWidth());
                }
    
                // Turn
                double turnRad = Math.abs(enemy.getBearingRadians()) - Math.PI/2 + getOvershoot(robot, enemy);
                robot.setTurnRightRadians(enemy.getBearingRadians() >= 0 ? turnRad : -turnRad);
            }
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

    private boolean hitWall(AdvancedRobot robot, Intel enemy, double firePower) {
        if (enemy.getVelocity() == 0 && enemy.getPreviousVelocity() != 0) {
            double wallHitDamage = Rules.getWallHitDamage(enemy.getPreviousVelocity()); 
            return Utils.isNear(firePower, wallHitDamage);
        }
        return false;
    }
    
    @Override
    public boolean isMelee() {
        return false;
    }
}

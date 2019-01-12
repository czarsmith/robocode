package smi.robots.dad.lib.guns;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import smi.robots.dad.lib.MyUtils;
import smi.robots.dad.lib.firepower.FirePower;
import smi.robots.dad.lib.intel.Intel;

/**
 * This a pure circular targeting algorithm with 20 iterations.  It also moves
 * the target into the battlefield if the algorithm places the target outside
 * the battlefield.
 */
public class CircularGun extends Gun {
    private Point2D.Double point;

    public CircularGun(AdvancedRobot robot, FirePower firepower) {
        super(robot, firepower, Color.CYAN);
    }

    public String getName() {
        return getClass().getName();
    }

    @Override
    public VirtualBullet fire(Intel enemy, double firepower) {
        point = new Point2D.Double();
        point = MyUtils.computeFutureBearingCircular(robot.getX(), robot.getY(),
            enemy.getAngularVelocity(), enemy.getVelocity(), enemy.getX(), enemy.getY(),
            enemy.getHeadingRadians(),
            firepower, robot.getBattleFieldWidth(), robot.getBattleFieldHeight());

        return new VirtualBullet(getName(), enemy.getName(), robot.getX(), robot.getY(),
            point.x, point.y, robot.getTime(), firepower);
    }
}

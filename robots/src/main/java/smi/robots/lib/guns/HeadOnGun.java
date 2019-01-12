package smi.robots.lib.guns;

import java.awt.Color;

import robocode.AdvancedRobot;
import smi.robots.lib.firepower.FirePower;
import smi.robots.lib.intel.Intel;

public class HeadOnGun extends Gun {
    public HeadOnGun(AdvancedRobot robot, FirePower firepower) {
        super(robot, firepower, Color.MAGENTA);
    }

    public String getName() {
        return getClass().getName();
    }

    @Override
    public VirtualBullet fire(Intel enemy, double firePower) {
        VirtualBullet ret = null;
        double absoluteBearing = robot.getHeadingRadians() + enemy.getBearingRadians();
        ret = new VirtualBullet(getName(), enemy.getName(), robot.getX(),
            robot.getY(), absoluteBearing, robot.getTime(), firePower);
        return ret;
    }
}

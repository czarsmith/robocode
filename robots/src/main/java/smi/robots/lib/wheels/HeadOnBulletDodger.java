package smi.robots.lib.wheels;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.Rules;
import smi.robots.lib.MyUtils;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.wheels.antigravity.BulletDodger;
import smi.robots.lib.wheels.antigravity.forces.LinearTravelingTangentalForce;

public class HeadOnBulletDodger extends BulletDodger {
    public HeadOnBulletDodger(AdvancedRobot robot) {
        this(robot, DEFAULT_BULLET_MAGNITUDE, DEFAULT_BULLET_DECAY);
    }

    public HeadOnBulletDodger(AdvancedRobot robot, double bulletStrength, double bulletDecay) {
        super(robot, bulletStrength, bulletDecay);
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    protected void createBullet(Intel enemy) {
        double de = enemy.getPreviousEnergy() - enemy.getEnergy();
        double bulletVelocity = Rules.getBulletSpeed(de);
        long expires = robot.getTime() + (long)(enemy.getDistance() / bulletVelocity) + 10;

        LinearTravelingTangentalForce tbf =
            new LinearTravelingTangentalForce(enemy.getName(), enemy.getX(), enemy.getY(),
                enemy.getPreviousTime(),
                MyUtils.getHeading(enemy.getX(), enemy.getY(), robot.getX(), robot.getY()),
                bulletVelocity, expires, bulletStrength, bulletDecay);
        liveBullets.add(tbf);
    }
}
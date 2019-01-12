package smi.robots.dad.lib.wheels;

import java.awt.Color;
import java.util.Collection;

import robocode.AdvancedRobot;
import smi.robots.dad.lib.MyUtils;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.wheels.antigravity.BulletDodger;
import smi.robots.dad.lib.wheels.antigravity.forces.Force;
import smi.robots.dad.lib.wheels.antigravity.forces.LinearTravelingTangentalForce;

public class CircularBulletDodger extends BulletDodger {
    private double lastHeading;
    private double myAngularVelocity;

    public CircularBulletDodger(AdvancedRobot robot) {
        this(robot, DEFAULT_BULLET_MAGNITUDE, DEFAULT_BULLET_DECAY);
    }

    public CircularBulletDodger(AdvancedRobot robot, double bulletStrength, double bulletDecay) {
        super(robot, bulletStrength, bulletDecay);
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    @Override
    public Collection<Force> getForces() {
        myAngularVelocity = MyUtils.getAngularVelocity(
            lastHeading, robot.getHeadingRadians(), 0, 1);
        lastHeading = robot.getHeadingRadians();

        return super.getForces();
    }

    protected void createBullet(Intel enemy) {
        LinearTravelingTangentalForce tbf =
            LinearTravelingTangentalForce.circular(
            robot, myAngularVelocity, enemy, bulletStrength, bulletDecay);
        liveBullets.add(tbf);
    }
}

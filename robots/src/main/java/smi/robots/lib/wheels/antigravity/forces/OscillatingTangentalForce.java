package smi.robots.lib.wheels.antigravity.forces;

import java.awt.geom.Point2D;
import java.util.Random;

import robocode.AdvancedRobot;

/**
 * This is a basic tangental force that switches directions after a configurable
 * amount of time.  It is used to create a rotator movement.
 */
public class OscillatingTangentalForce extends TangentalForce {
    private Random rand = new Random(System.currentTimeMillis());
    private int minDuration;
    private int maxDuration;
    private long nextChange;

    public OscillatingTangentalForce() {}

    public OscillatingTangentalForce(double x, double y,
        double strength, double decay, int minDuration, int maxDuration) {
        super(x, y, strength, decay, 1);
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
    }

    public OscillatingTangentalForce setMinDuration(int value) {
        this.minDuration = value;
        return this;
    }
    
    public OscillatingTangentalForce setMaxDuration(int value) {
        this.maxDuration = value;
        return this;
    }

    @Override
    public Point2D getForceVector(AdvancedRobot robot) {
        if (robot.getTime() > nextChange) {
            if (rand.nextInt(4) < 3) {
                setDirection(getDirection() * -1);
            }

            nextChange = robot.getTime() + minDuration + (long)(rand.nextDouble() * (maxDuration - minDuration));
        }
        
        return super.getForceVector(robot);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("\n\tabsoluteBearing: ").append(getAbsoluteBearingRadians());
        return sb.toString();
    }
}

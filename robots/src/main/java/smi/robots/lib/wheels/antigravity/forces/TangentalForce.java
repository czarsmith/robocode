package smi.robots.lib.wheels.antigravity.forces;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;

/**
 * A tangental force is one that acts perpendicular to the bearing to the
 * target. For example, if the force is located to your east, then it will
 * push you north or south depending on the force's direction.
 */
public class TangentalForce extends PointForce {
    private double absoluteBearingRadians;

    // 1 = forward, -1 = backward
    private int direction = 1;

    public TangentalForce() {}

    public TangentalForce(double x, double y, double strength, double decay,
        int direction) {
        super(x, y, strength, decay);
        this.direction = direction;
    }

    public double getAbsoluteBearingRadians() {
        return absoluteBearingRadians;
    }
    
    public TangentalForce setAbsoluteBearingRadians(double value) {
        this.absoluteBearingRadians = value;
        return this;
    }

    public Point2D getForceVector(AdvancedRobot robot) {
        super.getForceVector(robot);
        
        return new Point2D.Double(
            getDirection() * Math.sin(absoluteBearingRadians - Math.PI/2) * getMagnitude() / Math.pow(getDistance(), getDecay()),
            getDirection() * Math.cos(absoluteBearingRadians - Math.PI/2) * getMagnitude() / Math.pow(getDistance(), getDecay()));
    }
    
    protected int getDirection() {
        return direction;
    }
    
    protected void setDirection(int value) {
        direction = value;
    }
}

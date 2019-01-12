package smi.robots.dad.lib.wheels.antigravity.forces;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;

public abstract class Force {
    private String id;
    /**
     * The "size" of the force.
     */
    private double magnitude;
    
    private double decay;
    private double distance;
    
    /**
     * The force exerted by this gravitational force based on the distance
     * from the object.
     */
    protected double force;
    
    public Force() {}

    public Force(double magnitude, double decay) {
        this.magnitude = magnitude;
        this.decay = decay;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public double getMagnitude() {
        return magnitude;
    }
    
    public void setMagnitude(double value) {
        this.magnitude = value;
    }

    public double getDecay() {
        return decay;
    }
    
    public void setDecay(double value) {
        this.decay = value;
    }
    
    public double getDistance() {
        return distance;
    }

    public Force setDistance(double dist) {
        this.distance = dist;
        return this;
    }

    /**
     * Returns the force vector exerted on the given robot.
     */
    public abstract Point2D getForceVector(AdvancedRobot robot);
}

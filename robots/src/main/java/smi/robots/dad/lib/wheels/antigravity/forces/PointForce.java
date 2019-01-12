package smi.robots.dad.lib.wheels.antigravity.forces;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.Robot;
import smi.robots.dad.lib.MyUtils;

public class PointForce extends Force {
    private double x;
    private double y;
    private double bearing;
    private long expires = -1;
    private int polarity = -1; // Repulsive

    public PointForce() {
        this(0, 0, 0, 0);
    }

    public PointForce(double x, double y, double magnitude, double decay) {
        this(x, y, magnitude, decay, -1);
    }

    public PointForce(double x, double y, double magnitude, double decay, int polarity) {
        super(magnitude, decay);
        this.x = x;
        this.y = y;
        this.polarity = polarity;
    }

    public Point2D getForceVector(AdvancedRobot robot) {
        setDistance(MyUtils.getDistance(robot.getX(), robot.getY(), getX(), getY()));
        setBearing(MyUtils.normalizeBearing(Math.PI / 2
            - Math.atan2(robot.getY() - getY(), robot.getX() - getX())));

        double strength = getMagnitude() * polarity;
        if (expires >= 0 && robot.getTime() > getExpires()) {
            strength = 0;
        }

        double force = strength / Math.pow(getDistance(), getDecay());
        return new Point2D.Double(
            force * Math.sin(getBearing()),
            force * Math.cos(getBearing()));
    }

    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }

    public int getPolarity() {
        return polarity;
    }

    public double getX() {
        return x;
    }
    
    public PointForce setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public PointForce setY(double y) {
        this.y = y;
        return this;
    }

    public double getBearing() {
        return bearing;
    }

    public PointForce setBearing(double value) {
        this.bearing = value;
        return this;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long time) {
        expires = time;
    }
    
    public void draw(Robot robot) {
        if (polarity == 1) {
            draw(robot, 500, new Color(0, 255, 0)); // Attractive
        }
        else {
            draw(robot, 500, new Color(255, 0, 0)); // Repulsive
        }
    }
    
    private void draw(Robot robot, int force, Color c) {
        double r = Math.pow(getMagnitude() / force, 1/getDecay());
        robot.getGraphics().setColor(c);
        robot.getGraphics().drawOval((int)(getX() - r), (int)(getY() - r),
            (int)(r*2), (int)(r*2));
    }
}

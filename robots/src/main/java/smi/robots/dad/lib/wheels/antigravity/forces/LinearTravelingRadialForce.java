package smi.robots.dad.lib.wheels.antigravity.forces;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import smi.robots.dad.lib.intel.Intel;

/**
 * This is a basic point force that travels in a straight line and expires
 * at some specific time.  It also switches direction at a specific range
 */
public class LinearTravelingRadialForce extends RadialForce {
    protected double headingRadians;
    protected double speed;
    protected long startTime;
    protected double startX;
    protected double startY;

    public LinearTravelingRadialForce() {}

    /**
     * Creates a traveling force that follows the path of an enemy robot, assuming
     * that the enemy is traveling in a straight line at constant speed.
     */
    public LinearTravelingRadialForce(AdvancedRobot robot, Intel enemy,
        double magnitude, double decay) {
        double absoluteBearing = robot.getHeadingRadians() + enemy.getBearingRadians();
        double targetX = robot.getX() + enemy.getDistance() * Math.sin(absoluteBearing);
        double targetY = robot.getY() + enemy.getDistance() * Math.cos(absoluteBearing);
        setStartPos(targetX, targetY);
        setStartTime(robot.getTime());
        setHeadingRadians(enemy.getHeadingRadians());
        setSpeed(enemy.getVelocity());
        setExpires(robot.getTime() + 40);
        setMagnitude(magnitude);
        setDecay(decay);
        setId(enemy.getName());
    }

    public LinearTravelingRadialForce(String id, double startX, double startY,
        long startTime, double headingRadians, double speed, long expires,
        double magnitude, double decay) {
        super(startX, startY, magnitude, decay);
        setId(id);
        this.startX = startX;
        this.startY = startY;
        this.startTime = startTime;
        this.headingRadians = headingRadians;
        this.speed = speed;
        setExpires(expires);
        setMagnitude(magnitude);
        setDecay(decay);
        setHeadingRadians(headingRadians);
    }

    /**
     * Update our parameters with the new robot position.
     */
    public void update(AdvancedRobot robot, Intel enemy) {
        double absoluteBearing = robot.getHeadingRadians() + enemy.getBearingRadians();
        double targetX = robot.getX() + enemy.getDistance() * Math.sin(absoluteBearing);
        double targetY = robot.getY() + enemy.getDistance() * Math.cos(absoluteBearing);
        setHeadingRadians(enemy.getHeadingRadians());
        setStartPos(targetX, targetY);
        setStartTime(robot.getTime());
        setSpeed(enemy.getVelocity());
        setExpires(robot.getTime() + 40);
    }

    @Override
    public Point2D getForceVector(AdvancedRobot robot) {
        // Calculate the position of this force.
        double travel = (robot.getTime() - startTime) * getSpeed();
        setX(Math.sin(getHeadingRadians()) * travel + startX);
        setY(Math.cos(getHeadingRadians()) * travel + startY);
        return super.getForceVector(robot);
    }
    
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long time) {
        this.startTime = time;
    }

    public void setStartPos(double x, double y) {
        this.startX = x;
        this.startY = y;
    }

    public double getSpeed() {
        return speed;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public double getHeadingRadians() {
        return headingRadians;
    }
    
    public void setHeadingRadians(double headingRadians) {
        this.headingRadians = headingRadians;
    }

    public double getStartX() {
        return startX;
    }
    
    public double getStartY() {
        return startY;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("\n\tstartX:   ").append(startX);
        sb.append("\n\tstartY:   ").append(startY);
        sb.append("\n\ttime:     ").append(startTime);
        sb.append("\n\theading:  ").append(getHeadingRadians());
        sb.append("\n\tspeed:    ").append(getSpeed());
        sb.append("\n\texpires:  ").append(getExpires());
        
        return sb.toString();
    }
}

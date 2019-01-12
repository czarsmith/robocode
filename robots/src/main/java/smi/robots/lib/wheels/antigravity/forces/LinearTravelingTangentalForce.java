package smi.robots.lib.wheels.antigravity.forces;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.util.Utils;
import smi.robots.lib.MyUtils;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.Observation;

/**
 * This is a basic tangental force that moves.  It's primarily used to follow
 * bullets across the battlefield.
 */
public class LinearTravelingTangentalForce extends TangentalForce {
    protected String enemy;
    protected double headingRadians;
    protected double speed;
    protected long expires;
    protected long startTime;
    protected double startX;
    protected double startY;
    public LinearTravelingTangentalForce() {}
    
    public LinearTravelingTangentalForce(String enemy, double startX,
        double startY, long startTime, double headingRadians,
        double speed, long expires, double magnitude, double decay) {
        super(startX, startY, magnitude, decay, 1);
        this.enemy = enemy;
        this.startX = startX;
        this.startY = startY;
        this.startTime = startTime;
        this.headingRadians = headingRadians;
        this.speed = speed;
        this.expires = expires;
        setDecay(decay);
        setHeading(headingRadians);
    }

    @Override
    public Point2D getForceVector(AdvancedRobot robot) {
        double travel = (robot.getTime() - startTime) * getBulletSpeed();
        setX(Math.sin(getHeading()) * travel + startX);
        setY(Math.cos(getHeading()) * travel + startY);
        setAbsoluteBearingRadians(MyUtils.getHeading(
            getX(), getY(), robot.getX(), robot.getY()));
        
        // Decide to go left or right to avoid the bullet.
        double bearingFromBulletToRobot = MyUtils.getBearing(
            getHeading(), MyUtils.getHeading(getX(), getY(), robot.getX(), robot.getY()));
        setDirection(bearingFromBulletToRobot > 0 ? 1 : -1);

        // Once the bullet has passed, stop responding to it.
        double d = Utils.normalRelativeAngle(getAbsoluteBearingRadians() - getHeading());
        if (d > Math.PI / 2 || d < -Math.PI / 2) {
            setMagnitude(0);
        }

        return super.getForceVector(robot);
    }

    public String getEnemy() {
        return enemy;
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

    public long getExpires() {
        return expires;
    }

    public void setExpires(long time) {
        expires = time;
    }

    public double getBulletSpeed() {
        return speed;
    }
    
    public void setBulletSpeed(double speed) {
        this.speed = speed;
    }

    public double getHeading() {
        return headingRadians;
    }
    
    public void setHeading(double headingRadians) {
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
        sb.append("\n\theading:  ").append(getHeading());
        sb.append("\n\tspeed:    ").append(getBulletSpeed());
        sb.append("\n\texpires:  ").append(expires);
        
        return sb.toString();
    }

    public static LinearTravelingTangentalForce circular(
        AdvancedRobot robot, double myAngularVelocity,
        Intel enemy, double magnitude, double decay) {
        Observation eo = enemy.getLatestObservation();
        double de = enemy.getPreviousEnergy() - eo.getEnergy();
        double absoluteBearing = robot.getHeadingRadians() + eo.getBearingRadians();
        double targetX = robot.getX() + eo.getDistance() * Math.sin(absoluteBearing);
        double targetY = robot.getY() + eo.getDistance() * Math.cos(absoluteBearing);
        double bulletVelocity = Rules.getBulletSpeed(de);
        long expires = robot.getTime() + (long)(eo.getDistance() / bulletVelocity) + 10;

        Point2D.Double pos = MyUtils.computeFutureBearingCircular(targetX, targetY,
            myAngularVelocity, robot.getVelocity(), robot.getX(), robot.getY(),
            robot.getHeadingRadians(), de, robot.getBattleFieldWidth(), robot.getBattleFieldHeight());
        double angle = MyUtils.getHeading(targetX, targetY, pos.x, pos.y);

        return new LinearTravelingTangentalForce(enemy.getName(), targetX, targetY,
            enemy.getPreviousTime(), angle, bulletVelocity, expires,
            magnitude, decay);
    }
}

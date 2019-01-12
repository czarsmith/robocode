package smi.robots.lib.guns;

import smi.robots.lib.MyUtils;

public class VirtualBullet {
    /**
     * The gun that fired this bullet.
     */
    private String gun;
    private String enemy;
    private double firepower;
    private double fromX;
    private double fromY;
    private long startTime;

    /**
     * The toX and toY variables are optional.  Some guns calculate
     * a specific target point but all that we really need is a start
     * point and bearing.
     */
    private double toX = -1;
    private double toY = -1;

    /**
     * The heading of the bullet.
     */
    private double heading;
    
    /**
     * The bearing from head-on that the bullet is traveling.  This is optional.
     * Some guns keep track of this.
     */
    private double bearing;

    public VirtualBullet(String gun, String enemy, double fromX, double fromY,
        double toX, double toY, long startTime, double power) {
        this(gun, enemy, fromX, fromY,
            MyUtils.getHeading(fromX, fromY, toX, toY), startTime, power);
        this.toX = toX;
        this.toY = toY;
    }

    public VirtualBullet(String gun, String enemy, double fromX, double fromY,
        double heading, long startTime, double power) {
        this.gun = gun;
        this.enemy = enemy;
        this.fromX = fromX;
        this.fromY = fromY;
        this.heading = heading;
        this.startTime = startTime;
        this.firepower = power;
    }

    public double getPower() {
        return firepower;
    }

    public String getEnemy() {
        return enemy;
    }
    
    public void setEnemy(String enemy) {
        this.enemy = enemy;
    }

    public String getGun() {
        return gun;
    }
    
    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }
    
    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }
    
    public double getFromX() {
        return fromX;
    }
    
    public double getFromY() {
        return fromY;
    }

    public double getToX() {
        return toX;
    }
    
    public double getToY() {
        return toY;
    }

    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long time) {
        this.startTime = time;
    }
}

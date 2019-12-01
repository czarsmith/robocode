package smi.robots.dad.lib.guns.guessfactor;

import smi.robots.dad.lib.guns.VirtualBullet;

public class GuessBullet extends VirtualBullet {
    private double fromHeading;
    private double toHeading;
    
    public GuessBullet(String gun, String enemy, double fromX, double fromY,
        double heading, long startTime, double power, double fromHeading,
        double toHeading) {
        super(gun, enemy, fromX, fromY, heading, startTime, power);
        this.fromHeading = fromHeading;
        this.toHeading = toHeading;
    }
    
    public double getFromHeading() {
        return fromHeading;
    }
    
    public double getToHeading() {
        return toHeading;
    }
}

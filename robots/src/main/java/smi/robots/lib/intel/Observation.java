package smi.robots.lib.intel;

public class Observation {
    private long time;
    private double velocity;
    private double angularVelocity;
    private double acceleration;
    private double headingRadians;
    private double bearingRadians;
    private double distance;
    private double energy;
    private double x;
    private double y;

    public Observation(long time, double velocity, double angularVelocity,
        double acceleration, double headingRadians, double bearingRadians,
        double distance, double energy, double x, double y) {
        this.time = time;
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
        this.acceleration = acceleration;
        this.headingRadians = headingRadians;
        this.bearingRadians = bearingRadians;
        this.distance = distance;
        this.energy = energy;
        this.x = x;
        this.y = y;
    }

    public long getTime() {
        return time;
    }
    public double getVelocity() {
        return velocity;
    }
    public double getAngularVelocity() {
        return angularVelocity;
    }
    public double getAcceleration() {
        return acceleration;
    }
    public double getHeadingRadians() {
        return headingRadians;
    }
    public double getBearingRadians() {
        return bearingRadians;
    }
    public double getDistance() {
        return distance;
    }
    public double getEnergy() {
        return energy;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}

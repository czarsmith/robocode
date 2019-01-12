package smi.robots.lib.intel;

public class SelfObservation extends Observation {
    private int others;

    public SelfObservation(long time, double velocity, double angularVelocity,
        double acceleration, double headingRadians, double bearingRadians,
        double distance, double energy, double x, double y, int others) {
        super(time, velocity, angularVelocity, acceleration, headingRadians,
            bearingRadians, distance, energy, x, y);
        this.others = others;
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }
}

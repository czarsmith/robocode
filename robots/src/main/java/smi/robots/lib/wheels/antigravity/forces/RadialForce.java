package smi.robots.lib.wheels.antigravity.forces;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;

public class RadialForce extends PointForce {
    protected double optimumRange = Double.POSITIVE_INFINITY;

    public RadialForce() {}

    public RadialForce(double x, double y, double magnitude, double decay) {
        super(x, y, magnitude, decay);
    }

    @Override
    public Point2D getForceVector(AdvancedRobot robot) {
        setPolarity(getDistance() > optimumRange ? 1 : -1);
        return super.getForceVector(robot);
    }

    public double getOptimumRange() {
        return optimumRange;
    }
    
    public void setOptimumRange(double range) {
        optimumRange = range;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("\n\tx: ").append(getX());
        sb.append("\n\ty: ").append(getY());
        sb.append("\n\trange: ").append(getDistance());

        return sb.toString();
    }
}

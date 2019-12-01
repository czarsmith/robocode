package smi.robots.dad.lib.guns.guessfactor;

import smi.robots.dad.lib.guns.guessfactor.AcellerationSegment.Direction;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.Observation;

public class DistanceSegment extends Segment {
    private double minDist;
    private double maxDist;

    public DistanceSegment(double min, double max) {
        minDist = min;
        maxDist = max;

        addSubSegment(new AcellerationSegment(Direction.INCREASING));
        addSubSegment(new AcellerationSegment(Direction.DECREASING));
        addSubSegment(new AcellerationSegment(Direction.ZERO));
    }

    @Override
    protected boolean matches(Intel enemy, long time) {
        Observation eo = enemy.getObservationAtTime((int)time);
        if (eo != null) {
            return eo.getDistance() >= minDist && eo.getDistance() < maxDist;
        }
        return false;
    }
}

package smi.robots.dad.lib.guns.guessfactor;

import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.Observation;

public class SprintLengthSegment extends Segment {
    private double minTime;
    private double maxTime;

    public SprintLengthSegment(double min, double max) {
        minTime = min;
        maxTime = max;
    }

    @Override
    protected boolean matches(Intel enemy, long time) {
        Observation eo = enemy.getObservationAtTime((int)time);
        if (eo != null && Math.abs(eo.getVelocity()) > 0) {
            double speed = eo.getVelocity();
            int sprintLen = 0;
            long t = time;
            while ((eo = enemy.getObservationAtTime((int)t)) != null &&
                eo.getVelocity() == speed && sprintLen <= maxTime) {
                t--;
                sprintLen++;
            }

            return sprintLen >= minTime && sprintLen < maxTime;
        }

        return false;
    }
}

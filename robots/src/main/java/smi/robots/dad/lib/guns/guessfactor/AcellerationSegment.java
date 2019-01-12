package smi.robots.dad.lib.guns.guessfactor;

import robocode.util.Utils;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.Observation;

public class AcellerationSegment extends Segment {
    public enum Direction {
        INCREASING,
        DECREASING,
        ZERO
    }

    private Direction dir;

    public AcellerationSegment(Direction dir) {
        this.dir = dir;

        if (dir == Direction.ZERO) {
            addSubSegment(new SprintLengthSegment(0, 1));
            addSubSegment(new SprintLengthSegment(1, 2));
            addSubSegment(new SprintLengthSegment(2, 3));
            addSubSegment(new SprintLengthSegment(3, 4));
            addSubSegment(new SprintLengthSegment(4, 5));
            addSubSegment(new SprintLengthSegment(5, 6));
            addSubSegment(new SprintLengthSegment(6, 7));
            addSubSegment(new SprintLengthSegment(7, 8));
            addSubSegment(new SprintLengthSegment(8, 9));
            addSubSegment(new SprintLengthSegment(9, 10));
            addSubSegment(new SprintLengthSegment(10, Double.MAX_VALUE));
        }
    }

    @Override
    protected boolean matches(Intel enemy, long time) {
        Observation eo = enemy.getObservationAtTime((int)time);

        if (eo != null) {
            if (dir == Direction.INCREASING) {
                return eo.getAcceleration() > 0 && !Utils.isNear(eo.getAcceleration(), 0);
            }
            else if (dir == Direction.DECREASING) {
                return eo.getAcceleration() < 0 && !Utils.isNear(eo.getAcceleration(), 0);
            }
            else {
                return Utils.isNear(eo.getAcceleration(), 0);
            }
        }
        return false;
    }
}

package smi.robots.dad.lib.guns.guessfactor;

public class DefaultSegment extends Segment {
    public DefaultSegment() {
        addSubSegment(new WallProximitySegment(100, true));
        addSubSegment(new WallProximitySegment(100, false));
    }
}

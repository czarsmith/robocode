package smi.robots.dad.lib.intel;

public class SelfIntel extends Intel {
    public int getOthers() {
        return ((SelfObservation)getLatestObservation()).getOthers();
    }
}

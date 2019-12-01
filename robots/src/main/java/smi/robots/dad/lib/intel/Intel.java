package smi.robots.dad.lib.intel;

import java.util.ArrayList;
import java.util.List;

public class Intel {
    private String name;
    private boolean isAlive = true;
    private ArrayList<Observation> observations;

    public Intel() {
        observations = new ArrayList<Observation>();
    }

    public void reset() {
        observations.clear();
        isAlive = true;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public Intel setAlive(boolean value) {
        isAlive = value;
        return this;
    }
    
    public boolean isAlive() {
        return isAlive;
    }

    public double getBearingRadians() {
        return getLatestObservation().getBearingRadians();
    }

    public double getPreviousBearingRadians() {
        return getPreviousObservation().getBearingRadians();
    }
    
    public double getHeadingRadians() {
        return getLatestObservation().getHeadingRadians();
    }

    public double getVelocity() {
        return getLatestObservation().getVelocity();
    }
    
    public double getPreviousVelocity() {
        return getPreviousObservation().getVelocity();
    }
    
    public double getAngularVelocity() {
        return getLatestObservation().getAngularVelocity();
    }
    
    public double getDistance() {
        return getLatestObservation().getDistance();
    }
    
    public double getPreviousDistance() {
        return getPreviousObservation().getDistance();
    }
    
    public double getEnergy() {
        return getLatestObservation().getEnergy();
    }

    public double getPreviousEnergy() {
        return getPreviousObservation().getEnergy();
    }

    public long getTime() {
        return getLatestObservation().getTime();
    }

    public long getPreviousTime() {
        return getPreviousObservation().getTime();
    }
    
    public double getX() {
        return getLatestObservation().getX();
    }
    
    public double getY() {
        return getLatestObservation().getY();
    }

    /**
     * Returns the latest observation.
     */
    public Observation getLatestObservation() {
        Observation ob = null;
        int i = observations.size()-1;
        while (i >= 0 && (ob = observations.get(i--)) == null) {}
        return ob;
    }

    /**
     * Returns the second most recent observation or the current observation
     * if there is only one.
     */
    public Observation getPreviousObservation() {
        Observation ob = null;
        Observation ret = null;
        int i = observations.size()-1;
        while (i >= 0 && (ob = observations.get(i--)) == null) {}
        while (i >= 0 && (ret = observations.get(i--)) == null) {}
        if (ret == null) ret = ob;
        return ret;
    }

    /**
     * Returns null if there was no observation at the specified time, or
     * returns the latest observation if the specified time is in the future.
     */
    public Observation getObservationAtTime(int time) {
        Observation ret = null;
        int idx = Math.min(time, observations.size()-1);
        if (idx >= 0) {
            ret = observations.get(idx);
        }
        return ret;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public Intel addObservation(Observation obs) {
        while (observations.size() < obs.getTime()) observations.add(null);
        observations.add((int)obs.getTime(), obs);
        return this;
    }
}

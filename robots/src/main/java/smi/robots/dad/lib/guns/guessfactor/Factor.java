package smi.robots.dad.lib.guns.guessfactor;

public class Factor {
    private double factor;
    private long hits;
    private long attempts;

    public Factor(double factor) {
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }

    public double getHitPcnt() {
        if (attempts > 0) {
            return (double)hits / attempts;
        }
        return 0;
    }

    public long getHits() {
        return hits;
    }
    
    public void addHit() {
        hits++;
        attempts++;
    }

    public long getAttempts() {
        return attempts;
    }
    
    public void addMiss() {
        attempts++;
    }
}

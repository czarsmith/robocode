package smi.robots.dad.lib.wheels.antigravity.factor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import robocode.AdvancedRobot;
import robocode.util.Utils;
import smi.robots.dad.lib.intel.Intel;

public abstract class Segment {
    protected Map<String, List<Factor>> allStats;
    protected List<Segment> subSegments;

    public Segment() {
        allStats = new HashMap<String, List<Factor>>();
        subSegments = new ArrayList<Segment>();
    }

    public void init(AdvancedRobot robot) {
        for (Segment s : subSegments) {
            s.init(robot);
        }
    }
    
    public void addSubSegment(Segment s) {
        subSegments.add(s);
    }

    public Factor getBestFactor(Intel enemy) {
        Factor bestStat = null;
        if (matches()) {
            List<Factor> stats = getStats(enemy.getName());
            bestStat = stats.get(stats.size() / 2);
            for (Factor stat : stats) {
                if (stat.getHits() > bestStat.getHits()) {
                    bestStat = stat;
                }
            }

            // See if there's a sub-segment with a better score.
            for (Segment s : subSegments) {
                Factor f = s.getBestFactor(enemy);
                if (f != null && f.getHitPcnt() > bestStat.getHitPcnt()) {
                    bestStat = f;
                }
            }
        }

        return bestStat;
    }

    public void saveFactor(Intel enemy, double factor) {
        if (matches()) {
            List<Factor> stats = getStats(enemy.getName());
            for (Factor stat : stats) {
                if (Utils.isNear(stat.getFactor(), factor)) {
                    stat.addHit();
                }
                else {
                    stat.addMiss();
                }
            }
            
            for (Segment s : subSegments) {
                s.saveFactor(enemy, factor);
            }
        }
    }

    /**
     * The default Segment matches everything.
     */
    protected boolean matches() {
        return true;
    }

    protected List<Factor> getStats(String enemy) {
        List<Factor> ret = allStats.get(enemy);
        if (ret == null) {
            ret = new ArrayList<Factor>();
            ret.add(new Factor(-1));
            ret.add(new Factor(-0.9));
            ret.add(new Factor(-0.8));
            ret.add(new Factor(-0.7));
            ret.add(new Factor(-0.6));
            ret.add(new Factor(-0.5));
            ret.add(new Factor(-0.4));
            ret.add(new Factor(-0.3));
            ret.add(new Factor(-0.2));
            ret.add(new Factor(-0.1));
            ret.add(new Factor(0));
            ret.add(new Factor(0.1));
            ret.add(new Factor(0.2));
            ret.add(new Factor(0.3));
            ret.add(new Factor(0.4));
            ret.add(new Factor(0.5));
            ret.add(new Factor(0.6));
            ret.add(new Factor(0.7));
            ret.add(new Factor(0.8));
            ret.add(new Factor(0.9));
            ret.add(new Factor(1));
            allStats.put(enemy, ret);
        }

        return ret;
    }
}

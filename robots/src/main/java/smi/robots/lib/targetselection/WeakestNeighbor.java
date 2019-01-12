package smi.robots.lib.targetselection;

import robocode.AdvancedRobot;
import smi.robots.lib.intel.Intel;

public class WeakestNeighbor extends TargetSelection {
    public WeakestNeighbor(AdvancedRobot robot) {
        this(robot, 1.1);
    }

    public WeakestNeighbor(AdvancedRobot robot, double impedance) {
        super(robot);
        setImpedance(impedance);
    }

    @Override
    public double getEnemyValue(AdvancedRobot robot, Intel data) {
        return data.getEnergy() == 0 ? Double.POSITIVE_INFINITY : 1 / data.getEnergy();
    }
}

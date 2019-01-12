package smi.robots.lib.targetselection;

import robocode.AdvancedRobot;
import smi.robots.lib.intel.Intel;

public class StrongestNeighbor extends TargetSelection {
    public StrongestNeighbor(AdvancedRobot robot) {
        this(robot, 1.1);
    }
    
    public StrongestNeighbor(AdvancedRobot robot, double impedance) {
        super(robot);
        setImpedance(impedance);
    }

    @Override
    public double getEnemyValue(AdvancedRobot robot, Intel data) {
        return data.getEnergy();
    }
}
